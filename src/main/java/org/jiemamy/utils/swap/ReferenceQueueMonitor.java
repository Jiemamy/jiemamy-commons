/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2008/11/15
 *
 * This file is part of Jiemamy.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.jiemamy.utils.swap;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 参照キュー監視スレッド。
 * 
 * <p>
 * 指定された参照キューを監視し、到達可能性が変更された際に{@link ReferenceListener }を実装した<br>
 * クラスに対し通知を行う。
 * </p>
 * 
 * @param <T> 参照キューに指定する型
 * @author Keisuke.K
 */
final class ReferenceQueueMonitor<T> implements Runnable {
	
	private static Logger logger = LoggerFactory.getLogger(ReferenceQueueMonitor.class);
	
	/** 参照キュー */
	ReferenceQueue<T> queue;
	
	/** 参照リスナリスト */
	List<ReferenceListener> listeners;
	
	/** スレッド実行フラグ */
	boolean running;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param queue 監視する参照キュー
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public ReferenceQueueMonitor(ReferenceQueue<T> queue) {
		Validate.notNull(queue);
		this.queue = queue;
		this.listeners = new ArrayList<ReferenceListener>();
		this.running = true;
	}
	
	public void run() {
		while (running) {
			try {
				Reference<? extends T> ref = queue.remove();
				ReferenceEvent event = new ReferenceEvent(ref);
				
				for (ReferenceListener listener : listeners) {
					listener.referenceModified(event);
				}
			} catch (InterruptedException e) {
				// スレッド停止
				logger.info("Shutting down because InterruptedException thrown.", e);
				running = false;
			}
		}
	}
	
	/**
	 * 参照リスナを追加する。
	 * 
	 * @param listener 参照リスナ
	 */
	void addReferenceListener(ReferenceListener listener) {
		listeners.add(listener);
	}
	
}
