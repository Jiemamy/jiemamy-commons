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
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * {@link java.util.zip.ZipFile}を扱うユーティリティクラス。
 * 
 * @author j5ik2o
 */
public class ZipFileUtil {
	
	/**
	 * Zipファイルをクローズする。
	 * 
	 * @param zipFile Zipファイル
	 * @throws IOException 入出力が失敗した場合
	 */
	public static void close(ZipFile zipFile) throws IOException {
		zipFile.close();
		
	}
	
	/**
	 * 指定されたZipファイルを読み取るための{@code ZipFile}を取得する。
	 * 
	 * @param file ファイル
	 * @return 指定されたZipファイルを読み取るための{@code ZipFile}
	 * @throws IOException 入出力が失敗した場合
	 */
	public static ZipFile create(File file) throws IOException {
		return new ZipFile(file);
	}
	
	/**
	 * 指定されたZipファイルを読み取るための{@code ZipFile}を取得する。
	 * 
	 * @param file ファイルパス
	 * @return 指定されたZipファイルを読み取るための{@code ZipFile}
	 * @throws IOException 入出力が失敗した場合
	 */
	public static ZipFile create(String file) throws IOException {
		return new ZipFile(file);
	}
	
	/**
	 * 指定されたZipファイルエントリの内容を読み込むための入力ストリームを取得する。
	 * 
	 * @param file Zipファイル
	 * @param entry Zipファイルエントリ
	 * @return 指定されたZipファイルエントリの内容を読み込むための入力ストリーム
	 * @throws IOException 入出力が失敗した場合
	 */
	public static InputStream getInputStream(ZipFile file, ZipEntry entry) throws IOException {
		return file.getInputStream(entry);
	}
	
	/**
	 * URLで指定されたZipファイルを読み取るための{@code ZipFile}を取得する。
	 * 
	 * @param zipUrl Zipファイルを示すURL
	 * @return 指定されたZipファイルを読み取るための{@code ZipFile}
	 * @throws IOException 入出力が失敗した場合
	 */
	public static ZipFile toZipFile(URL zipUrl) throws IOException {
		return create(new File(toZipFilePath(zipUrl)));
	}
	
	/**
	 * URLで指定されたZipファイルのパスを取得する。
	 * 
	 * @param zipUrl Zipファイルを示すURL
	 * @return URLで指定されたZipファイルのパス
	 * @throws IOException 入出力が失敗した場合
	 */
	public static String toZipFilePath(URL zipUrl) throws IOException {
		String urlString = zipUrl.getPath();
		int pos = urlString.lastIndexOf('!');
		String zipFilePath = urlString.substring(0, pos);
		File zipFile = new File(URLUtil.decode(zipFilePath, "UTF8"));
		return FileUtil.getCanonicalPath(zipFile);
	}
	
	private ZipFileUtil() {
	}
	
}
