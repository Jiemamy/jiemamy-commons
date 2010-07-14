/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2009/01/29
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
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.Validate;

import org.jiemamy.utils.JmStringUtil;

/**
 * リフレクションを使用するユーティリティクラス。
 * 
 * @author daisuke
 */
public final class ReflectionUtil {
	
	/** setterの接頭句 */
	public static final String SET = "set";
	
	/** getterの接頭句 */
	public static final String GET = "get";
	
	/** boolean getterの接頭句 */
	public static final String IS = "is";
	

	/**
	 * アクセサメソッドから、フィールド名を割り出す。
	 * 
	 * @param method アクセサメソッド
	 * @return フィールド名
	 * @throws IllegalArgumentException メソッドがアクセサではない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static String convertAccessorToFieldName(Method method) {
		Validate.notNull(method);
		if (isAccessor(method) == false) {
			throw new IllegalArgumentException();
		}
		Class<?> returnType = method.getReturnType();
		String name = method.getName();
		if ((returnType == boolean.class || returnType == Boolean.class) && name.startsWith(IS)) {
			return JmStringUtil.toUnCapital(name.substring(2));
		}
		return JmStringUtil.toUnCapital(name.substring(3));
	}
	
	/**
	 * フィールド名からアクセサ名を割り出す。
	 * 
	 * @param fieldName フィールド名
	 * @param prefix 接頭辞("set", "get", "is" 等)
	 * @return アクセサ名
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static String convertFieldNameToAccessorName(String fieldName, String prefix) {
		Validate.notNull(fieldName);
		Validate.notNull(prefix);
		return prefix + JmStringUtil.toCapital(fieldName);
	}
	
	/**
	 * フィールド名からsetter名を割り出す。
	 * 
	 * @param fieldName フィールド名
	 * @return setter名
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static String convertFieldNameToSetterName(String fieldName) {
		Validate.notNull(fieldName);
		return convertFieldNameToAccessorName(fieldName, SET);
	}
	
	/**
	 * フィールドからアクセサ名を割り出す。
	 * 
	 * @param field フィールド
	 * @param prefix 接頭辞("set", "get", "is" 等)
	 * @return アクセサ名
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static String convertFieldToAccessorName(Field field, String prefix) {
		Validate.notNull(field);
		Validate.notNull(prefix);
		return convertFieldNameToAccessorName(field.getName(), prefix);
	}
	
	/**
	 * フィールドからgetter名を割り出す。
	 * 
	 * @param field フィールド
	 * @return getter名
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static String convertFieldToGetterName(Field field) {
		Validate.notNull(field);
		if (field.getType() == boolean.class || field.getType() == Boolean.class) {
			return convertFieldToAccessorName(field, IS);
		}
		return convertFieldToAccessorName(field, GET);
	}
	
	/**
	 * フィールドからsetter名を割り出す。
	 * 
	 * @param field フィールド
	 * @return setter名
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static String convertFieldToSetterName(Field field) {
		Validate.notNull(field);
		return convertFieldToAccessorName(field, SET);
	}
	
	/**
	 * 現在のスレッドのコンテキストクラスローダを使って指定された文字列名を持つ型の{@link Class}オブジェクトを取得する。
	 * 
	 * @param <T> {@link Class}オブジェクトが表すクラス
	 * @param className 要求するクラスの完全修飾名
	 * @return 指定された名前を持つクラスの{@link Class}オブジェクト
	 * @throws ClassNotFoundException クラスが見つからなかった場合
	 * @see Class#forName(String)
	 */
	public static <T>Class<T> forName(String className) throws ClassNotFoundException {
		return forName(className, Thread.currentThread().getContextClassLoader());
	}
	
	/**
	 * 指定されたクラスローダを使って指定された文字列名を持つ型の{@link Class}オブジェクトを取得する。
	 * 
	 * @param <T> {@link Class}オブジェクトが表すクラス
	 * @param className 要求するクラスの完全修飾名
	 * @param loader クラスのロード元である必要があるクラスローダ
	 * @return 指定された名前を持つクラスの{@link Class}オブジェクト
	 * @throws ClassNotFoundException クラスが見つからなかった場合
	 * @see Class#forName(String, boolean, ClassLoader)
	 */
	@SuppressWarnings("unchecked")
	public static <T>Class<T> forName(String className, ClassLoader loader) throws ClassNotFoundException {
		return (Class<T>) Class.forName(className, true, loader);
	}
	
	/**
	 * 現在のスレッドのコンテキストクラスローダを使って、 指定された文字列名を持つ型の{@link Class}オブジェクトを取得する。
	 * 
	 * @param <T> {@link Class}オブジェクトが表すクラス
	 * @param className 要求するクラスの完全修飾名
	 * @return 指定された名前を持つクラスの{@link Class}オブジェクト.  クラスが見つからなかった場合は{@code null}
	 * @see Class#forName(String)
	 */
	public static <T>Class<T> forNameNoException(String className) {
		return forNameNoException(className, Thread.currentThread().getContextClassLoader());
	}
	
	/**
	 * 指定されたクラスローダを使って、 指定された文字列名を持つ型の{@link Class}オブジェクトを取得する。
	 * 
	 * @param <T> {@link Class}オブジェクトが表すクラス
	 * @param className 要求するクラスの完全修飾名
	 * @param loader クラスのロード元である必要があるクラスローダ
	 * @return 指定された名前を持つクラスの{@link Class}オブジェクト.  クラスが見つからなかった場合は{@code null}
	 * @see Class#forName(String)
	 */
	@SuppressWarnings("unchecked")
	public static <T>Class<T> forNameNoException(String className, ClassLoader loader) {
		try {
			return (Class<T>) Class.forName(className, true, loader);
		} catch (Throwable ignore) {
			return null;
		}
	}
	
	/**
	 * {@link Class}オブジェクトが表すクラスまたはインタフェースの指定された宣言フィールドをリフレクトする
	 * {@link Field}オブジェクトを取得する。ただし例外をスローしない。
	 * 
	 * @param clazz クラスの{@link Class}オブジェクト
	 * @param name フィールド名
	 * @return {@code name}で指定されたこのクラスの{@link Field}オブジェクト. 例外が発生した場合は{@code null}
	 * @see Class#getDeclaredField(String)
	 */
	public static Field getDeclaredFieldNoException(Class<?> clazz, String name) {
		try {
			return clazz.getDeclaredField(name);
		} catch (SecurityException e) {
			return null;
		} catch (NoSuchFieldException e) {
			return null;
		}
	}
	
	/**
	 * パラメタ化されたコレクションの要素型を取得する。
	 * 
	 * @param parameterizedCollection パラメタ化されたコレクションの型
	 * @return パラメタ化されたコレクションの要素型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> getElementTypeOfCollection(Type parameterizedCollection) {
		Validate.notNull(parameterizedCollection);
		return GenericUtil.getRawClass(GenericUtil.getElementTypeOfCollection(parameterizedCollection));
	}
	
	/**
	 * 指定されたフィールドのパラメタ化されたコレクションの要素型を取得する。
	 * 
	 * @param field フィールド
	 * @return 指定されたフィールドのパラメタ化されたコレクションの要素型 
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> getElementTypeOfCollectionFromFieldType(Field field) {
		Validate.notNull(field);
		Type type = field.getGenericType();
		return getElementTypeOfCollection(type);
	}
	
	/**
	 * 指定されたメソッドの引数型として宣言されているパラメタ化されたコレクションの要素型を取得する。
	 * 
	 * @param method メソッド
	 * @param parameterPosition パラメタ化されたコレクションが宣言されているメソッド引数の位置
	 * @return 指定されたメソッドの引数型として宣言されているパラメタ化されたコレクションの要素型
	 * @throws IllegalArgumentException 引数{@code method}に{@code null}を与えた場合
	 * @throws IndexOutOfBoundsException パラメタ化されたコレクションが宣言されているメソッド引数の数と parameterPositionが不整合の場合
	 */
	public static Class<?> getElementTypeOfCollectionFromParameterType(Method method, int parameterPosition) {
		Validate.notNull(method);
		Type[] parameterTypes = method.getGenericParameterTypes();
		return getElementTypeOfCollection(parameterTypes[parameterPosition]);
	}
	
	/**
	 * 指定されたメソッドの戻り値型として宣言されているパラメタ化されたコレクションの要素型を取得する。
	 * 
	 * @param method メソッド
	 * @return 指定されたメソッドの戻り値型として宣言されているパラメタ化されたコレクションの要素型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> getElementTypeOfCollectionFromReturnType(Method method) {
		Validate.notNull(method);
		return getElementTypeOfCollection(method.getGenericReturnType());
	}
	
	/**
	 * パラメタ化されたリストの要素型を取得する。
	 * 
	 * @param parameterizedList パラメタ化されたリストの型
	 * @return パラメタ化されたリストの要素型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> getElementTypeOfList(Type parameterizedList) {
		Validate.notNull(parameterizedList);
		return GenericUtil.getRawClass(GenericUtil.getElementTypeOfList(parameterizedList));
	}
	
	/**
	 * 指定されたフィールドのパラメタ化されたリストの要素型を取得する。
	 * 
	 * @param field フィールド
	 * @return 指定されたフィールドのパラメタ化されたリストの要素型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> getElementTypeOfListFromFieldType(Field field) {
		Validate.notNull(field);
		Type type = field.getGenericType();
		return getElementTypeOfList(type);
	}
	
	/**
	 * 指定されたメソッドの引数型として宣言されているパラメタ化されたリストの要素型を取得する。
	 * 
	 * @param method メソッド
	 * @param parameterPosition パラメタ化されたリストが宣言されているメソッド引数の位置
	 * @return 指定されたメソッドの引数型として宣言されているパラメタ化されたリストの要素型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws IndexOutOfBoundsException パラメタ化されたコレクションが宣言されているメソッド引数の数と parameterPositionが不整合の場合
	 */
	public static Class<?> getElementTypeOfListFromParameterType(Method method, int parameterPosition) {
		Validate.notNull(method);
		Type[] parameterTypes = method.getGenericParameterTypes();
		return getElementTypeOfList(parameterTypes[parameterPosition]);
	}
	
	/**
	 * 指定されたメソッドの戻り値型として宣言されているパラメタ化されたリストの要素型を取得する。
	 * 
	 * @param method メソッド
	 * @return 指定されたメソッドの戻り値型として宣言されているパラメタ化されたリストの要素型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> getElementTypeOfListFromReturnType(Method method) {
		Validate.notNull(method);
		return getElementTypeOfList(method.getGenericReturnType());
	}
	
	/**
	 * パラメタ化されたセットの要素型を取得する。
	 * 
	 * @param parameterizedSet パラメタ化されたセットの型
	 * @return パラメタ化されたセットの要素型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> getElementTypeOfSet(Type parameterizedSet) {
		Validate.notNull(parameterizedSet);
		return GenericUtil.getRawClass(GenericUtil.getElementTypeOfSet(parameterizedSet));
	}
	
	/**
	 * 指定されたフィールドのパラメタ化されたセットの要素型を取得する。
	 * 
	 * @param field フィールド
	 * @return 指定されたフィールドのパラメタ化されたセットの要素型
	 * @throws IndexOutOfBoundsException パラメタ化されたコレクションが宣言されているメソッド引数の数と parameterPositionが不整合の場合
	 */
	public static Class<?> getElementTypeOfSetFromFieldType(Field field) {
		Validate.notNull(field);
		Type type = field.getGenericType();
		return getElementTypeOfSet(type);
	}
	
	/**
	 * 指定されたメソッドの引数型として宣言されているパラメタ化されたセットの要素型を取得する。
	 * 
	 * @param method メソッド
	 * @param parameterPosition パラメタ化されたセットが宣言されているメソッド引数の位置
	 * @return 指定されたメソッドの引数型として宣言されているパラメタ化されたセットの要素型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> getElementTypeOfSetFromParameterType(Method method, int parameterPosition) {
		Validate.notNull(method);
		Type[] parameterTypes = method.getGenericParameterTypes();
		return getElementTypeOfSet(parameterTypes[parameterPosition]);
	}
	
	/**
	 * 指定されたメソッドの戻り値型として宣言されているパラメタ化されたセットの要素型を取得する。
	 * 
	 * @param method メソッド
	 * @return 指定されたメソッドの戻り値型として宣言されているパラメタ化されたセットの要素型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> getElementTypeOfSetFromReturnType(Method method) {
		Validate.notNull(method);
		return getElementTypeOfSet(method.getGenericReturnType());
	}
	
	/**
	 * メソッドがアクセサメソッドかどうかを調べる。
	 * 
	 * @param method メソッド
	 * @return アクセサであれば{@code true}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static boolean isAccessor(Method method) {
		return isGetter(method) || isSetter(method);
	}
	
	/**
	 * メソッドがgetterかどうか調べる。
	 * 
	 * @param method メソッド
	 * @return getterであれば{@code true}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static boolean isGetter(Method method) {
		Validate.notNull(method);
		Class<?>[] parameterTypes = method.getParameterTypes();
		if (parameterTypes.length != 0) {
			return false;
		}
		Class<?> returnType = method.getReturnType();
		String name = method.getName();
		if (returnType == void.class || returnType == Void.class) {
			return false;
		} else if (returnType == boolean.class || returnType == Boolean.class) {
			boolean result =
					name.startsWith(IS) && name.length() > 2 && CharUtils.isAsciiAlphaUpper(name.toCharArray()[2]);
			if (result == true) {
				return true;
			}
		}
		return name.startsWith(GET) && name.length() > 3 && CharUtils.isAsciiAlphaUpper(name.toCharArray()[3]);
	}
	
	/**
	 * メソッドがsetterかどうか調べる。
	 * 
	 * @param method メソッド
	 * @return setterであれば{@code true}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static boolean isSetter(Method method) {
		Validate.notNull(method);
		Class<?>[] parameterTypes = method.getParameterTypes();
		if (parameterTypes.length != 1) {
			return false;
		}
		Class<?> returnType = method.getReturnType();
		if (returnType != void.class && returnType != Void.class) {
			return false;
		}
		String name = method.getName();
		return name.startsWith(SET) && name.length() > 3 && CharUtils.isAsciiAlphaUpper(name.toCharArray()[3]);
	}
	
	private ReflectionUtil() {
	}
}
