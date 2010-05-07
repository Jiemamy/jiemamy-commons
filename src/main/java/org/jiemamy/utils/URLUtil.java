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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link URL}を扱うユーティリティ・クラス。
 * 
 * @author j5ik2o
 */
public class URLUtil {
	
	/** プロトコルを正規化するためのマップ */
	protected static final Map<String, String> CANONICAL_PROTOCOLS = new HashMap<String, String>();
	static {
		CANONICAL_PROTOCOLS.put("wsjar", "jar"); // WebSphereがJarファイルのために使用する固有のプロトコル
	}
	

	/**
	 * {@code String}表現から{@code URL}オブジェクトを作成します。
	 * 
	 * @param spec {@code URL}として構文解析される{@code String}
	 * @return {@code URL}
	 * @throws MalformedURLException 無効なURLが発生した場合
	 */
	public static URL create(String spec) throws MalformedURLException {
		return new URL(spec);
	}
	
	/**
	 * 指定されたコンテキスト内の指定された仕様で構文解析することによって、{@code URL}を生成する。
	 * 
	 * @param context 仕様を構文解析するコンテキスト
	 * @param spec {@code URL}として構文解析される{@code String}
	 * @return {@code URL}
	 * @throws MalformedURLException 文字列に指定されたプロトコルが未知である場合
	 */
	public static URL create(URL context, String spec) throws MalformedURLException {
		return new URL(context, spec);
	}
	
	/**
	 * 特別な符号化方式を使用して{@code application/x-www-form-urlencoded}文字列をデコードする。
	 * 
	 * @param s デコード対象の{@code String}
	 * @param enc サポートされる文字エンコーディングの名前
	 * @return 新しくデコードされた String
	 * @throws UnsupportedEncodingException 文字のエンコーディングがサポートされていない場合
	 */
	public static String decode(String s, String enc) throws UnsupportedEncodingException {
		return URLDecoder.decode(s, enc);
	}
	
//	/**
//	 * <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4167874">このバグ</a>に対する対応です。
//	 */
//	public static void disableURLCaches() {
//		BeanDesc bd = BeanDescFactory.getBeanDesc(URLConnection.class);
//		FieldUtil.set(bd.getField("defaultUseCaches"), null, Boolean.FALSE);
//	}
	
	/**
	 * 特定の符号化方式を使用して文字列を{@code application/x-www-form-urlencoded}形式に変換する。
	 * 
	 * @param s 変換対象の String
	 * @param enc サポートされる文字エンコーディングの名前
	 * @return 変換後の{@code String}
	 * @throws UnsupportedEncodingException 文字のエンコーディングがサポートされてない場合
	 */
	public static String encode(String s, String enc) throws UnsupportedEncodingException {
		return URLEncoder.encode(s, enc);
	}
	
	/**
	 * URLが参照するリモートオブジェクトへの接続を表す{@link URLConnection}オブジェクトを取得する。
	 * 
	 * @param url URL
	 * @return URLへの{@link URLConnection}オブジェクト
	 * @throws IOException 入出力が失敗した場合
	 */
	public static URLConnection openConnection(URL url) throws IOException {
		URLConnection connection = url.openConnection();
		connection.setUseCaches(false);
		return connection;
	}
	
	/**
	 * URLをオープンして{@link InputStream}を取得する。
	 * 
	 * @param url URL
	 * @return URLが表すリソースを読み込むための{@link InputStream}
	 * @throws IOException 入出力が失敗した場合
	 */
	public static InputStream openStream(URL url) throws IOException {
		URLConnection connection = url.openConnection();
		connection.setUseCaches(false);
		return connection.getInputStream();
	}
	
	/**
	 * 正規化されたプロトコルを取得する。
	 * 
	 * @param protocol プロトコル
	 * @return 正規化されたプロトコル
	 */
	public static String toCanonicalProtocol(String protocol) {
		String canonicalProtocol = CANONICAL_PROTOCOLS.get(protocol);
		if (canonicalProtocol != null) {
			return canonicalProtocol;
		}
		return protocol;
	}
	
	/**
	 * URLが示すJarファイルの{@link File}オブジェクトを取得する。
	 * 
	 * @param fileUrl JarファイルのURL
	 * @return Jarファイルの{@link File}
	 * @throws UnsupportedEncodingException 文字のエンコーディングがサポートされてない場合
	 */
	public static File toFile(URL fileUrl) throws UnsupportedEncodingException {
		String path = URLDecoder.decode(fileUrl.getPath(), "UTF-8");
		return new File(path).getAbsoluteFile();
	}
	
	private URLUtil() {
	}
	
}
