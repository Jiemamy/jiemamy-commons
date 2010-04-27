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
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * リソースを横断的に処理するためのクラス。
 * 
 * @author j5ik2o
 */
public class ResourceTraversal {
	
	/**
	 * リソースを処理するインターフェースです。
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
	

	/**
	 * リソースを横断的に処理する。
	 * 
	 * @param rootDir ルートディレクトリ
	 * @param handler ハンドラ
	 * @throws IOException 入出力が失敗した場合
	 * @throws TraversalHandlerException ハンドラの処理が失敗した場合 
	 */
	public static void forEach(final File rootDir, final ResourceHandler handler) throws IOException,
			TraversalHandlerException {
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
	 */
	public static void forEach(final File rootDir, final String baseDirectory, final ResourceHandler handler)
			throws IOException, TraversalHandlerException {
		final File baseDir = getBaseDir(rootDir, baseDirectory);
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
	 */
	public static void forEach(final JarFile jarFile, final ResourceHandler handler) throws IOException,
			TraversalHandlerException {
		final Enumeration<JarEntry> enumeration = jarFile.entries();
		while (enumeration.hasMoreElements()) {
			final JarEntry entry = enumeration.nextElement();
			if (!entry.isDirectory()) {
				final String entryName = entry.getName().replace('\\', '/');
				final InputStream is = JarFileUtil.getInputStream(jarFile, entry);
				try {
					handler.processResource(entryName, is);
				} finally {
					InputStreamUtil.close(is);
				}
			}
		}
	}
	
	private static File getBaseDir(final File rootDir, final String baseDirectory) {
		File baseDir = rootDir;
		if (baseDirectory != null) {
			final String[] names = baseDirectory.split("/");
			for (String name : names) {
				baseDir = new File(baseDir, name);
			}
		}
		return baseDir;
	}
	
	private static void traverseFileSystem(final File rootDir, final File baseDir, final ResourceHandler handler)
			throws IOException, TraversalHandlerException {
		final File[] files = baseDir.listFiles();
		for (int i = 0; i < files.length; ++i) {
			final File file = files[i];
			if (file.isDirectory()) {
				traverseFileSystem(rootDir, file, handler);
			} else {
				final int pos = FileUtil.getCanonicalPath(rootDir).length();
				final String filePath = FileUtil.getCanonicalPath(file);
				final String resourcePath = filePath.substring(pos + 1).replace('\\', '/');
				final InputStream is = FileInputStreamUtil.create(file);
				try {
					handler.processResource(resourcePath, is);
				} finally {
					InputStreamUtil.close(is);
				}
			}
		}
	}
	
	/**
	 * インスタンスを構築します。
	 */
	private ResourceTraversal() {
	}
}
