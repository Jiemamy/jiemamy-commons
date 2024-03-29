/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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
package org.jiemamy.utils.sql;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.Collection;
import java.util.jar.JarFile;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyError;
import org.jiemamy.utils.ClassTraversal;
import org.jiemamy.utils.ClassTraversal.ClassHandler;
import org.jiemamy.utils.LogMarker;
import org.jiemamy.utils.TraversalHandlerException;
import org.jiemamy.utils.reflect.ClassUtil;

/**
 * JDBCドライバ関係のユーティリティクラス。
 * 
 * @version $Id$
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
	 * @throws IllegalArgumentException 引数が{@code null}要素を持つまたは、引数自体が{@code null}の場合
	 * @throws IllegalArgumentException URIとして不適切なURLが引数に含まれていた場合
	 * @throws FileNotFoundException pathsに指定したJARファイルが見つからなかった場合
	 */
	public static Collection<Class<? extends Driver>> getDriverClasses(URL[] paths) throws IOException {
		Validate.noNullElements(paths);
		
		URLClassLoader classLoader = new URLClassLoader(paths);
		
		Collection<Class<? extends Driver>> driverList = Lists.newArrayList();
		
		for (URL path : paths) {
			try {
				File file = new File(path.toURI());
				if (file.exists() == false) {
					throw new FileNotFoundException(file.getAbsolutePath());
				}
				JarFile jarFile = new JarFile(file);
				ClassTraversal.forEach(jarFile, new GetDriverClassesFromJarHandler(driverList, classLoader));
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException(path.toString(), e);
			} catch (TraversalHandlerException e) {
				logger.error(LogMarker.DETAIL, "fail to handle jar entry.", e);
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
	 * @throws FileNotFoundException pathsに指定したJARファイルが見つからなかった場合
	 * @throws IOException I/Oエラーが発生した場合
	 * @throws IllegalArgumentException 引数{@code paths}が{@code null}要素を持つまたは、引数に{@code null}を与えた場合
	 */
	public static Driver getDriverInstance(URL[] paths, String fqcn) throws InstantiationException,
			IllegalAccessException, DriverNotFoundException, IOException {
		Validate.noNullElements(paths);
		Validate.notNull(fqcn);
		Driver driver = null;
		
		Collection<Class<? extends Driver>> classes = getDriverClasses(paths);
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
	private static final class GetDriverClassesFromJarHandler implements ClassHandler {
		
		private final Collection<Class<? extends Driver>> driverList;
		
		private final URLClassLoader classLoader;
		

		/**
		 * インスタンスを生成する。
		 * 
		 * @param driverList 見つかったドライバクラスを保持するリスト
		 * @param classLoader ドライバを読み込むクラスローダ
		 */
		private GetDriverClassesFromJarHandler(Collection<Class<? extends Driver>> driverList,
				URLClassLoader classLoader) {
			assert driverList != null;
			assert classLoader != null;
			this.driverList = driverList;
			this.classLoader = classLoader;
		}
		
		public void processClass(String packageName, String shortClassName) throws TraversalHandlerException {
			String fqcn = ClassUtil.concatName(packageName, shortClassName);
			try {
				Class<?> clazz = classLoader.loadClass(fqcn);
				Class<?>[] interfaceClasses = clazz.getInterfaces();
				for (Class<?> interfaceClass : interfaceClasses) {
					if (interfaceClass.equals(Driver.class)) {
						// 直前でreflectionによる型チェックを行っているため、キャスト安全である。
						@SuppressWarnings("unchecked")
						Class<? extends Driver> driverClass = (Class<? extends Driver>) clazz;
						driverList.add(driverClass);
					}
				}
			} catch (NoClassDefFoundError e) {
				// ignore
			} catch (ClassNotFoundException e) {
				throw new JiemamyError("Class must to be in classpath: " + fqcn, e);
			} catch (Throwable t) {
				throw new TraversalHandlerException(t);
			}
		}
	}
}
