/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;

/**
 * リソースを横断的に処理するためのクラス。
 * 
 * @version $Id$
 * @author j5ik2o
 */
public final class ResourceTraversal {
	
	/**
	 * リソースを横断的に処理する。
	 * 
	 * @param rootDir ルートディレクトリ
	 * @param handler ハンドラ
	 * @throws IOException 入出力が失敗した場合
	 * @throws TraversalHandlerException ハンドラの処理が失敗した場合 
	 */
	public static void forEach(File rootDir, ResourceHandler handler) throws IOException, TraversalHandlerException {
		forEach(rootDir, null, handler);
	}
	
	/**
	 * リソースを横断的に処理する。
	 * 
	 * @param rootDir ルートディレクトリ
	 * @param baseDirectory ベースディレクトリ
	 * @param handler ハンドラ
	 * @throws IOException 入出力が失敗した場合
	 * @throws TraversalHandlerException ハンドラの処理が失敗した場合 
	 * @throws IllegalArgumentException 引数{@code rootDir}, {@code handler}に{@code null}を与えた場合
	 */
	public static void forEach(File rootDir, String baseDirectory, ResourceHandler handler) throws IOException,
			TraversalHandlerException {
		Validate.notNull(rootDir);
		Validate.notNull(handler);
		File baseDir = getBaseDir(rootDir, baseDirectory);
		if (baseDir.exists()) {
			traverseFileSystem(rootDir, baseDir, handler);
		}
	}
	
	/**
	 * リソースを横断的に処理する。
	 * 
	 * @param jarFile JarFile
	 * @param handler ハンドラ
	 * @throws IOException 入出力が失敗した場合
	 * @throws TraversalHandlerException ハンドラの処理が失敗した場合 
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static void forEach(JarFile jarFile, ResourceHandler handler) throws IOException, TraversalHandlerException {
		Validate.notNull(jarFile);
		Validate.notNull(handler);
		Enumeration<JarEntry> enumeration = jarFile.entries();
		while (enumeration.hasMoreElements()) {
			JarEntry entry = enumeration.nextElement();
			if (!entry.isDirectory()) {
				String entryName = entry.getName().replace('\\', '/');
				InputStream is = jarFile.getInputStream(entry);
				try {
					handler.processResource(entryName, is);
				} finally {
					IOUtils.closeQuietly(is);
				}
			}
		}
	}
	
	private static File getBaseDir(File rootDir, String baseDirectory) {
		File baseDir = rootDir;
		if (baseDirectory != null) {
			String[] names = baseDirectory.split("/");
			for (String name : names) {
				baseDir = new File(baseDir, name);
			}
		}
		return baseDir;
	}
	
	private static void traverseFileSystem(File rootDir, File baseDir, ResourceHandler handler) throws IOException,
			TraversalHandlerException {
		File[] files = baseDir.listFiles();
		for (int i = 0; i < files.length; ++i) {
			File file = files[i];
			if (file.isDirectory()) {
				traverseFileSystem(rootDir, file, handler);
			} else {
				int pos = rootDir.getCanonicalPath().length();
				String filePath = file.getCanonicalPath();
				String resourcePath = filePath.substring(pos + 1).replace('\\', '/');
				InputStream is = new FileInputStream(file);
				try {
					handler.processResource(resourcePath, is);
				} finally {
					IOUtils.closeQuietly(is);
				}
			}
		}
	}
	
	/**
	 * インスタンスを構築します。
	 */
	private ResourceTraversal() {
	}
	

	/**
	 * リソースを処理するインターフェース。
	 * 
	 */
	public interface ResourceHandler {
		
		/**
		 * リソースを処理します。
		 * 
		 * @param path パス
		 * @param is {@link InputStream}
		 * @throws TraversalHandlerException ハンドラの処理に失敗した場合
		 */
		void processResource(String path, InputStream is) throws TraversalHandlerException;
	}
}
