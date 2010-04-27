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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

import org.apache.commons.lang.StringUtils;

import org.jiemamy.exception.JiemamyError;

/**
 * {@link Class}用のユーティリティクラスです。
 * 
 * @author j5ik2o
 * 
 */
public class ClassUtil {
	
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
	 * クラス名の要素を結合する。
	 * 
	 * @param s1 パッケージ名
	 * @param s2 パッケージ名もしくはクラス名
	 * @return 結合された名前
	 */
	public static String concatName(String s1, String s2) {
		if (StringUtils.isEmpty(s1) && StringUtils.isEmpty(s2)) {
			return null;
		}
		if (!StringUtils.isEmpty(s1) && StringUtils.isEmpty(s2)) {
			return s1;
		}
		if (StringUtils.isEmpty(s1) && !StringUtils.isEmpty(s2)) {
			return s2;
		}
		return s1 + '.' + s2;
	}
	
	/**
	 * プリミティブクラスの場合は、ラッパークラスに変換する。
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
	 * {@link Class}を取得する。
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
	 * {@link Constructor}を返します。
	 * 
	 * @param clazz クラス
	 * @param argTypes 引数
	 * @return {@link Constructor}
	 * @throws NoSuchMethodException メソッドが存在しない場合
	 * @throws SecurityException セキュリティ違反が発生した場合
	 * @see Class#getConstructor(Class[])
	 */
	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>[] argTypes) throws SecurityException,
			NoSuchMethodException {
		return clazz.getConstructor(argTypes);
	}
	
	/**
	 * そのクラスに宣言されている {@link Constructor}を取得する。
	 * 
	 * @param clazz クラス
	 * @param argTypes 引数
	 * @return {@link Constructor} 
	 * @throws NoSuchMethodException メソッドが存在しない場合
	 * @throws SecurityException セキュリティ違反が発生した場合
	 * @see Class#getDeclaredConstructor(Class[])
	 */
	public static Constructor<?> getDeclaredConstructor(Class<?> clazz, Class<?>[] argTypes) throws SecurityException,
			NoSuchMethodException {
		return clazz.getDeclaredConstructor(argTypes);
	}
	
	/**
	 * そのクラスに宣言されている {@link Field}を取得する。
	 * 
	 * @param clazz クラス
	 * @param fieldName フィールド名
	 * @return {@link Field}
	 * @throws NoSuchFieldException フィールドが見つからなかった場合
	 * @see Class#getDeclaredField(String)
	 */
	public static Field getDeclaredField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
		return clazz.getDeclaredField(fieldName);
	}
	
	/**
	 * このクラスに定義された{@link Field フィールド}をクラスファイルに定義された順番で取得する。
	 * 
	 * @param clazz 対象のクラス
	 * @return このクラスに定義されたフィールドの配列
	 * @throws NotFoundException {@link CtClass}が見つからなかった場合
	 * @throws NoSuchFieldException フィールドが見つからなかった場合
	 */
	public static Field[] getDeclaredFields(final Class<?> clazz) throws NotFoundException, NoSuchFieldException {
		try {
			final ClassPool pool = ClassPoolUtil.getClassPool(clazz);
			final CtClass ctClass = ClassPoolUtil.toCtClass(pool, clazz);
			final CtField[] ctFields = ctClass.getDeclaredFields();
			final int size = ctFields.length;
			final Field[] fields = new Field[size];
			for (int i = 0; i < size; ++i) {
				fields[i] = ClassUtil.getDeclaredField(clazz, ctFields[i].getName());
			}
			return fields;
		} finally {
			ClassPoolUtil.dispose();
		}
	}
	
	/**
	 * そのクラスに宣言されている {@link Method}を取得する。
	 * 
	 * @param clazz クラス
	 * @param methodName メソッド名
	 * @param argTypes 引数
	 * @return {@link Method} メソッド
	 * @throws NoSuchMethodException メソッドが存在しない場合
	 * @throws SecurityException セキュリティ違反が発生した場合
	 * @see Class#getDeclaredMethod(String, Class[])
	 */
	public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>[] argTypes)
			throws SecurityException, NoSuchMethodException {
		return clazz.getDeclaredMethod(methodName, argTypes);
	}
	
	/**
	 * {@link Field}を取得する。
	 * 
	 * @param clazz クラス
	 * @param fieldName フィールド名
	 * @return {@link Field}
	 * @throws NoSuchFieldException フィールドが見つからなかった場合
	 * @see Class#getField(String)
	 */
	public static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
		return clazz.getField(fieldName);
	}
	
	/**
	 * {@link Field}を取得する。ただし、例外はスローしない。
	 * 
	 * @param clazz クラス
	 * @param fieldName フィールド名
	 * @return {@link Field}
	 * @see Class#getField(String)
	 */
	public static Field getFieldNoException(Class<?> clazz, String fieldName) {
		try {
			return clazz.getField(fieldName);
		} catch (SecurityException e) {
			return null;
		} catch (NoSuchFieldException e) {
			return null;
		}
	}
	
	/**
	 * {@link Method}を取得する。
	 * 
	 * @param clazz クラス
	 * @param methodName メソッド名
	 * @param argTypes 引数
	 * @return {@link Method}
	 * @throws NoSuchMethodException メソッドがみつからなかった場合
	 * @throws SecurityException セキュリティ違反が発生した場合
	 * @see Class#getMethod(String, Class[])
	 */
	public static Method getMethod(Class<?> clazz, String methodName, Class<?>[] argTypes) throws SecurityException,
			NoSuchMethodException {
		return clazz.getMethod(methodName, argTypes);
	}
	
	/**
	 * パッケージ名を取得する。
	 * 
	 * @param clazz クラス
	 * @return パッケージ名
	 */
	public static String getPackageName(Class<?> clazz) {
		String fqcn = clazz.getName();
		int pos = fqcn.lastIndexOf('.');
		if (pos > 0) {
			return fqcn.substring(0, pos);
		}
		return null;
	}
	
	/**
	 * ラッパークラスをプリミティブクラスに変換する。
	 * 
	 * @param clazz クラス
	 * @return プリミティブクラス
	 */
	public static Class<?> getPrimitiveClass(Class<?> clazz) {
		return wrapperToPrimitiveMap.get(clazz);
	}
	
	/**
	 * ラッパークラスならプリミティブクラスに、 そうでなければそのままクラスを取得する。
	 * 
	 * @param clazz クラス
	 * @return {@link Class}
	 */
	public static Class<?> getPrimitiveClassIfWrapper(Class<?> clazz) {
		Class<?> ret = getPrimitiveClass(clazz);
		if (ret != null) {
			return ret;
		}
		return clazz;
	}
	
	/**
	 * クラス名をリソースパスに変換する。
	 * 
	 * @param clazz クラス
	 * @return リソースパス
	 * @see #getResourcePath(String)
	 */
	public static String getResourcePath(Class<?> clazz) {
		return getResourcePath(clazz.getName());
	}
	
	/**
	 * クラス名をリソースパスに変換する。
	 * 
	 * @param className クラス名
	 * @return リソースパス
	 */
	public static String getResourcePath(String className) {
		return StringUtils.replace(className, ".", "/") + ".class";
	}
	
	/**
	 * FQCNからパッケージ名を除いた名前を取得する。
	 * 
	 * @param clazz クラス
	 * @return FQCNからパッケージ名を除いた名前
	 * @see #getShortClassName(String)
	 */
	public static String getShortClassName(Class<?> clazz) {
		return getShortClassName(clazz.getName());
	}
	
	/**
	 * FQCNからパッケージ名を除いた名前を取得する。
	 * 
	 * @param className クラス名
	 * @return FQCNからパッケージ名を除いた名前
	 */
	public static String getShortClassName(String className) {
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
	 */
	public static String getSimpleClassName(final Class<?> clazz) {
		if (clazz.isArray()) {
			return getSimpleClassName(clazz.getComponentType()) + "[]";
		}
		return clazz.getName();
	}
	
	/**
	 * プリミティブクラスをラッパークラスに変換する。
	 * 
	 * @param clazz クラス
	 * @return {@link Class}
	 */
	public static Class<?> getWrapperClass(Class<?> clazz) {
		return primitiveToWrapperMap.get(clazz);
	}
	
	/**
	 * プリミティブの場合はラッパークラス、そうでない場合はもとのクラスを取得する。
	 * 
	 * @param clazz クラス
	 * @return {@link Class}
	 */
	public static Class<?> getWrapperClassIfPrimitive(Class<?> clazz) {
		Class<?> ret = getWrapperClass(clazz);
		if (ret != null) {
			return ret;
		}
		return clazz;
	}
	
	/**
	 * 代入可能かどうかをチェックする。
	 * 
	 * @param toClass 代入先のクラス
	 * @param fromClass 代入元のクラス
	 * @return 代入可能かどうか
	 * @see Class#isAssignableFrom(Class)
	 */
	public static boolean isAssignableFrom(Class<?> toClass, Class<?> fromClass) {
		if (toClass == Object.class && !fromClass.isPrimitive()) {
			return true;
		}
		if (toClass.isPrimitive()) {
			fromClass = getPrimitiveClassIfWrapper(fromClass);
		}
		return toClass.isAssignableFrom(fromClass);
	}
	
	/**
	 * 新しいインスタンスを作成する。
	 * 
	 * @param clazz クラス
	 * @return 新しいインスタンス
	 * @throws InstantiationException この Class が abstract クラス、インタフェース、配列クラス、プリミティブ型、
	 * または void を表す場合、クラスが null コンストラクタを保持しない場合、あるいはインスタンスの生成がほかの理由で失敗した場合
	 * @throws IllegalAccessException コンストラクタにアクセスできない場合
	 * @see Class#newInstance()
	 */
	public static Object newInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException {
		return clazz.newInstance();
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
	 * @see #newInstance(Class)
	 */
	public static Object newInstance(String className) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return newInstance(forName(className));
	}
	
	/**
	 * FQCNをパッケージ名とFQCNからパッケージ名を除いた名前に分割する。
	 * 
	 * @param className クラス名
	 * @return パッケージ名とFQCNからパッケージ名を除いた名前
	 */
	public static String[] splitPackageAndShortClassName(String className) {
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
	
	private ClassUtil() {
	}
}
