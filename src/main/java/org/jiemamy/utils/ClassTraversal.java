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
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.jiemamy.exception.JiemamyError;

/**
 * クラスを横断して処理するためのハンドらクラス。
 * 
 * @author j5ik2o
 */
public class ClassTraversal {
	
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
	public static void forEach(final File rootDir, final ClassHandler handler) throws TraversalHandlerException {
		forEach(rootDir, null, handler);
	}
	
	/**
	 * rootディレクトリ配下でrootパッケージ名配下を処理します。
	 * 
	 * @param rootDir ルートディレクトリ
	 * @param rootPackage ルートパッケージ
	 * @param handler ハンドラ
	 * @throws TraversalHandlerException ハンドラの処理が失敗した場合
	 */
	public static void forEach(final File rootDir, final String rootPackage, final ClassHandler handler)
			throws TraversalHandlerException {
		final File packageDir = getPackageDir(rootDir, rootPackage);
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
	 */
	public static void forEach(final JarFile jarFile, final ClassHandler handler) throws TraversalHandlerException {
		final boolean hasWarExtension = jarFile.getName().endsWith(WAR_FILE_EXTENSION);
		final Enumeration<JarEntry> enumeration = jarFile.entries();
		while (enumeration.hasMoreElements()) {
			final JarEntry entry = enumeration.nextElement();
			final String entryName = entry.getName().replace('\\', '/');
			if (entryName.endsWith(CLASS_SUFFIX)) {
				final int startPos =
						hasWarExtension && entryName.startsWith(WEB_INF_CLASSES_PATH) ? WEB_INF_CLASSES_PATH.length()
								: 0;
				final String className =
						entryName.substring(startPos, entryName.length() - CLASS_SUFFIX.length()).replace('/', '.');
				final int pos = className.lastIndexOf('.');
				final String packageName = (pos == -1) ? null : className.substring(0, pos);
				final String shortClassName = (pos == -1) ? className : className.substring(pos + 1);
				handler.processClass(packageName, shortClassName);
			}
		}
	}
	
	private static File getPackageDir(final File rootDir, final String rootPackage) {
		File packageDir = rootDir;
		if (rootPackage != null) {
			final String[] names = rootPackage.split("\\.");
			for (String name : names) {
				packageDir = new File(packageDir, name);
			}
		}
		return packageDir;
	}
	
	private static void traverseFileSystem(final File dir, final String packageName, final ClassHandler handler)
			throws TraversalHandlerException {
		final File[] files = dir.listFiles();
		for (int i = 0; i < files.length; ++i) {
			final File file = files[i];
			final String fileName = file.getName();
			if (file.isDirectory()) {
				traverseFileSystem(file, ClassUtil.concatName(packageName, fileName), handler);
			} else if (fileName.endsWith(".class")) {
				final String shortClassName = fileName.substring(0, fileName.length() - CLASS_SUFFIX.length());
				handler.processClass(packageName, shortClassName);
			}
		}
	}
	
	private ClassTraversal() {
		throw new JiemamyError("不到達ポイント");
	}
}
