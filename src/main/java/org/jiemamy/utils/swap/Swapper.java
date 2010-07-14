/*
 * Copyright 2007-2010 Jiemamy Project and the Others. Created on 2008/08/20
 * 
 * This file is part of Jiemamy.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.jiemamy.utils.swap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Comparator;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.utils.LogMarker;

/**
 * スワップ処理を行うクラス。
 * 
 * <p>
 * スワップ処理は{@link SwapObject }から要求を受けた場合にのみ行われる。<br>
 * そのため、このクラスのアクセス修飾子は無く、パッケージプライベートである。
 * </p>
 * 
 * <p>
 * スワップファイルは、このクラスの Singleton インスタンスが生成されるタイミングで生成される。<br>
 * なお、{@link File#createTempFile(String, String) }による一時ファイルをスワップファイルとしている。
 * </p>
 * 
 * @author Keisuke.K
 */
final class Swapper implements ReferenceListener {
	
	/** スワップファイルの接頭辞 */
	static final String SWAP_FILE_PREFIX = "jiemamy";
	
	/** スワップファイルの接尾辞 */
	static final String SWAP_FILE_SUFFIX = null;
	
	/** シングルトンインスタンス */
	static final Swapper INSTANCE = new Swapper();
	
	private static Logger logger = LoggerFactory.getLogger(Swapper.class);
	
	/** スワップファイルの読み書きを行うファイルチャネル */
	final FileChannel channel;
	
	/** スワップ情報を一元管理する参照TreeSet */
	final TreeSet<Reference<SwapObject<?>>> swapRefSet;
	
	/** スワップ情報参照キュー */
	final ReferenceQueue<SwapObject<?>> swapRefQueue;
	

	/**
	 * インスタンスを生成する。
	 */
	private Swapper() {
		try {
			File tmpFile = File.createTempFile(SWAP_FILE_PREFIX, SWAP_FILE_SUFFIX);
			tmpFile.deleteOnExit();
			
			channel = (new RandomAccessFile(tmpFile, "rw")).getChannel();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		swapRefSet = new TreeSet<Reference<SwapObject<?>>>(new SetComparator());
		swapRefQueue = new ReferenceQueue<SwapObject<?>>();
		
		// 参照キュー監視スレッドの生成
		ReferenceQueueMonitor<SwapObject<?>> monitor = new ReferenceQueueMonitor<SwapObject<?>>(swapRefQueue);
		monitor.addReferenceListener(this);
		
		Thread t = new Thread(monitor, "SwapObjectReferenceQueueMonitor");
		t.setDaemon(true);
		t.start();
	}
	
	/**
	 * スワップ情報参照Setをクリーニングし、スワップファイルのサイズを調整する。
	 * 
	 * <p>
	 * 参照の切れたスワップ情報を持ち続ける必要はない上、参照が切れているのでスワップファイルに情報を持つ必要がなくなるため。
	 * </p>
	 */
	public void referenceModified(ReferenceEvent event) {
		synchronized (swapRefSet) {
			// 参照TreeSetのクリーニング
			swapRefSet.remove(event.getSource());
			
			// スワップファイルのサイズ調整
			if (swapRefSet.size() > 0) {
				SwapObject<?> swapObj = swapRefSet.last().get();
				if (swapObj != null) {
					synchronized (channel) {
						try {
							channel.truncate(swapObj.position + swapObj.length);
						} catch (IOException e) {
							logger.error(LogMarker.BOUNDARY, "Error truncating swap file channel.", e);
						}
					}
				}
			} else {
				synchronized (channel) {
					try {
						channel.truncate(0L);
					} catch (IOException e) {
						logger.error(LogMarker.BOUNDARY, "Error truncating swap file channel.", e);
					}
				}
			}
		}
	}
	
	/**
	 * 引数{@code swapObj}に指定されたスワップ情報を元に RealObject をデシリアライズする。
	 * 
	 * @param <T> 取得するスワップ済み RealObject のクラス
	 * @param swapObj スワップ位置情報を保持している{@link SwapObject }インスタンス
	 * @return スワップ済み RealObject
	 * @throws SwapException スワップの復元に失敗した場合
	 */
	@SuppressWarnings("unchecked")
	<T extends Serializable>T deserialize(SwapObject<T> swapObj) throws SwapException {
		if (swapRefSet.contains(new WeakReference<SwapObject<T>>(swapObj)) == false) {
			// 管理されているSwapObjectではない
			throw new SwapException("Unknown swap info.");
		}
		
		ByteBuffer buffer = ByteBuffer.allocate(swapObj.length);
		
		// スワップファイルからの読込
		int length = -1;
		
		try {
			synchronized (channel) {
				length = channel.read(buffer, swapObj.position);
			}
		} catch (IOException e) {
			throw new SwapException(e);
		}
		
		if (length != swapObj.length) {
			throw new SwapException("Loaded size is not matched.");
		}
		
		buffer.flip();
		
		// 対象オブジェクトのデシリアライズ
		T obj = null;
		ObjectInputStream ois = null;
		byte[] buf;
		
		if (buffer.hasArray()) {
			buf = buffer.array();
		} else {
			buf = new byte[buffer.limit()];
			buffer.get(buf);
		}
		
		try {
			ois = new ObjectInputStream(new ByteArrayInputStream(buf));
			obj = (T) ois.readObject();
		} catch (IOException e) {
			throw new SwapException(e);
		} catch (ClassNotFoundException e) {
			throw new SwapException(e);
		} catch (ClassCastException e) {
			throw new SwapException(e);
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
			} catch (IOException e) {
				throw new SwapException(e);
			}
		}
		
		return obj;
	}
	
	/**
	 * 引数{@code obj}に指定された RealObject をスワップファイルへシリアライズする。
	 * 
	 * <p>
	 * {@code Swapper#serialize(SwapObject, Serializable) }との違いは、<br>
	 * 対象の RealObject のシリアライズが初回ではなく、更新を意味することである。
	 * </p>
	 * 
	 * @param <T> スワップ対象となる RealObject のクラス
	 * @param swapObj スワップ位置情報を代入させる{@link SwapObject }インスタンス
	 * @param obj スワップ対象となる RealObject
	 * @throws SwapException スワップのに失敗した場合
	 */
	<T extends Serializable>void reserialize(SwapObject<T> swapObj, T obj) throws SwapException {
		swapRefSet.remove(new WeakReference<SwapObject<?>>(swapObj));
		serialize(swapObj, obj);
	}
	
	/**
	 * 引数{@code obj}に指定された RealObject をスワップファイルへシリアライズする。
	 * 
	 * <p>
	 * RealObject がシリアライズされた位置、およびバイト長は引数{@code swapObj }の各フィールドに代入される。
	 * </p>
	 * 
	 * @param <T> スワップ対象となるオブジェクトのクラス
	 * @param swapObj スワップ位置情報を代入させる{@link SwapObject }インスタンス
	 * @param obj スワップ対象となる RealObject
	 * @throws SwapException スワップのに失敗した場合
	 */
	<T extends Serializable>void serialize(SwapObject<T> swapObj, T obj) throws SwapException {
		// オブジェクトのシリアライズを行う
		ByteBuffer buffer = serialize(obj);
		
		// オブジェクトをスワップする位置を特定する
		synchronized (swapRefSet) {
			long pos = 0L;
			for (Reference<SwapObject<?>> ref : swapRefSet) {
				SwapObject<? extends Serializable> tmp = ref.get();
				
				if (tmp == null) {
					// [SER-5] nullオブジェクトが残っている可能性がある。
					continue;
				}
				
				if (tmp.position - pos >= buffer.limit()) {
					// 位置決定
					break;
				}
				
				pos = tmp.position + tmp.length;
			}
			
			swapObj.position = pos;
			swapObj.length = buffer.limit();
			
			Reference<SwapObject<?>> ref = new WeakReference<SwapObject<?>>(swapObj, swapRefQueue);
			swapRefSet.add(ref);
		}
		
		// スワップ
		swap(buffer, swapObj.position);
	}
	
	/**
	 * オブジェクトをシリアライズして、その結果をByteBufferで取得する。
	 * 
	 * @param obj シリアライズするオブジェクト
	 * @return シリアライズ結果のバイト値を保持するByteBuffer
	 * @throws SwapException スワップに失敗した場合
	 */
	private ByteBuffer serialize(Serializable obj) throws SwapException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
		} catch (IOException e) {
			throw new SwapException(e);
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
			} catch (IOException e) {
				throw new SwapException(e);
			}
		}
		
		return ByteBuffer.wrap(baos.toByteArray());
	}
	
	/**
	 * シリアライズしたオブジェクトをスワップする。

	 * @param buffer シリアライズ結果
	 * @param position スワップ位置
	 * @throws SwapException スワップに失敗した場合
	 */
	private void swap(ByteBuffer buffer, long position) throws SwapException {
		synchronized (channel) {
			try {
				channel.write(buffer, position);
			} catch (IOException e) {
				throw new SwapException(e);
			}
		}
	}
	

	/**
	 * {@code swapReferenceSet }のための比較クラス。
	 * 
	 * <p>
	 * {@link Swapper }内の{@code swapReferenceSet }は、スワップ位置の昇順に並べる必要があるため。
	 * </p>
	 */
	private static class SetComparator implements Comparator<Reference<SwapObject<?>>> {
		
		public int compare(Reference<SwapObject<?>> o1, Reference<SwapObject<?>> o2) {
			SwapObject<?> s1 = o1.get();
			SwapObject<?> s2 = o2.get();
			
			if (s1 == null && s2 == null) {
				return 0;
			} else if (s1 == null) {
				return 1;
			} else if (s2 == null) {
				return -1;
			}
			
			return (int) (s1.position - s2.position);
		}
		
	}
	
}
