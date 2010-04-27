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
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Properties;

/**
 * リソース用のユーティリティクラス。
 * 
 * @author j5ik2o
 * 
 */
public class ResourceUtil {
	
	/**
	 * パスを変換する。
	 * 
	 * @param path パス
	 * @param clazz クラス
	 * @return 変換された結果
	 */
	public static String convertPath(String path, Class<?> clazz) {
		if (isExist(path)) {
			return path;
		}
		String prefix = clazz.getName().replace('.', '/').replaceFirst("/[^/]+$", "");
		String extendedPath = prefix + "/" + path;
		if (ResourceUtil.getResourceNoException(extendedPath) != null) {
			return extendedPath;
		}
		return path;
	}
	
	/**
	 * クラスファイルが置かれているルートディレクトリを取得する。
	 * 
	 * @param clazz クラス
	 * @return ルートディレクトリ
	 * @throws ResourceNotFoundException リソースが見つからなかった場合
	 * @throws IOException 入出力が失敗した場合
	 * @see #getBuildDir(String)
	 */
	public static File getBuildDir(Class<?> clazz) throws ResourceNotFoundException, IOException {
		return getBuildDir(getResourcePath(clazz));
	}
	
	/**
	 * クラスファイルが置かれているルートディレクトリを取得する。
	 * 
	 * @param path パス
	 * @return ルートディレクトリ
	 * @throws ResourceNotFoundException リソースが見つからなかった場合
	 * @throws IOException 入出力が失敗した場合
	 */
	public static File getBuildDir(String path) throws ResourceNotFoundException, IOException {
		File dir = null;
		URL url = getResource(path);
		if ("file".equals(url.getProtocol())) {
			int num = path.split("/").length;
			dir = new File(getFileName(url));
			for (int i = 0; i < num; ++i, dir = dir.getParentFile()) {
				// nop
			}
		} else {
			dir = new File(JarFileUtil.toJarFilePath(url));
		}
		return dir;
	}
	
	/**
	 * クラスファイルが置かれているルートディレクトリを取得する。
	 * 
	 * @param clazz クラス
	 * @return ルートディレクトリ
	 * @see #getBuildDir(String)
	 */
	public static File getBuildDirNoException(Class<?> clazz) {
		try {
			File buildDir = ResourceUtil.getBuildDir(clazz);
			return buildDir;
		} catch (ResourceNotFoundException ignore) {
			return null;
		} catch (IOException ignore) {
			return null;
		}
	}
	
	/**
	 * クラスローダを取得する。
	 * 
	 * @return クラスローダ
	 */
	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
	/**
	 * 拡張子を取得する。
	 * 
	 * @param path パス
	 * @return 拡張子
	 */
	public static String getExtension(String path) {
		int extPos = path.lastIndexOf(".");
		if (extPos >= 0) {
			return path.substring(extPos + 1);
		}
		return null;
	}
	
	/**
	 * ファイルを取得する。
	 * 
	 * @param url URL
	 * @return ファイル
	 * @throws UnsupportedEncodingException 文字のエンコーディングがサポートされていない場合
	 */
	public static File getFile(URL url) throws UnsupportedEncodingException {
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
	 * @throws UnsupportedEncodingException 文字のエンコーディングがサポートされていない場合
	 */
	public static String getFileName(URL url) throws UnsupportedEncodingException {
		String s = url.getFile();
		return URLUtil.decode(s, "UTF8");
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
	 * <p>
	 * リソースが見つからない場合は<code>null</code>。
	 * </p>
	 * 
	 * @param clazz クラス
	 * @return ファイル
	 * @see #getResourceAsFileNoException(String)
	 */
	public static File getResourceAsFileNoException(Class<?> clazz) {
		return getResourceAsFileNoException(getResourcePath(clazz));
	}
	
	/**
	 * リソースをファイルで取得する。
	 * <p>
	 * リソースが見つからない場合は<code>null</code>。
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
		try {
			return getFile(url);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
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
		return URLUtil.openStream(url);
	}
	
	/**
	 * リソースをストリームで取得する。
	 * <p>
	 * リソースが見つからなかった場合は<code>null</code>。
	 * </p>
	 * 
	 * @param path パス
	 * @return ストリーム
	 * @see #getResourceAsStreamNoException(String, String)
	 */
	public static InputStream getResourceAsStreamNoException(String path) {
		return getResourceAsStreamNoException(path, null);
	}
	
	/**
	 * リソースをストリームとして取得する。
	 * <p>
	 * リソースが見つからなかった場合は<code>null</code>。
	 * </p>
	 * 
	 * @param path パス
	 * @param extension 拡張子
	 * @return ストリーム
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
	 * <p>
	 * 見つからなかった場合は<code>null</code>を返します。
	 * </p>
	 * 
	 * @param path パス
	 * @return リソース
	 * @see #getResourceNoException(String, String)
	 */
	public static URL getResourceNoException(String path) {
		return getResourceNoException(path, null);
	}
	
	/**
	 * リソースを取得する。
	 * <p>
	 * 見つからなかった場合は<code>null</code>。
	 * </p>
	 * 
	 * @param path パス
	 * @param extension 拡張子
	 * @return リソース
	 * @see #getResourceNoException(String, String, ClassLoader)
	 */
	public static URL getResourceNoException(String path, String extension) {
		return getResourceNoException(path, extension, Thread.currentThread().getContextClassLoader());
	}
	
	/**
	 * リソースを取得する。
	 * <p>
	 * 見つからなかった場合は<code>null</code>。
	 * </p>
	 * 
	 * @param path パス
	 * @param extension 拡張子
	 * @param loader クラスローダ
	 * @return リソース
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
	 */
	public static String getResourcePath(Class<?> clazz) {
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
	 * 拡張子を除去する。
	 * 
	 * @param path パス
	 * @return 取り除いた後の結果
	 */
	public static String removeExtension(String path) {
		int extPos = path.lastIndexOf(".");
		if (extPos >= 0) {
			return path.substring(0, extPos);
		}
		return path;
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
		return URLUtil.decode(s, "UTF8");
	}
	
	private ResourceUtil() {
	}
	
}
