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
package org.jiemamy.utils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * {@link OutputStream}用のユーティリティクラス。
 * 
 * @author j5ik2o
 */
public final class OutputStreamUtil {
	
	/**
	 * {@link OutputStream}を閉じる。
	 * 
	 * @param out {@link OutputStream}
	 * @throws IOException closeが失敗した場合
	 */
	public static void close(OutputStream out) throws IOException {
		if (out == null) {
			return;
		}
		out.close();
	}
	
	/**
	 * {@link OutputStream}をフラッシュする。
	 * 
	 * @param out {@link OutputStream}
	 * @throws IOException closeが失敗した場合
	 */
	public static void flush(OutputStream out) throws IOException {
		if (out == null) {
			return;
		}
		out.flush();
	}
	
	private OutputStreamUtil() {
	}
}
