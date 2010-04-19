/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2008/07/26
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.List;
import java.util.jar.JarFile;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.exception.DriverNotFoundException;
import org.jiemamy.exception.JiemamyError;
import org.jiemamy.utils.ResourceTraversal.ResourceHandler;

/**
 * JDBCドライバ関係のユーティリティクラス。
 * 
 * @author daisuke
 */
public final class DriverUtil {
	
	private static Logger logger = LoggerFactory.getLogger(DriverUtil.class);
	

	/**
	 * JARファイルに含まれるDriverクラスのリストを取得する。
	 * 
	 * @param paths JARファイルを示すURLの配列
	 * @return Driverクラスのリスト
	 * @throws IOException 入出力エラーが発生した場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws IllegalArgumentException URIとして不適切なURLが引数に含まれていた場合
	 * @throws FileNotFoundException ファイルが見つからなかった場合
	 */
	public static List<Class<? extends Driver>> getDriverClasses(URL[] paths) throws IOException {
		Validate.notNull(paths);
		
		URLClassLoader classLoader = new URLClassLoader(paths);
		
		List<Class<? extends Driver>> driverList = CollectionsUtil.newArrayList();
		
		for (URL path : paths) {
			try {
				File file = new File(path.toURI());
				if (file.exists() == false) {
					throw new FileNotFoundException(file.getAbsolutePath());
				}
				JarFile jarFile = new JarFile(file); //JarFileUtil.toJarFile(path);
				ResourceTraversal.forEach(jarFile, new GetDriverClassesFromJarHandler(driverList, classLoader));
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException(path.toString(), e);
			} catch (TraversalHandlerException e) {
				logger.error(LogMarker.DETAIL, "", e);
			}
		}
		return driverList;
	}
	
	/**
	 * JARファイルからドライバのインスタンスを取得する。
	 * 
	 * @param paths JARファイルを示すURLの配列
	 * @param fqcn ドライバの完全修飾クラス名
	 * @return ドライバのインスタンス
	 * @throws IllegalAccessException ドライバの実装が不正だった場合
	 * @throws InstantiationException ドライバの実装が不正だった場合
	 * @throws DriverNotFoundException ドライバが見つからなかった場合
	 * @throws FileNotFoundException ファイルが見つからなかった場合
	 * @throws IOException I/Oエラーが発生した場合
	 * @throws IllegalArgumentException 引数pathsに{@code null}を与えた場合
	 */
	public static Driver getDriverInstance(URL[] paths, String fqcn) throws InstantiationException,
			IllegalAccessException, DriverNotFoundException, IOException {
		Driver driver = null;
		
		List<Class<? extends Driver>> classes = getDriverClasses(paths);
		for (Class<? extends Driver> clazz : classes) {
			if (clazz.getName().equals(fqcn)) {
				driver = clazz.newInstance();
				break;
			}
		}
		
		if (driver == null) {
			try {
				driver = (Driver) Class.forName(fqcn).newInstance();
			} catch (ClassNotFoundException e) {
				throw new DriverNotFoundException(fqcn);
			}
		}
		return driver;
	}
	
	private DriverUtil() {
	}
	

	/**
	 * JARファイル内の、{@link Driver}を実装したクラスのリストを抽出する。
	 * 
	 * @author daisuke
	 */
	private static final class GetDriverClassesFromJarHandler implements ResourceHandler {
		
		private static final String CLASS_EXTENSION = ".class";
		
		private final List<Class<? extends Driver>> driverList;
		
		private final URLClassLoader classLoader;
		

		/**
		 * インスタンスを生成する。
		 * 
		 * @param driverList 見つかったドライバクラスを保持するリスト
		 * @param classLoader ドライバを読み込むクラスローダ
		 */
		private GetDriverClassesFromJarHandler(List<Class<? extends Driver>> driverList, URLClassLoader classLoader) {
			this.driverList = driverList;
			this.classLoader = classLoader;
		}
		
		public void processResource(String path, InputStream is) throws TraversalHandlerException {
			if (path.endsWith(CLASS_EXTENSION) == false) {
				return;
			}
			
			String ccls = StringUtils.substring(path, 0, -1 * CLASS_EXTENSION.length());
			try {
				Class<?> clazz = classLoader.loadClass(ccls.replaceAll("/", ClassUtils.PACKAGE_SEPARATOR));
				Class<?>[] interfaceClasses = clazz.getInterfaces();
				for (Class<?> interfaceClass : interfaceClasses) {
					if (interfaceClass.equals(Driver.class)) {
						// 直前でロジックによる型チェックを行っているため、キャスト安全である。
						@SuppressWarnings("unchecked")
						Class<? extends Driver> driverClass = (Class<? extends Driver>) clazz;
						driverList.add(driverClass);
					}
				}
			} catch (NoClassDefFoundError e) {
				// ignore
//				logger.warn("NoClassDefFoundError: ", e);
			} catch (ClassNotFoundException ignore) {
				throw new JiemamyError("クラスは必ず存在するはずである。");
			} catch (Throwable t) {
				throw new TraversalHandlerException(t);
			}
		}
	}
}
