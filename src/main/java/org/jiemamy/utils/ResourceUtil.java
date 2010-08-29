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
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyError;

/**
 * リソース用のユーティリティクラス。
 * 
 * @author j5ik2o
 */
public final class ResourceUtil {
	
	/**
	 * 現在のスレッドのコンテキストクラスローダを取得する。
	 * 
	 * @return コンテキストクラスローダ。未定義の場合は{@code null}
	 */
	public static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
	/**
	 * ファイルを取得する。
	 * 
	 * @param url URL
	 * @return ファイルオブジェクト。URLで示したファイルが存在しなかった場合{@code null}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static File getFile(URL url) {
		Validate.notNull(url);
		File file = new File(getFileName(url));
		if (file.exists()) {
			return file;
		}
		return null;
	}
	
	/**
	 * ファイル名を取得する。
	 * 
	 * @param url URL
	 * @return ファイル名
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static String getFileName(URL url) {
		Validate.notNull(url);
		String s = url.getFile();
		try {
			return URLDecoder.decode(s, "UTF8");
		} catch (UnsupportedEncodingException e) {
			throw new JiemamyError("This JVM does not support UTF-8 encoding!!", e);
		}
	}
	
	/**
	 * プロパティファイルを取得する。
	 * 
	 * @param path パス
	 * @return プロパティファイル
	 * @throws IOException 入出力が失敗した場合
	 * @throws ResourceNotFoundException リソースが見つからなかった場合
	 */
	public static Properties getProperties(String path) throws ResourceNotFoundException, IOException {
		Properties props = new Properties();
		InputStream is = getResourceAsStream(path);
		
		props.load(is);
		return props;
		
	}
	
	/**
	 * リソースを取得する。
	 * 
	 * @param path パス
	 * @return リソース
	 * @throws ResourceNotFoundException リソースが見つからなかった場合
	 * @see #getResource(String, String)
	 */
	public static URL getResource(String path) throws ResourceNotFoundException {
		return getResource(path, null);
	}
	
	/**
	 * リソースを取得する。
	 * 
	 * @param path パス
	 * @param extension 拡張子
	 * @return リソース
	 * @throws ResourceNotFoundException リソースが見つからなかった場合
	 */
	public static URL getResource(String path, String extension) throws ResourceNotFoundException {
		URL url = getResourceNoException(path, extension);
		if (url != null) {
			return url;
		}
		throw new ResourceNotFoundException(getResourcePath(path, extension));
	}
	
	/**
	 * リソースをファイルで取得する。
	 * 
	 * @param path パス
	 * @return ファイル
	 * @throws ResourceNotFoundException リソースが見つからなかった場合
	 * @throws UnsupportedEncodingException 文字のエンコーディングがサポートされていない場合
	 * @see #getResourceAsFile(String, String)
	 */
	public static File getResourceAsFile(String path) throws ResourceNotFoundException, UnsupportedEncodingException {
		return getResourceAsFile(path, null);
	}
	
	/**
	 * リソースをファイルで取得する。
	 * 
	 * @param path パス
	 * @param extension 拡張子
	 * @return ファイル
	 * @throws ResourceNotFoundException リソースが見つからなかった場合
	 * @throws UnsupportedEncodingException 文字のエンコーディングがサポートされていない場合
	 * @see #getFile(URL)
	 */
	public static File getResourceAsFile(String path, String extension) throws ResourceNotFoundException,
			UnsupportedEncodingException {
		return getFile(getResource(path, extension));
	}
	
	/**
	 * リソースをファイルで取得する。
	 * 
	 * @param clazz クラス
	 * @return ファイル. リソースが見つからない場合は{@code null}。
	 * @see #getResourceAsFileNoException(String)
	 */
	public static File getResourceAsFileNoException(Class<?> clazz) {
		return getResourceAsFileNoException(getResourcePath(clazz));
	}
	
	/**
	 * リソースをファイルで取得する。
	 * <p>
	 * リソースが見つからない場合は{@code null}。
	 * </p>
	 * 
	 * @param path パス
	 * @return ファイル
	 * @see #getResourceNoException(String)
	 */
	public static File getResourceAsFileNoException(String path) {
		URL url = getResourceNoException(path);
		if (url == null) {
			return null;
		}
		return getFile(url);
	}
	
	/**
	 * リソースをストリームで取得する。
	 * 
	 * @param path パス
	 * @return ストリーム
	 * @throws ResourceNotFoundException リソースが見つからなかった場合
	 * @throws IOException 入出力が失敗した場合
	 * @see #getResourceAsStream(String, String)
	 */
	public static InputStream getResourceAsStream(String path) throws ResourceNotFoundException, IOException {
		return getResourceAsStream(path, null);
	}
	
	/**
	 * リソースをストリームで取得する。
	 * 
	 * @param path パス
	 * @param extension 拡張子
	 * @return ストリーム
	 * @throws ResourceNotFoundException リソースが見つからなかった場合
	 * @throws IOException 入出力が失敗した場合
	 * @see #getResource(String, String)
	 */
	public static InputStream getResourceAsStream(String path, String extension) throws ResourceNotFoundException,
			IOException {
		URL url = getResource(path, extension);
		return url.openStream();
	}
	
	/**
	 * リソースをストリームで取得する。
	 * 
	 * @param path パス
	 * @return ストリーム。リソースが見つからなかった場合は{@code null}。
	 * @see #getResourceAsStreamNoException(String, String)
	 */
	public static InputStream getResourceAsStreamNoException(String path) {
		return getResourceAsStreamNoException(path, null);
	}
	
	/**
	 * リソースをストリームとして取得する。
	 * 
	 * @param path パス
	 * @param extension 拡張子
	 * @return ストリーム。リソースが見つからなかった場合は{@code null}。
	 * @see #getResourceNoException(String, String)
	 */
	public static InputStream getResourceAsStreamNoException(String path, String extension) {
		URL url = getResourceNoException(path, extension);
		if (url == null) {
			return null;
		}
		try {
			return url.openStream();
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * リソースを取得する。
	 * 
	 * @param path パス
	 * @return リソース。見つからなかった場合は{@code null}
	 * @see #getResourceNoException(String, String)
	 */
	public static URL getResourceNoException(String path) {
		return getResourceNoException(path, null);
	}
	
	/**
	 * リソースを取得する。
	 * 
	 * @param path パス
	 * @param extension 拡張子
	 * @return リソース。見つからなかった場合は{@code null}
	 * @see #getResourceNoException(String, String, ClassLoader)
	 */
	public static URL getResourceNoException(String path, String extension) {
		return getResourceNoException(path, extension, Thread.currentThread().getContextClassLoader());
	}
	
	/**
	 * リソースを取得する。
	 * 
	 * @param path パス
	 * @param extension 拡張子
	 * @param loader クラスローダ
	 * @return リソース。見つからなかった場合は{@code null}
	 * @see #getResourcePath(String, String)
	 */
	public static URL getResourceNoException(String path, String extension, ClassLoader loader) {
		if (path == null || loader == null) {
			return null;
		}
		path = getResourcePath(path, extension);
		return loader.getResource(path);
	}
	
	/**
	 * リソースパスを取得する。
	 * 
	 * @param clazz クラス
	 * @return リソースパス
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static String getResourcePath(Class<?> clazz) {
		Validate.notNull(clazz);
		return clazz.getName().replace('.', '/') + ".class";
	}
	
	/**
	 * リソースパスを取得する。
	 * 
	 * @param path パス
	 * @param extension 拡張子
	 * @return リソースパス
	 */
	public static String getResourcePath(String path, String extension) {
		if (extension == null) {
			return path;
		}
		extension = "." + extension;
		if (path.endsWith(extension)) {
			return path;
		}
		return path.replace('.', '/') + extension;
	}
	
	/**
	 * リソースの存在有無を取得する。
	 * 
	 * @param path パス
	 * @return リソースが存在するかどうか
	 * @see #getResourceNoException(String)
	 */
	public static boolean isExist(String path) {
		return getResourceNoException(path) != null;
	}
	
	/**
	 * 外部形式に変換する。
	 * 
	 * @param url URL
	 * @return 外部形式
	 * @throws UnsupportedEncodingException 文字のエンコーディングがサポートされていない場合
	 */
	public static String toExternalForm(URL url) throws UnsupportedEncodingException {
		String s = url.toExternalForm();
		return URLDecoder.decode(s, "UTF8");
	}
	
	private ResourceUtil() {
	}
	
}
