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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.jiemamy.exception.JiemamyError;

/**
 * {@link JarFile}を扱うユーティリティクラス。
 * 
 * @author j5ik2o
 */
public class JarFileUtil {
	
	/**
	 * Jarファイルを閉じる。
	 * 
	 * @param jarFile Jarファイル
	 * @throws IOException 入出力が失敗した場合
	 */
	public static void close(final JarFile jarFile) throws IOException {
		jarFile.close();
	}
	
	/**
	 * 指定されたJarファイルを読み取るための<code>JarFile</code>を作成する。
	 * 
	 * @param file ファイル
	 * @return 指定されたJarファイルを読み取るための<code>JarFile</code>
	 * @throws IOException 入出力が失敗した場合
	 */
	public static JarFile create(final File file) throws IOException {
		return new JarFile(file);
	}
	
	/**
	 * 指定されたJarファイルを読み取るための<code>JarFile</code>を作成する。
	 * 
	 * @param file ファイルパス
	 * @return 指定されたJarファイルを読み取るための<code>JarFile</code>
	 * @throws IOException 入出力が失敗した場合
	 */
	public static JarFile create(final String file) throws IOException {
		return new JarFile(file);
	}
	
	/**
	 * 指定されたJarファイルエントリの内容を読み込むための入力ストリームを取得する。
	 * 
	 * @param file Jarファイル
	 * @param entry Jarファイルエントリ
	 * @return 指定されたJarファイルエントリの内容を読み込むための入力ストリーム
	 * @throws IOException 入出力が失敗した場合
	 */
	public static InputStream getInputStream(final JarFile file, final ZipEntry entry) throws IOException {
		return file.getInputStream(entry);
	}
	
	/**
	 * URLで指定されたJarファイルを読み取るための<code>JarFile</code>を作成する。
	 * 
	 * @param jarUrl Jarファイルを示すURL
	 * @return 指定されたJarファイルを読み取るための<code>JarFile</code>
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public static JarFile toJarFile(final URL jarUrl) throws IOException {
		final URLConnection con = URLUtil.openConnection(jarUrl);
		if (con instanceof JarURLConnection) {
			return JarURLConnectionUtil.getJarFile((JarURLConnection) con);
		}
		return create(new File(toJarFilePath(jarUrl)));
	}
	
	/**
	 * URLで指定されたJarファイルのパスを返します。
	 * 
	 * @param jarUrl Jarファイルを示すURL
	 * @return URLで指定されたJarファイルのパス
	 * @throws IOException 入出力が失敗した場合
	 */
	public static String toJarFilePath(final URL jarUrl) throws IOException {
		final URL nestedUrl = URLUtil.create(jarUrl.getPath());
		final String nestedUrlPath = nestedUrl.getPath();
		final int pos = nestedUrlPath.lastIndexOf('!');
		final String jarFilePath = nestedUrlPath.substring(0, pos);
		final File jarFile = new File(URLUtil.decode(jarFilePath, "UTF8"));
		return FileUtil.getCanonicalPath(jarFile);
	}
	
	private JarFileUtil() {
		throw new JiemamyError("不到達ポイント");
	}
	
}
