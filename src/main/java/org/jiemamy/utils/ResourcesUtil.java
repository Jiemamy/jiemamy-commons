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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

import org.apache.commons.lang.StringUtils;

import org.jiemamy.utils.ClassTraversal.ClassHandler;
import org.jiemamy.utils.ResourceTraversal.ResourceHandler;
import org.jiemamy.utils.reflect.ClassUtil;

/**
 * ファイルシステム上やJarファイル中に展開されているリソースの集まりを扱うユーティリティクラス。
 * 
 * @author j5ik2o
 */
public final class ResourcesUtil {
	
	/** 空の{@link Resources}の配列。 */
	private static final Resources[] EMPTY_ARRAY = new Resources[0];
	
	/** URLのプロトコルをキー、{@link ResourcesFactory}を値とするマッピング。 */
	private static final Map<String, ResourcesFactory> RESOUCES_TYPE_FACTORIES =
			new HashMap<String, ResourcesFactory>();
	
	static {
		addResourcesFactory("file", new ResourcesFactory() {
			
			public Resources create(URL url, String rootPackage, String rootDir) {
				try {
					return new FileSystemResources(getBaseDir(url, rootDir), rootPackage, rootDir);
				} catch (UnsupportedEncodingException e) {
					return null;
				}
			}
		});
		addResourcesFactory("jar", new ResourcesFactory() {
			
			public Resources create(URL url, String rootPackage, String rootDir) throws IOException {
				return new JarFileResources(url, rootPackage, rootDir);
			}
		});
		addResourcesFactory("zip", new ResourcesFactory() {
			
			public Resources create(URL url, String rootPackage, String rootDir) throws IOException {
				return new JarFileResources(new JarFile(new File(ZipFileUtil.toZipFilePath(url))), rootPackage, rootDir);
			}
		});
		addResourcesFactory("code-source", new ResourcesFactory() {
			
			public Resources create(URL url, String rootPackage, String rootDir) throws IOException {
				return new JarFileResources(new URL("jar:file:" + url.getPath()), rootPackage, rootDir);
			}
		});
	}
	

	/**
	 * {@link ResourcesFactory}を追加する。
	 * 
	 * @param protocol URLのプロトコル
	 * @param factory プロトコルに対応する{@link Resources}のファクトリ
	 */
	public static void addResourcesFactory(String protocol, ResourcesFactory factory) {
		RESOUCES_TYPE_FACTORIES.put(protocol, factory);
	}
	
	/**
	 * 指定のクラスを基点とするリソースの集まりを扱う{@link Resources}を取得する。
	 * <p>
	 * このメソッドが返す{@link Resources}は、指定されたクラスをFQNで参照可能なパスをルートとします。 例えば指定されたクラスが
	 * {@code foo.Bar}で、そのクラスファイルが{@code classes/foo/Bar.class}の場合、
	 * このメソッドが返す{@link Resources}は{@code classes}ディレクトリ以下のリソースの集合を扱う。
	 * </p>
	 * 
	 * @param referenceClass 基点となるクラス
	 * @return 指定のクラスを基点とするリソースの集まりを扱う{@link Resources}
	 * @throws IOException 入出力エラーが発生した場合
	 * @throws ResourceNotFoundException リソースが見つからなかった場合
	 */
	public static Resources getResourcesType(Class<?> referenceClass) throws IOException, ResourceNotFoundException {
		URL url = ResourceUtil.getResource(toClassFile(referenceClass.getName()));
		String[] path = referenceClass.getName().split("\\.");
		String baseUrl = url.toExternalForm();
		for (int i = 0; i < path.length; ++i) {
			int pos = baseUrl.lastIndexOf('/');
			baseUrl = baseUrl.substring(0, pos);
		}
		return getResourcesType(new URL(baseUrl + '/'), null, null);
	}
	
	/**
	 * 指定のディレクトリを基点とするリソースの集まりを扱う{@link Resources}を取得する。
	 * 
	 * @param rootDir ルートディレクトリ
	 * @return 指定のディレクトリを基点とするリソースの集まりを扱う{@link Resources}
	 * @throws IOException 入出力エラーが発生した場合
	 * @throws ResourceNotFoundException リソースが見つからなかった場合
	 */
	public static Resources getResourcesType(String rootDir) throws IOException, ResourceNotFoundException {
		URL url = ResourceUtil.getResource(rootDir.endsWith("/") ? rootDir : rootDir + '/');
		return getResourcesType(url, null, rootDir);
	}
	
	//private static final Logger logger = Logger.getLogger(ResourcesUtil.class);
	
	/**
	 * 指定のルートパッケージを基点とするリソースの集まりを扱う{@link Resources}の配列を取得する。
	 * 
	 * @param rootPackage ルートパッケージ
	 * @return 指定のルートパッケージを基点とするリソースの集まりを扱う{@link Resources}の配列
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public static Resources[] getResourcesTypes(String rootPackage) throws IOException {
		if (StringUtils.isEmpty(rootPackage)) {
			return EMPTY_ARRAY;
		}
		
		String baseName = toDirectoryName(rootPackage);
		List<Resources> list = new ArrayList<Resources>();
		for (Iterator<URL> it = ClassLoaderUtil.getResources(baseName); it.hasNext();) {
			URL url = it.next();
			Resources resourcesType = getResourcesType(url, rootPackage, baseName);
			if (resourcesType != null) {
				list.add(resourcesType);
			}
		}
		if (list.isEmpty()) {
			return EMPTY_ARRAY;
		}
		return list.toArray(new Resources[list.size()]);
	}
	
	/**
	 * ファイルを表すURLからルートパッケージの上位となるベースディレクトリを取得する。
	 * 
	 * @param url ファイルを表すURL
	 * @param baseName ベース名
	 * @return ルートパッケージの上位となるベースディレクトリ
	 * @throws UnsupportedEncodingException 文字のエンコーディングがサポートされてない場合
	 */
	protected static File getBaseDir(URL url, String baseName) throws UnsupportedEncodingException {
		String path = URLDecoder.decode(url.getPath(), "UTF-8");
		File file = new File(path).getAbsoluteFile();
		String[] paths = StringUtils.split(baseName, "/");
		for (int i = 0; i < paths.length; ++i) {
			file = file.getParentFile();
		}
		return file;
	}
	
	/**
	 * URLを扱う{@link Resources}を取得する。
	 * <p>
	 * URLのプロトコルが未知の場合は{@code null}。
	 * </p>
	 * 
	 * @param url リソースのURL
	 * @param rootPackage ルートパッケージ
	 * @param rootDir ルートディレクトリ
	 * @return URLを扱う{@link Resources}
	 * @throws IOException 入出力エラーが発生した場合
	 */
	protected static Resources getResourcesType(URL url, String rootPackage, String rootDir) throws IOException {
		ResourcesFactory factory = RESOUCES_TYPE_FACTORIES.get(url.getProtocol());
		if (factory != null) {
			return factory.create(url, rootPackage, rootDir);
		}
		return null;
	}
	
	/**
	 * クラス名をクラスファイルのパス名に変換する。
	 * 
	 * @param className クラス名
	 * @return クラスファイルのパス名
	 */
	protected static String toClassFile(String className) {
		return className.replace('.', '/') + ".class";
	}
	
	/**
	 * パッケージ名をディレクトリ名に変換する。
	 * 
	 * @param packageName パッケージ名
	 * @return ディレクトリ名
	 */
	protected static String toDirectoryName(String packageName) {
		if (StringUtils.isEmpty(packageName)) {
			return null;
		}
		return packageName.replace('.', '/') + '/';
	}
	
	private ResourcesUtil() {
	}
	

	/**
	 * ファイルシステム上のリソースの集まりを扱うオブジェクト。
	 * 
	 * @author j5ik2o
	 */
	public static class FileSystemResources implements Resources {
		
		/** ベースディレクトリです。 */
		protected final File baseDir;
		
		/** ルートパッケージです。 */
		protected final String rootPackage;
		
		/** ルートディレクトリです。 */
		protected final String rootDir;
		

		/**
		 * インスタンスを構築します。
		 * 
		 * @param baseDir ベースディレクトリ
		 * @param rootPackage ルートパッケージ
		 * @param rootDir ルートディレクトリ
		 */
		public FileSystemResources(File baseDir, String rootPackage, String rootDir) {
			this.baseDir = baseDir;
			this.rootPackage = rootPackage;
			this.rootDir = rootDir;
		}
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param url ディレクトリを表すURL
		 * @param rootPackage ルートパッケージ
		 * @param rootDir ルートディレクトリ
		 * @throws UnsupportedEncodingException 文字のエンコーディングがサポートされてない場合
		 */
		public FileSystemResources(URL url, String rootPackage, String rootDir) throws UnsupportedEncodingException {
			this(new File(URLDecoder.decode(url.getPath(), "UTF-8")).getAbsoluteFile(), rootPackage, rootDir);
		}
		
		public void close() {
			// nothing to do
		}
		
		public void forEach(ClassHandler handler) throws TraversalHandlerException {
			ClassTraversal.forEach(baseDir, rootPackage, handler);
		}
		
		public void forEach(ResourceHandler handler) throws IOException, TraversalHandlerException {
			ResourceTraversal.forEach(baseDir, rootDir, handler);
		}
		
		public boolean isExistClass(String className) {
			File file = new File(baseDir, toClassFile(ClassUtil.concatName(rootPackage, className)));
			return file.exists();
		}
		
	}
	
	/**
	 * Jarファイル中のリソースの集まりを扱うオブジェクト。
	 * 
	 * @author j5ik2o
	 */
	public static class JarFileResources implements Resources {
		
		/** Jarファイルです。 */
		protected final JarFile jarFile;
		
		/** ルートパッケージです。 */
		protected final String rootPackage;
		
		/** ルートディレクトリです。 */
		protected final String rootDir;
		

		/**
		 * インスタンスを生成する。
		 * 
		 * @param jarFile Jarファイル
		 * @param rootPackage ルートパッケージ
		 * @param rootDir ルートディレクトリ
		 */
		public JarFileResources(JarFile jarFile, String rootPackage, String rootDir) {
			this.jarFile = jarFile;
			this.rootPackage = rootPackage;
			this.rootDir = rootDir;
		}
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param url Jarファイルを表すURL
		 * @param rootPackage ルートパッケージ
		 * @param rootDir ルートディレクトリ
		 * @throws IOException 入出力エラーが発生した場合
		 */
		public JarFileResources(URL url, String rootPackage, String rootDir) throws IOException {
			this(JarFileUtil.toJarFile(url), rootPackage, rootDir);
		}
		
		public void close() throws IOException {
			jarFile.close();
		}
		
		public void forEach(final ClassHandler handler) throws TraversalHandlerException {
			ClassTraversal.forEach(jarFile, new ClassHandler() {
				
				public void processClass(String packageName, String shortClassName) throws TraversalHandlerException {
					if (rootPackage == null || (packageName != null && packageName.startsWith(rootPackage))) {
						handler.processClass(packageName, shortClassName);
					}
				}
			});
		}
		
		public void forEach(final ResourceHandler handler) throws IOException, TraversalHandlerException {
			ResourceTraversal.forEach(jarFile, new ResourceHandler() {
				
				public void processResource(String path, InputStream is) throws TraversalHandlerException {
					if (rootDir == null || path.startsWith(rootDir)) {
						handler.processResource(path, is);
					}
				}
			});
		}
		
		public boolean isExistClass(String className) {
			return jarFile.getEntry(toClassFile(ClassUtil.concatName(rootPackage, className))) != null;
		}
		
	}
	
	/**
	 * リソースの集まりを表すオブジェクト。
	 * 
	 * @author j5ik2o
	 */
	public interface Resources {
		
		/**
		 * リソースの後処理を行う。
		 * @throws IOException 入出力が失敗した場合
		 */
		void close() throws IOException;
		
		/**
		 * このインスタンスが扱うリソースの中に存在するクラスを探して
		 * {@link ClassHandler#processClass(String, String) ハンドラ}をコールバックする。
		 * <p>
		 * インスタンス構築時にルートパッケージが指定されている場合は、 ルートパッケージ以下のクラスのみが対象となる。
		 * </p>
		 * 
		 * @param handler ハンドラ
		 * @throws TraversalHandlerException ハンドラの処理が失敗した場合
		 */
		void forEach(ClassHandler handler) throws TraversalHandlerException;
		
		/**
		 * このインスタンスが扱うリソースを探して
		 * {@link ResourceHandler#processResource(String, java.io.InputStream)
		 * ハンドラ}をコールバックする。
		 * <p>
		 * インスタンス構築時にルートディレクトリが指定されている場合は、 ルートディレクトリ以下のリソースのみが対象となる。
		 * </p>
		 * 
		 * @param handler ハンドラ
		 * @throws IOException 入出力が失敗した場合
		 * @throws TraversalHandlerException ハンドラの処理が失敗した場合
		 */
		void forEach(ResourceHandler handler) throws IOException, TraversalHandlerException;
		
		/**
		 * 指定されたクラス名に対応するクラスファイルがこのインスタンスが扱うリソースの中に存在すれば{@code true}を取得する。
		 * <p>
		 * インスタンス構築時にルートパッケージが指定されている場合、 指定されたクラス名はルートパッケージからの相対名として解釈する。
		 * </p>
		 * 
		 * @param className クラス名
		 * @return 指定されたクラス名に対応するクラスファイルがこのインスタンスが扱うリソースの中に存在すれば
		 *         {@code true}
		 */
		boolean isExistClass(String className);
		
	}
	
	/**
	 * {@link Resources}のインスタンスを作成するファクトリ。
	 * 
	 * @author j5ik2o
	 */
	public interface ResourcesFactory {
		
		/**
		 * {@link Resources}のインスタンスを作成して返します。
		 * 
		 * @param url リソースを表すURL
		 * @param rootPackage ルートパッケージ
		 * @param rootDir ルートディレクトリ
		 * @return URLで表されたリソースを扱う{@link Resources}
		 * @throws IOException 入出力エラーが発生した場合
		 */
		Resources create(URL url, String rootPackage, String rootDir) throws IOException;
	}
	
}
