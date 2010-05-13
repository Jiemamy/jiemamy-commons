/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy.utils.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * {@link InputStream}用のユーティリティクラス。
 * 
 * @author j5ik2o
 * 
 */
public class InputStreamUtil {
	
	/** バッファサイズ */
	private static final int BUFF_SIZE = 8192;
	

	/**
	 * {@link InputStream#available()}の例外処理をラップしたメソッド。
	 * 
	 * @param is {@link InputStream}
	 * @return 可能なサイズ
	 * @throws IOException 入出力が失敗した場合
	 */
	public static int available(InputStream is) throws IOException {
		return is.available();
	}
	
	/**
	 * {@link InputStream}を閉じる。
	 * 
	 * @param is {@link InputStream}
	 * @throws IOException 入出力が失敗した場合
	 * @see InputStream#close()
	 */
	public static void close(InputStream is) throws IOException {
		if (is == null) {
			return;
		}
		is.close();
	}
	
	/**
	 * {@link InputStream}の内容を {@link OutputStream}にコピーします。
	 * 
	 * @param is {@link InputStream}
	 * @param os {@link OutputStream}
	 * @throws IOException 入出力が失敗した場合
	 */
	public static final void copy(InputStream is, OutputStream os) throws IOException {
		byte[] buf = new byte[BUFF_SIZE];
		int n = 0;
		while ((n = is.read(buf, 0, buf.length)) != -1) {
			os.write(buf, 0, n);
		}
	}
	
	/**
	 * {@link InputStream}からbyteの配列を取得する。
	 * 
	 * @param is {@link InputStream}
	 * @return byteの配列
	 * @throws IOException 入出力が失敗した場合
	 */
	public static final byte[] getBytes(InputStream is) throws IOException {
		byte[] buf = new byte[BUFF_SIZE];
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int n = 0;
			while ((n = is.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, n);
			}
			byte[] bytes = baos.toByteArray();
			return bytes;
		} finally {
			//if (is != null) {
			close(is);
			//}
		}
	}
	
	private InputStreamUtil() {
	}
}
