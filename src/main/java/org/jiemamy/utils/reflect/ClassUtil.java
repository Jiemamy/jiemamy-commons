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
package org.jiemamy.utils.reflect;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import org.jiemamy.utils.ClassPoolUtil;

/**
 * {@link Class}用のユーティリティクラスです。
 * 
 * @author j5ik2o
 * 
 */
public final class ClassUtil {
	
	private static Map<Class<?>, Class<?>> wrapperToPrimitiveMap = new HashMap<Class<?>, Class<?>>();
	
	private static Map<Class<?>, Class<?>> primitiveToWrapperMap = new HashMap<Class<?>, Class<?>>();
	
	private static Map<String, Class<?>> primitiveClsssNameMap = new HashMap<String, Class<?>>();
	
	static {
		wrapperToPrimitiveMap.put(Character.class, Character.TYPE);
		wrapperToPrimitiveMap.put(Byte.class, Byte.TYPE);
		wrapperToPrimitiveMap.put(Short.class, Short.TYPE);
		wrapperToPrimitiveMap.put(Integer.class, Integer.TYPE);
		wrapperToPrimitiveMap.put(Long.class, Long.TYPE);
		wrapperToPrimitiveMap.put(Double.class, Double.TYPE);
		wrapperToPrimitiveMap.put(Float.class, Float.TYPE);
		wrapperToPrimitiveMap.put(Boolean.class, Boolean.TYPE);
		
		primitiveToWrapperMap.put(Character.TYPE, Character.class);
		primitiveToWrapperMap.put(Byte.TYPE, Byte.class);
		primitiveToWrapperMap.put(Short.TYPE, Short.class);
		primitiveToWrapperMap.put(Integer.TYPE, Integer.class);
		primitiveToWrapperMap.put(Long.TYPE, Long.class);
		primitiveToWrapperMap.put(Double.TYPE, Double.class);
		primitiveToWrapperMap.put(Float.TYPE, Float.class);
		primitiveToWrapperMap.put(Boolean.TYPE, Boolean.class);
		
		primitiveClsssNameMap.put(Character.TYPE.getName(), Character.TYPE);
		primitiveClsssNameMap.put(Byte.TYPE.getName(), Byte.TYPE);
		primitiveClsssNameMap.put(Short.TYPE.getName(), Short.TYPE);
		primitiveClsssNameMap.put(Integer.TYPE.getName(), Integer.TYPE);
		primitiveClsssNameMap.put(Long.TYPE.getName(), Long.TYPE);
		primitiveClsssNameMap.put(Double.TYPE.getName(), Double.TYPE);
		primitiveClsssNameMap.put(Float.TYPE.getName(), Float.TYPE);
		primitiveClsssNameMap.put(Boolean.TYPE.getName(), Boolean.TYPE);
	}
	

	/**
	 * 与えた2つの文字列をパッケージ名または完全限定名の要素として扱い、これらをを結合する。
	 * 
	 * <p>Java言語仕様に従わない要素を与えた場合の挙動は未定義。</p>
	 * 
	 * @param s1 パッケージ名
	 * @param s2 パッケージ名もしくはクラス名
	 * @return 結合された名前
	 */
	public static String concatName(String s1, String s2) {
		if (StringUtils.isEmpty(s1) && StringUtils.isEmpty(s2)) {
			return s1;
		}
		if (StringUtils.isEmpty(s1) == false && StringUtils.isEmpty(s2)) {
			return s1;
		}
		if (StringUtils.isEmpty(s1) && StringUtils.isEmpty(s2) == false) {
			return s2;
		}
		return s1 + '.' + s2;
	}
	
	/**
	 * プリミティブクラスの場合は、ラッパークラスに変換する。
	 * 
	 * <p>そうでない場合は context {@link ClassLoader}を使って {@link Class#forName(String, boolean, ClassLoader)}した結果を返す。</p>
	 * 
	 * @param className クラス名
	 * @return {@link Class}
	 * @throws ClassNotFoundException クラスが見つからなかった場合
	 * @see #forName(String)
	 */
	public static Class<?> convertClass(String className) throws ClassNotFoundException {
		Class<?> clazz = primitiveClsssNameMap.get(className);
		if (clazz != null) {
			return clazz;
		}
		return forName(className);
	}
	
	/**
	 * 現在のスレッドの context {@link ClassLoader}から{@link Class}を取得する。 
	 * 
	 * @param className クラス名
	 * @return {@link Class}
	 * @throws ClassNotFoundException クラスが見つからなかった場合
	 * @see Class#forName(String)
	 */
	public static Class<?> forName(String className) throws ClassNotFoundException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		return Class.forName(className, true, loader);
	}
	
	/**
	 * このクラスに定義された{@link Field}をクラスファイルに定義された順番で取得する。
	 * 
	 * @param clazz 対象のクラス
	 * @return このクラスに定義されたフィールドの配列
	 * @throws NotFoundException {@link CtClass}が見つからなかった場合
	 * @throws NoSuchFieldException フィールドが見つからなかった場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Field[] getDeclaredFields(Class<?> clazz) throws NotFoundException, NoSuchFieldException {
		try {
			ClassPool pool = ClassPoolUtil.getClassPool(clazz);
			CtClass ctClass = ClassPoolUtil.toCtClass(pool, clazz);
			CtField[] ctFields = ctClass.getDeclaredFields();
			int size = ctFields.length;
			Field[] fields = new Field[size];
			for (int i = 0; i < size; ++i) {
				fields[i] = clazz.getDeclaredField(ctFields[i].getName());
			}
			return fields;
		} finally {
			ClassPoolUtil.dispose();
		}
	}
	
	/**
	 * {@link Field}を取得する。ただし、例外はスローせず、{@code null}を返す。
	 * 
	 * @param clazz クラス
	 * @param fieldName フィールド名
	 * @return {@link Field}
	 * @see Class#getField(String)
	 * @throws IllegalArgumentException 引数{@code clazz}に{@code null}を与えた場合
	 */
	public static Field getFieldNoException(Class<?> clazz, String fieldName) {
		Validate.notNull(clazz);
		try {
			return clazz.getField(fieldName);
		} catch (SecurityException e) {
			return null;
		} catch (NoSuchFieldException e) {
			return null;
		}
	}
	
	/**
	 * パッケージ名を取得する。
	 * 
	 * @param clazz クラス
	 * @return パッケージ名.  デフォルトパッケージの場合は{@code null}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static String getPackageName(Class<?> clazz) {
		Validate.notNull(clazz);
		String fqcn = clazz.getName();
		int pos = fqcn.lastIndexOf('.');
		if (pos > 0) {
			return fqcn.substring(0, pos);
		}
		return null;
	}
	
	/**
	 * クラス名をリソースパスに変換する。
	 * 
	 * @param clazz クラス
	 * @return リソースパス
	 * @see #getResourcePath(String)
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static String getResourcePath(Class<?> clazz) {
		Validate.notNull(clazz);
		return getResourcePath(clazz.getName());
	}
	
	/**
	 * クラス名をリソースパスに変換する。
	 * 
	 * @param className クラス名
	 * @return リソースパス
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static String getResourcePath(String className) {
		Validate.notNull(className);
		return StringUtils.replace(className, ".", "/") + ".class";
	}
	
	/**
	 * FQCNからパッケージ名を除いた名前を取得する。
	 * 
	 * @param clazz クラス
	 * @return FQCNからパッケージ名を除いた名前
	 * @see #getShortClassName(String)
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static String getShortClassName(Class<?> clazz) {
		Validate.notNull(clazz);
		return getShortClassName(clazz.getName());
	}
	
	/**
	 * FQCNからパッケージ名を除いた名前を取得する。
	 * 
	 * @param className クラス名
	 * @return FQCNからパッケージ名を除いた名前
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static String getShortClassName(String className) {
		Validate.notNull(className);
		int i = className.lastIndexOf('.');
		if (i > 0) {
			return className.substring(i + 1);
		}
		return className;
	}
	
	/**
	 * 配列の場合は要素のクラス名、それ以外はクラス名そのものを返します。
	 * 
	 * @param clazz クラス
	 * @return クラス名
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static String getSimpleClassName(Class<?> clazz) {
		Validate.notNull(clazz);
		if (clazz.isArray()) {
			return getSimpleClassName(clazz.getComponentType()) + "[]";
		}
		return clazz.getName();
	}
	
	/**
	 * 代入可能かどうかをチェックする。
	 * 
	 * @param toClass 代入先のクラス
	 * @param fromClass 代入元のクラス
	 * @return 代入可能かどうか
	 * @see Class#isAssignableFrom(Class)
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static boolean isAssignableFrom(Class<?> toClass, Class<?> fromClass) {
		Validate.notNull(toClass);
		Validate.notNull(fromClass);
		if (toClass == Object.class && fromClass.isPrimitive() == false) {
			return true;
		}
		if (toClass.isPrimitive()) {
			fromClass = toPrimitiveClassIfWrapper(fromClass);
		}
		return toClass.isAssignableFrom(fromClass);
	}
	
	/**
	 * 新しいインスタンスを作成する。
	 * 
	 * @param className クラス名
	 * @return 新しいインスタンス
	 * @throws InstantiationException この Class が abstract クラス、インタフェース、配列クラス、プリミティブ型、
	 * または void を表す場合、クラスが null コンストラクタを保持しない場合、あるいはインスタンスの生成がほかの理由で失敗した場合
	 * @throws IllegalAccessException コンストラクタにアクセスできない場合
	 * @throws ClassNotFoundException クラスが見つからなかった場合
	 */
	public static Object newInstance(String className) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return forName(className).newInstance();
	}
	
	/**
	 * FQCNをパッケージ名とFQCNからパッケージ名を除いた名前に分割する。
	 * 
	 * @param className クラス名
	 * @return パッケージ名とFQCNからパッケージ名を除いた名前
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static String[] splitPackageAndShortClassName(String className) {
		Validate.notNull(className);
		String[] ret = new String[2];
		int i = className.lastIndexOf('.');
		if (i > 0) {
			ret[0] = className.substring(0, i);
			ret[1] = className.substring(i + 1);
		} else {
			ret[1] = className;
		}
		return ret;
	}
	
	/**
	 * ラッパークラスをプリミティブクラスに変換する。
	 * 
	 * @param clazz クラス
	 * @return プリミティブクラス.  {@code clazz}がラッパークラスではない場合は{@code null}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> toPrimitiveClass(Class<?> clazz) {
		Validate.notNull(clazz);
		return wrapperToPrimitiveMap.get(clazz);
	}
	
	/**
	 * ラッパークラスならプリミティブクラスに、 そうでなければそのままクラスを取得する。
	 * 
	 * @param clazz クラス
	 * @return {@link Class}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> toPrimitiveClassIfWrapper(Class<?> clazz) {
		Validate.notNull(clazz);
		Class<?> ret = toPrimitiveClass(clazz);
		if (ret != null) {
			return ret;
		}
		return clazz;
	}
	
	/**
	 * プリミティブクラスをラッパークラスに変換する。
	 * 
	 * @param clazz クラス
	 * @return {@link Class}.  {@code clazz}がプリミティブクラスではない場合は{@code null}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> toWrapperClass(Class<?> clazz) {
		Validate.notNull(clazz);
		return primitiveToWrapperMap.get(clazz);
	}
	
	/**
	 * プリミティブの場合はラッパークラス、そうでない場合はもとのクラスを取得する。
	 * 
	 * @param clazz クラス
	 * @return {@link Class}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> toWrapperClassIfPrimitive(Class<?> clazz) {
		Validate.notNull(clazz);
		Class<?> ret = toWrapperClass(clazz);
		if (ret != null) {
			return ret;
		}
		return clazz;
	}
	
	private ClassUtil() {
	}
}
