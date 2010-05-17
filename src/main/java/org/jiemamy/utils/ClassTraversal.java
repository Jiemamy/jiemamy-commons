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
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.Validate;

import org.jiemamy.utils.reflect.ClassUtil;

/**
 * クラスを横断して処理するためのハンドらクラス。
 * 
 * @author j5ik2o
 */
public class ClassTraversal {
	
	private static final String CLASS_SUFFIX = ".class";
	
	private static final String WAR_FILE_EXTENSION = ".war";
	
	private static final String WEB_INF_CLASSES_PATH = "WEB-INF/classes/";
	

	/**
	 * rootディレクトリ配下を処理します。
	 * 
	 * @param rootDir ルートディレクトリ
	 * @param handler ハンドラ
	 * @throws TraversalHandlerException ハンドラの処理が失敗した場合
	 */
	public static void forEach(File rootDir, ClassHandler handler) throws TraversalHandlerException {
		forEach(rootDir, null, handler);
	}
	
	/**
	 * rootディレクトリ配下でrootパッケージ名配下を処理します。
	 * 
	 * @param rootDir ルートディレクトリ
	 * @param rootPackage ルートパッケージ
	 * @param handler ハンドラ
	 * @throws TraversalHandlerException ハンドラの処理が失敗した場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static void forEach(File rootDir, String rootPackage, ClassHandler handler) throws TraversalHandlerException {
		Validate.notNull(rootDir);
		Validate.notNull(rootPackage);
		Validate.notNull(handler);
		File packageDir = getPackageDir(rootDir, rootPackage);
		if (packageDir.exists()) {
			traverseFileSystem(packageDir, rootPackage, handler);
		}
	}
	
	/**
	 * 指定されたjarファイルを処理します。
	 * 
	 * @param jarFile Jarファイル
	 * @param handler ハンドラ
	 * @throws TraversalHandlerException ハンドラの処理が失敗した場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static void forEach(JarFile jarFile, ClassHandler handler) throws TraversalHandlerException {
		Validate.notNull(jarFile);
		Validate.notNull(handler);
		boolean hasWarExtension = jarFile.getName().endsWith(WAR_FILE_EXTENSION);
		Enumeration<JarEntry> enumeration = jarFile.entries();
		while (enumeration.hasMoreElements()) {
			JarEntry entry = enumeration.nextElement();
			String entryName = entry.getName().replace('\\', '/');
			if (entryName.endsWith(CLASS_SUFFIX)) {
				int startPos =
						hasWarExtension && entryName.startsWith(WEB_INF_CLASSES_PATH) ? WEB_INF_CLASSES_PATH.length()
								: 0;
				String className =
						entryName.substring(startPos, entryName.length() - CLASS_SUFFIX.length()).replace('/', '.');
				int pos = className.lastIndexOf('.');
				String packageName = (pos == -1) ? null : className.substring(0, pos);
				String shortClassName = (pos == -1) ? className : className.substring(pos + 1);
				handler.processClass(packageName, shortClassName);
			}
		}
	}
	
	private static File getPackageDir(File rootDir, String rootPackage) {
		File packageDir = rootDir;
		if (rootPackage != null) {
			String[] names = rootPackage.split("\\.");
			for (String name : names) {
				packageDir = new File(packageDir, name);
			}
		}
		return packageDir;
	}
	
	private static void traverseFileSystem(File dir, String packageName, ClassHandler handler)
			throws TraversalHandlerException {
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; ++i) {
			File file = files[i];
			String fileName = file.getName();
			if (file.isDirectory()) {
				traverseFileSystem(file, ClassUtil.concatName(packageName, fileName), handler);
			} else if (fileName.endsWith(".class")) {
				String shortClassName = fileName.substring(0, fileName.length() - CLASS_SUFFIX.length());
				handler.processClass(packageName, shortClassName);
			}
		}
	}
	
	private ClassTraversal() {
	}
	

	/**
	 * クラスを横断して処理するためのハンドラインターフェイス。
	 * 
	 */
	public interface ClassHandler {
		
		/**
		 * クラスを処理する。
		 * 
		 * @param packageName パッケージ名
		 * @param shortClassName クラス名
		 * @throws TraversalHandlerException ハンドラの処理が失敗した場合
		 */
		void processClass(String packageName, String shortClassName) throws TraversalHandlerException;
	}
}
