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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * {@link ClassLoader}を扱うためのユーティリティクラス。
 * 
 * @author j5ik2o
 */
public class ClassLoaderUtil {
	
	private static final Method FIND_LOADED_CLASS_METHOD = getFindLoadedClassMethod();
	
	private static final Method DEFINE_CLASS_METHOD = getDefineClassMethod();
	
	private static final Method DEFINE_PACKAGE_METHOD = getDefinePackageMethod();
	

	/**
	 * バイトの配列を<code>Class</code>クラスのインスタンスに変換する。
	 * 
	 * @param classLoader バイナリデータから<code>Class</code>クラスのインスタンスに変換するクラスローダ
	 * @param className クラスのバイナリ名
	 * @param bytes クラスデータを構成するバイト列
	 * @param offset クラスデータ<code>bytes</code>の開始オフセット
	 * @param length クラスデータの長さ
	 * @return 指定されたクラスデータから作成された<code>Class</code>オブジェクト
	 * @throws InvocationTargetException メソッドが例外をスローする場合
	 * @throws IllegalAccessException メソッドにアクセスできない場合
	 * @throws IllegalArgumentException 引数が正しくない場合
	 */
	public static Class<?> defineClass(final ClassLoader classLoader, final String className, final byte[] bytes,
			final int offset, final int length) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		return (Class<?>) MethodUtil.invoke(DEFINE_CLASS_METHOD, classLoader, new Object[] {
			className,
			bytes,
			Integer.valueOf(offset),
			Integer.valueOf(length)
		});
	}
	
	/**
	 * 指定の<code>ClassLoader</code>で名前を使ってパッケージを定義する。
	 * 
	 * @param classLoader パッケージを定義するクラスローダ
	 * @param name パッケージ名
	 * @param specTitle 仕様のタイトル
	 * @param specVersion 仕様のバージョン
	 * @param specVendor 仕様のベンダー
	 * @param implTitle 実装のタイトル
	 * @param implVersion 実装のバージョン
	 * @param implVendor 実装のベンダー
	 * @param sealBase <code>null</code>でない場合、このパッケージは指定されたコードソース<code>URL</code>オブジェクトを考慮してシールされる。そうでない場合、パッケージはシールされない
	 * @return 新しく定義された<code>Package</code>オブジェクト
	 * @throws InvocationTargetException メソッドが例外をスローする場合
	 * @throws IllegalAccessException メソッドにアクセスできない場合
	 * @throws IllegalArgumentException 引数が正しくない場合
	 */
	// CHECKSTYLE:OFF
	public static Package definePackage(final ClassLoader classLoader, final String name, final String specTitle,
			final String specVersion, final String specVendor, final String implTitle, final String implVersion,
			final String implVendor, final URL sealBase) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		return (Package) MethodUtil.invoke(DEFINE_PACKAGE_METHOD, classLoader, new Object[] {
			name,
			specTitle,
			specVersion,
			specVendor,
			implTitle,
			implVersion,
			implVendor,
			sealBase
		});
	}
	
	// CHECKSTYLE:ON
	
	/**
	 * 指定のクラスローダまたはその祖先のクラスローダが、 このバイナリ名を持つクラスの起動ローダとしてJava仮想マシンにより記録されていた場合は、
	 * 指定されたバイナリ名を持つクラスを取得する。 記録されていなかった場合は<code>null</code>を取得する。
	 * 
	 * @param classLoader クラスローダ
	 * @param className クラスのバイナリ名
	 * @return <code>Class</code>オブジェクト。クラスがロードされていない場合は<code>null</code>
	 * @throws InvocationTargetException メソッドが例外をスローする場合
	 * @throws IllegalAccessException メソッドにアクセスできない場合
	 * @throws IllegalArgumentException 引数が正しくない場合
	 */
	public static Class<?> findLoadedClass(final ClassLoader classLoader, final String className)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		for (ClassLoader loader = classLoader; loader != null; loader = loader.getParent()) {
			final Class<?> clazz = (Class<?>) MethodUtil.invoke(FIND_LOADED_CLASS_METHOD, loader, new Object[] {
				className
			});
			if (clazz != null) {
				return clazz;
			}
		}
		return null;
	}
	
	/**
	 * クラスローダを取得する。
	 * 
	 * @param targetClass ターゲット・クラス
	 * @return クラスローダ
	 */
	public static ClassLoader getClassLoader(final Class<?> targetClass) {
		final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		if (contextClassLoader != null) {
			return contextClassLoader;
		}
		
		final ClassLoader targetClassLoader = targetClass.getClassLoader();
		final ClassLoader thisClassLoader = ClassLoaderUtil.class.getClassLoader();
		if (targetClassLoader != null && thisClassLoader != null) {
			if (isAncestor(thisClassLoader, targetClassLoader)) {
				return thisClassLoader;
			}
			return targetClassLoader;
		}
		if (targetClassLoader != null) {
			return targetClassLoader;
		}
		if (thisClassLoader != null) {
			return thisClassLoader;
		}
		
		final ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
		if (systemClassLoader != null) {
			return systemClassLoader;
		}
		
		throw new IllegalStateException();
	}
	
	private static Method getDefineClassMethod() {
		try {
			Method method = ClassUtil.getDeclaredMethod(ClassLoader.class, "defineClass", new Class[] {
				String.class,
				byte[].class,
				int.class,
				int.class
			});
			method.setAccessible(true);
			return method;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Method getDefinePackageMethod() {
		try {
			Method method = ClassUtil.getDeclaredMethod(ClassLoader.class, "definePackage", new Class[] {
				String.class,
				String.class,
				String.class,
				String.class,
				String.class,
				String.class,
				String.class,
				URL.class
			});
			method.setAccessible(true);
			return method;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Method getFindLoadedClassMethod() {
		try {
			Method method = ClassUtil.getDeclaredMethod(ClassLoader.class, "findLoadedClass", new Class[] {
				String.class
			});
			method.setAccessible(true);
			return method;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * {@link #getClassLoader(Class)}が返すクラスローダから指定された名前を持つすべてのリソースを検索する。
	 * 
	 * @param targetClass ターゲット・クラス
	 * @param name リソース名
	 * @return リソースに対するURL。オブジェクトの列挙。リソースが見つからなかった場合、列挙は空になる。クラスローダがアクセスを持たないリソースは列挙に入らない
	 * @throws IOException IOException 入出力エラーが発生した場合
	 * @see java.lang.ClassLoader#getResources(String)
	 */
	public static Iterator<?> getResources(final Class<?> targetClass, final String name) throws IOException {
		return getResources(getClassLoader(targetClass), name);
	}
	
	/**
	 * 指定のクラスローダから指定された名前を持つすべてのリソースを検索する。
	 * 
	 * @param loader クラスローダ
	 * @param name リソース名
	 * @return リソースに対するURL。オブジェクトの列挙。リソースが見つからなかった場合、列挙は空になる。クラスローダがアクセスを持たないリソースは列挙に入らない
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public static Iterator<URL> getResources(final ClassLoader loader, final String name) throws IOException {
		
		final Enumeration<URL> e = loader.getResources(name);
		return new EnumerationIterator<URL>(e);
		
	}
	
	/**
	 * コンテキストクラスローダから指定された名前を持つすべてのリソースを検索する。
	 * 
	 * @param name リソース名
	 * @return リソースに対するURL。オブジェクトの列挙。リソースが見つからなかった場合、列挙は空になる。クラスローダがアクセスを持たないリソースは列挙に入らない
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public static Iterator<URL> getResources(final String name) throws IOException {
		return getResources(Thread.currentThread().getContextClassLoader(), name);
	}
	
	/**
	 * クラスローダ<code>other</code>がクラスローダ<code>cl</code>の祖先なら<code>true</code>を取得する。
	 * 
	 * @param cl クラスローダ
	 * @param other クラスローダ
	 * @return クラスローダ<code>other</code>がクラスローダ<code>cl</code>の祖先なら<code>true</code>
	 */
	protected static boolean isAncestor(ClassLoader cl, final ClassLoader other) {
		ClassLoader current = cl;
		while (current != null) {
			if (current == other) {
				return true;
			}
			current = current.getParent();
		}
		return false;
	}
	
	/**
	 * 指定されたバイナリ名を持つクラスをロードする。
	 * 
	 * @param loader クラスローダ
	 * @param className クラスのバイナリ名
	 * @return 結果の<code>Class</code>オブジェクト
	 * @throws ClassNotFoundException クラスが見つからなかった場合
	 */
	public static Class<?> loadClass(final ClassLoader loader, final String className) throws ClassNotFoundException {
		return loader.loadClass(className);
	}
	
	private ClassLoaderUtil() {
	}
	
}
