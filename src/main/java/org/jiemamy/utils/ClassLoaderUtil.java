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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

import org.apache.commons.collections15.iterators.EnumerationIterator;
import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyError;
import org.jiemamy.utils.reflect.MethodUtil;

/**
 * {@link ClassLoader}を扱うためのユーティリティクラス。
 * 
 * @author j5ik2o
 */
public final class ClassLoaderUtil {
	
	static final Method FIND_LOADED_CLASS_METHOD = getFindLoadedClassMethod();
	
	static final Method DEFINE_CLASS_METHOD = getDefineClassMethod();
	
	static final Method DEFINE_PACKAGE_METHOD = getDefinePackageMethod();
	

	/**
	 * バイトの配列を{@code Class}クラスのインスタンスに変換する。
	 * 
	 * @param classLoader バイナリデータから{@code Class}クラスのインスタンスに変換するクラスローダ
	 * @param className クラスのバイナリ名
	 * @param bytes クラスデータを構成するバイト列
	 * @param offset クラスデータ{@code bytes}の開始オフセット
	 * @param length クラスデータの長さ
	 * @return 指定されたクラスデータから作成された{@code Class}オブジェクト
	 * @throws InvocationTargetException メソッドが例外をスローする場合
	 * @throws IllegalAccessException メソッドにアクセスできない場合
	 * @throws IllegalArgumentException 引数が正しくない場合
	 */
	public static Class<?> defineClass(ClassLoader classLoader, String className, byte[] bytes, int offset, int length)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		return (Class<?>) MethodUtil.invoke(DEFINE_CLASS_METHOD, classLoader, new Object[] {
			className,
			bytes,
			Integer.valueOf(offset),
			Integer.valueOf(length)
		});
	}
	
	/**
	 * 指定の{@code ClassLoader}で名前を使ってパッケージを定義する。
	 * 
	 * @param classLoader パッケージを定義するクラスローダ
	 * @param name パッケージ名
	 * @param specTitle 仕様のタイトル
	 * @param specVersion 仕様のバージョン
	 * @param specVendor 仕様のベンダー
	 * @param implTitle 実装のタイトル
	 * @param implVersion 実装のバージョン
	 * @param implVendor 実装のベンダー
	 * @param sealBase {@code null}でない場合、このパッケージは指定されたコードソース{@code URL}オブジェクトを考慮してシールされる。そうでない場合、パッケージはシールされない
	 * @return 新しく定義された{@code Package}オブジェクト
	 * @throws InvocationTargetException メソッドが例外をスローする場合
	 * @throws IllegalAccessException メソッドにアクセスできない場合
	 * @throws IllegalArgumentException 引数が正しくない場合
	 */
	// CHECKSTYLE:OFF
	public static Package definePackage(ClassLoader classLoader, String name, String specTitle, String specVersion,
			String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		// CHECKSTYLE:ON
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
	
	/**
	 * 指定のクラスローダまたはその祖先のクラスローダが、 このバイナリ名を持つクラスの起動ローダとしてJava仮想マシンにより記録されていた場合は、
	 * 指定されたバイナリ名を持つクラスを取得する。 記録されていなかった場合は{@code null}を取得する。
	 * 
	 * @param classLoader クラスローダ
	 * @param className クラスのバイナリ名
	 * @return {@code Class}オブジェクト。クラスがロードされていない場合は{@code null}
	 * @throws InvocationTargetException メソッドが例外をスローする場合
	 * @throws IllegalAccessException メソッドにアクセスできない場合
	 * @throws IllegalArgumentException 引数が正しくない場合
	 */
	public static Class<?> findLoadedClass(ClassLoader classLoader, String className) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		for (ClassLoader loader = classLoader; loader != null; loader = loader.getParent()) {
			Class<?> clazz = (Class<?>) MethodUtil.invoke(FIND_LOADED_CLASS_METHOD, loader, new Object[] {
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
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static ClassLoader getClassLoader(Class<?> targetClass) {
		Validate.notNull(targetClass);
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		if (contextClassLoader != null) {
			return contextClassLoader;
		}
		
		ClassLoader targetClassLoader = targetClass.getClassLoader();
		ClassLoader thisClassLoader = ClassLoaderUtil.class.getClassLoader();
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
		
		ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
		if (systemClassLoader != null) {
			return systemClassLoader;
		}
		
		throw new IllegalStateException();
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
	public static Iterator<?> getResources(Class<?> targetClass, String name) throws IOException {
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
	public static Iterator<URL> getResources(ClassLoader loader, String name) throws IOException {
		Enumeration<URL> e = loader.getResources(name);
		return new EnumerationIterator<URL>(e);
		
	}
	
	/**
	 * コンテキストクラスローダから指定された名前を持つすべてのリソースを検索する。
	 * 
	 * @param name リソース名
	 * @return リソースに対するURL。オブジェクトの列挙。リソースが見つからなかった場合、列挙は空になる。クラスローダがアクセスを持たないリソースは列挙に入らない
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public static Iterator<URL> getResources(String name) throws IOException {
		return getResources(Thread.currentThread().getContextClassLoader(), name);
	}
	
	/**
	 * 指定されたバイナリ名を持つクラスをロードする。
	 * 
	 * @param loader クラスローダ
	 * @param className クラスのバイナリ名
	 * @return 結果の{@code Class}オブジェクト
	 * @throws ClassNotFoundException クラスが見つからなかった場合
	 */
	public static Class<?> loadClass(ClassLoader loader, String className) throws ClassNotFoundException {
		return loader.loadClass(className);
	}
	
	/**
	 * クラスローダ{@code other}がクラスローダ{@code cl}の祖先なら{@code true}を取得する。
	 * 
	 * @param cl クラスローダ
	 * @param other クラスローダ
	 * @return クラスローダ{@code other}がクラスローダ{@code cl}の祖先なら{@code true}
	 */
	protected static boolean isAncestor(ClassLoader cl, ClassLoader other) {
		ClassLoader current = cl;
		while (current != null) {
			if (current == other) {
				return true;
			}
			current = current.getParent();
		}
		return false;
	}
	
	private static Method getDefineClassMethod() {
		try {
			Method method = ClassLoader.class.getDeclaredMethod("defineClass", new Class[] {
				String.class,
				byte[].class,
				int.class,
				int.class
			});
			method.setAccessible(true);
			return method;
		} catch (NoSuchMethodException e) {
			throw new JiemamyError("Wrong usage of getDeclaredMethods", e);
		}
	}
	
	private static Method getDefinePackageMethod() {
		try {
			Method method = ClassLoader.class.getDeclaredMethod("definePackage", new Class[] {
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
		} catch (NoSuchMethodException e) {
			throw new JiemamyError("Wrong usage of getDeclaredMethods", e);
		}
	}
	
	private static Method getFindLoadedClassMethod() {
		try {
			Method method = ClassLoader.class.getDeclaredMethod("findLoadedClass", new Class[] {
				String.class
			});
			method.setAccessible(true);
			return method;
		} catch (NoSuchMethodException e) {
			throw new JiemamyError("Wrong usage of getDeclaredMethods", e);
		}
	}
	
	private ClassLoaderUtil() {
	}
	
}
