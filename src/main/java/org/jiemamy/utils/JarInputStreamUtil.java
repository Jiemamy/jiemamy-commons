/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.jiemamy.exception.JiemamyError;

/**
 * {@link JarInputStream}用のユーティリティクラス。
 * 
 * @author j5ik2o
 */
public class JarInputStreamUtil {
	
	/**
	 * {@link JarInputStream}を作成する。
	 * 
	 * @param is {@link InputStream}
	 * @return {@link JarInputStream}
	 * @throws IOException 入出力に失敗した場合
	 * @see JarInputStream#JarInputStream(InputStream)
	 */
	public static JarInputStream create(final InputStream is) throws IOException {
		return new JarInputStream(is);
	}
	
	/**
	 * {@link JarInputStream#getNextJarEntry()}の例外処理をラップするメソッド。
	 * 
	 * @param is {@link JarInputStream}
	 * @return {@link JarEntry}
	 * @throws IOException 入出力に失敗した場合
	 * @see JarInputStream#getNextJarEntry()
	 */
	public static JarEntry getNextJarEntry(final JarInputStream is) throws IOException {
		return is.getNextJarEntry();
	}
	
	private JarInputStreamUtil() {
	}
}
