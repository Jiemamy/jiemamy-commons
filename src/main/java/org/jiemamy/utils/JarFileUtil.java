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

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.jar.JarFile;

/**
 * {@link JarFile}を扱うユーティリティクラス。
 * 
 * @author j5ik2o
 */
public class JarFileUtil {
	
	/**
	 * URLで指定されたJarファイルを読み取るための{@code JarFile}を作成する。
	 * 
	 * @param jarUrl Jarファイルを示すURL
	 * @return 指定されたJarファイルを読み取るための{@code JarFile}
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public static JarFile toJarFile(URL jarUrl) throws IOException {
		URLConnection con = jarUrl.openConnection();
		if (con instanceof JarURLConnection) {
			return ((JarURLConnection) con).getJarFile();
		}
		return new JarFile(new File(toJarFilePath(jarUrl)));
	}
	
	/**
	 * URLで指定されたJarファイルのパスを返します。
	 * 
	 * @param jarUrl Jarファイルを示すURL
	 * @return URLで指定されたJarファイルのパス
	 * @throws IOException 入出力が失敗した場合
	 */
	public static String toJarFilePath(URL jarUrl) throws IOException {
		URL nestedUrl = new URL(jarUrl.getPath());
		String nestedUrlPath = nestedUrl.getPath();
		int pos = nestedUrlPath.lastIndexOf('!');
		String jarFilePath = nestedUrlPath.substring(0, pos);
		File jarFile = new File(URLDecoder.decode(jarFilePath, "UTF8"));
		return jarFile.getCanonicalPath();
	}
	
	private JarFileUtil() {
	}
	
}
