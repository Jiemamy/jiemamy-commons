/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
package org.jiemamy.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.apache.commons.lang.CharUtils;

/**
 * リフレクションを使用するユーティリティクラス。
 * 
 * @author daisuke
 */
public class ReflectionUtil {
	
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
	 */
	public static String convertAccessorToFieldName(Method method) {
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
	 */
	public static String convertFieldNameToAccessorName(String fieldName, String prefix) {
		return prefix + JmStringUtil.toCapital(fieldName);
	}
	
	/**
	 * フィールド名からsetter名を割り出す。
	 * 
	 * @param fieldName フィールド名
	 * @return setter名
	 */
	public static String convertFieldNameToSetterName(String fieldName) {
		return convertFieldNameToAccessorName(fieldName, SET);
	}
	
	/**
	 * フィールドからアクセサ名を割り出す。
	 * 
	 * @param field フィールド
	 * @param prefix 接頭辞("set", "get", "is" 等)
	 * @return アクセサ名
	 */
	public static String convertFieldToAccessorName(Field field, String prefix) {
		return convertFieldNameToAccessorName(field.getName(), prefix);
	}
	
	/**
	 * フィールドからgetter名を割り出す。
	 * 
	 * @param field フィールド
	 * @return getter名
	 */
	public static String convertFieldToGetterName(Field field) {
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
	 */
	public static String convertFieldToSetterName(Field field) {
		return convertFieldToAccessorName(field, SET);
	}
	
	/**
	 * 現在のスレッドのコンテキストクラスローダを使って指定された文字列名を持つクラスまたはインタフェースに
	 * 関連付けられた{@link Class}オブジェクトを取得する。
	 * 
	 * @param <T> {@link Class}オブジェクトが表すクラス
	 * @param className 要求するクラスの完全修飾名
	 * @return 指定された名前を持つクラスの{@link Class}オブジェクト
	 * @throws ClassNotFoundException クラスが見つからなかった場合
	 * @see Class#forName(String)
	 */
	public static <T>Class<T> forName(final String className) throws ClassNotFoundException {
		return forName(className, Thread.currentThread().getContextClassLoader());
	}
	
	/**
	 * 指定されたクラスローダを使って指定された文字列名を持つクラスまたはインタフェースに
	 * 関連付けられた{@link Class}オブジェクトを取得する。
	 * 
	 * @param <T> {@link Class}オブジェクトが表すクラス
	 * @param className 要求するクラスの完全修飾名
	 * @param loader クラスのロード元である必要があるクラスローダ
	 * @return 指定された名前を持つクラスの{@link Class}オブジェクト
	 * @throws ClassNotFoundException クラスが見つからなかった場合
	 * @see Class#forName(String, boolean, ClassLoader)
	 */
	@SuppressWarnings("unchecked")
	public static <T>Class<T> forName(final String className, final ClassLoader loader) throws ClassNotFoundException {
		return (Class<T>) Class.forName(className, true, loader);
	}
	
	/**
	 * 現在のスレッドのコンテキストクラスローダを使って、 指定された文字列名を持つクラスまたはインタフェースに
	 * 関連付けられた{@link Class}オブジェクトを取得する。
	 * <p>
	 * クラスが見つからなかった場合は{@code null}を取得する。
	 * </p>
	 * 
	 * @param <T> {@link Class}オブジェクトが表すクラス
	 * @param className 要求するクラスの完全修飾名
	 * @return 指定された名前を持つクラスの{@link Class}オブジェクト
	 * @see Class#forName(String)
	 */
	public static <T>Class<T> forNameNoException(final String className) {
		return forNameNoException(className, Thread.currentThread().getContextClassLoader());
	}
	
	/**
	 * 指定されたクラスローダを使って、 指定された文字列名を持つクラスまたはインタフェースに
	 * 関連付けられた{@link Class}オブジェクトを取得する。
	 * <p>
	 * クラスが見つからなかった場合は{@code null}を取得する。
	 * </p>
	 * 
	 * @param <T> {@link Class}オブジェクトが表すクラス
	 * @param className 要求するクラスの完全修飾名
	 * @param loader クラスのロード元である必要があるクラスローダ
	 * @return 指定された名前を持つクラスの{@link Class}オブジェクト
	 * @see Class#forName(String)
	 */
	@SuppressWarnings("unchecked")
	public static <T>Class<T> forNameNoException(final String className, final ClassLoader loader) {
		try {
			return (Class<T>) Class.forName(className, true, loader);
		} catch (final Throwable ignore) {
			return null;
		}
	}
	
	/**
	 * {@link Class}オブジェクトが表すクラスの指定された{@code public}コンストラクタを
	 * リフレクトする{@link Constructor}オブジェクトを取得する。
	 * 
	 * @param <T> {@link Class}オブジェクトが表すクラス
	 * @param clazz クラスの{@link Class}オブジェクト
	 * @param argTypes パラメータ配列
	 * @return 指定された{@code argTypes}と一致する{@code public}コンストラクタの{@link Constructor}オブジェクト
	 * @throws NoSuchMethodException メソッドが見つからなかった場合
	 * @throws SecurityException セキュリティ違反が発生した場合
	 * @see Class#getConstructor(Class[])
	 */
	public static <T>Constructor<T> getConstructor(final Class<T> clazz, final Class<?>... argTypes)
			throws SecurityException, NoSuchMethodException {
		return clazz.getConstructor(argTypes);
	}
	
	/**
	 * {@link Class}オブジェクトが表すクラスまたはインタフェースの指定されたコンストラクタを
	 * リフレクトする{@link Constructor}オブジェクトを取得する。
	 * 
	 * @param <T> {@link Class}オブジェクトが表すクラス
	 * @param clazz クラスの{@link Class}オブジェクト
	 * @param argTypes パラメータ配列
	 * @return 指定された{@code argTypes}と一致するコンストラクタの{@link Constructor}オブジェクト
	 * @throws NoSuchMethodException メソッドが見つからなかった場合
	 * @throws SecurityException セキュリティ違反が発生した場合
	 * @see Class#getDeclaredConstructor(Class[])
	 */
	public static <T>Constructor<T> getDeclaredConstructor(final Class<T> clazz, final Class<?>... argTypes)
			throws SecurityException, NoSuchMethodException {
		return clazz.getDeclaredConstructor(argTypes);
	}
	
	/**
	 * {@link Class}オブジェクトが表すクラスまたはインタフェースの指定された宣言フィールドをリフレクトする
	 * {@link Field}オブジェクトを取得する。
	 * 
	 * @param clazz クラスの{@link Class}オブジェクト
	 * @param name フィールド名
	 * @return {@code name}で指定されたこのクラスの{@link Field}オブジェクト
	 * @throws NoSuchFieldException メソッドが見つからなかった場合
	 * @throws SecurityException セキュリティ違反が発生した場合 
	 * @see Class#getDeclaredField(String)
	 */
	public static Field getDeclaredField(final Class<?> clazz, final String name) throws SecurityException,
			NoSuchFieldException {
		return clazz.getDeclaredField(name);
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
	public static Field getDeclaredFieldNoException(final Class<?> clazz, final String name) {
		try {
			return clazz.getDeclaredField(name);
		} catch (SecurityException e) {
			return null;
		} catch (NoSuchFieldException e) {
			return null;
		}
	}
	
	/**
	 * {@link Class}オブジェクトが表すクラスまたはインタフェースの指定されたメンバメソッドを
	 * リフレクトする{@link Method}オブジェクトを取得する。
	 * 
	 * @param clazz クラスの{@link Class}オブジェクト
	 * @param name メソッドの名前
	 * @param argTypes パラメータのリスト
	 * @return 指定された{@code name}および{@code argTypes}と一致する{@link Method}オブジェクト
	 * @throws NoSuchMethodException メソッドが見つからない場合
	 * @throws SecurityException セキュリティ違反が発生した場合
	 * @see Class#getDeclaredMethod(String, Class[])
	 */
	public static Method getDeclaredMethod(final Class<?> clazz, final String name, final Class<?>... argTypes)
			throws SecurityException, NoSuchMethodException {
		return clazz.getDeclaredMethod(name, argTypes);
	}
	
	/**
	 * パラメタ化されたコレクションの要素型を取得する。
	 * 
	 * @param parameterizedCollection パラメタ化されたコレクションの型
	 * @return パラメタ化されたコレクションの要素型
	 */
	public static Class<?> getElementTypeOfCollection(final Type parameterizedCollection) {
		return GenericUtil.getRawClass(GenericUtil.getElementTypeOfCollection(parameterizedCollection));
	}
	
	/**
	 * 指定されたフィールドのパラメタ化されたコレクションの要素型を取得する。
	 * 
	 * @param field フィールド
	 * @return 指定されたフィールドのパラメタ化されたコレクションの要素型 since 2.4.18
	 * since 2.4.18
	 */
	public static Class<?> getElementTypeOfCollectionFromFieldType(final Field field) {
		final Type type = field.getGenericType();
		return getElementTypeOfCollection(type);
	}
	
	/**
	 * 指定されたメソッドの引数型として宣言されているパラメタ化されたコレクションの要素型を取得する。
	 * 
	 * @param method メソッド
	 * @param parameterPosition パラメタ化されたコレクションが宣言されているメソッド引数の位置
	 * @return 指定されたメソッドの引数型として宣言されているパラメタ化されたコレクションの要素型
	 */
	public static Class<?> getElementTypeOfCollectionFromParameterType(final Method method, final int parameterPosition) {
		final Type[] parameterTypes = method.getGenericParameterTypes();
		return getElementTypeOfCollection(parameterTypes[parameterPosition]);
	}
	
	/**
	 * 指定されたメソッドの戻り値型として宣言されているパラメタ化されたコレクションの要素型を取得する。
	 * 
	 * @param method メソッド
	 * @return 指定されたメソッドの戻り値型として宣言されているパラメタ化されたコレクションの要素型
	 */
	public static Class<?> getElementTypeOfCollectionFromReturnType(final Method method) {
		return getElementTypeOfCollection(method.getGenericReturnType());
	}
	
	/**
	 * パラメタ化されたリストの要素型を取得する。
	 * 
	 * @param parameterizedList パラメタ化されたリストの型
	 * @return パラメタ化されたリストの要素型
	 */
	public static Class<?> getElementTypeOfList(final Type parameterizedList) {
		return GenericUtil.getRawClass(GenericUtil.getElementTypeOfList(parameterizedList));
	}
	
	/**
	 * 指定されたフィールドのパラメタ化されたリストの要素型を取得する。
	 * 
	 * @param field フィールド
	 * @return 指定されたフィールドのパラメタ化されたリストの要素型
	 */
	public static Class<?> getElementTypeOfListFromFieldType(final Field field) {
		final Type type = field.getGenericType();
		return getElementTypeOfList(type);
	}
	
	/**
	 * 指定されたメソッドの引数型として宣言されているパラメタ化されたリストの要素型を取得する。
	 * 
	 * @param method メソッド
	 * @param parameterPosition パラメタ化されたリストが宣言されているメソッド引数の位置
	 * @return 指定されたメソッドの引数型として宣言されているパラメタ化されたリストの要素型
	 */
	public static Class<?> getElementTypeOfListFromParameterType(final Method method, final int parameterPosition) {
		final Type[] parameterTypes = method.getGenericParameterTypes();
		return getElementTypeOfList(parameterTypes[parameterPosition]);
	}
	
	/**
	 * 指定されたメソッドの戻り値型として宣言されているパラメタ化されたリストの要素型を取得する。
	 * 
	 * @param method メソッド
	 * @return 指定されたメソッドの戻り値型として宣言されているパラメタ化されたリストの要素型
	 */
	public static Class<?> getElementTypeOfListFromReturnType(final Method method) {
		return getElementTypeOfList(method.getGenericReturnType());
	}
	
	/**
	 * パラメタ化されたセットの要素型を取得する。
	 * 
	 * @param parameterizedSet パラメタ化されたセットの型
	 * @return パラメタ化されたセットの要素型
	 */
	public static Class<?> getElementTypeOfSet(final Type parameterizedSet) {
		return GenericUtil.getRawClass(GenericUtil.getElementTypeOfSet(parameterizedSet));
	}
	
	/**
	 * 指定されたフィールドのパラメタ化されたセットの要素型を取得する。
	 * 
	 * @param field フィールド
	 * @return 指定されたフィールドのパラメタ化されたセットの要素型 since 2.4.18
	 */
	public static Class<?> getElementTypeOfSetFromFieldType(final Field field) {
		final Type type = field.getGenericType();
		return getElementTypeOfSet(type);
	}
	
	/**
	 * 指定されたメソッドの引数型として宣言されているパラメタ化されたセットの要素型を取得する。
	 * 
	 * @param method メソッド
	 * @param parameterPosition パラメタ化されたセットが宣言されているメソッド引数の位置
	 * @return 指定されたメソッドの引数型として宣言されているパラメタ化されたセットの要素型
	 */
	public static Class<?> getElementTypeOfSetFromParameterType(final Method method, final int parameterPosition) {
		final Type[] parameterTypes = method.getGenericParameterTypes();
		return getElementTypeOfSet(parameterTypes[parameterPosition]);
	}
	
	/**
	 * 指定されたメソッドの戻り値型として宣言されているパラメタ化されたセットの要素型を取得する。
	 * 
	 * @param method メソッド
	 * @return 指定されたメソッドの戻り値型として宣言されているパラメタ化されたセットの要素型
	 */
	public static Class<?> getElementTypeOfSetFromReturnType(final Method method) {
		return getElementTypeOfSet(method.getGenericReturnType());
	}
	
	/**
	 * {@link Class}オブジェクトが表すクラスまたはインタフェースの指定された{@code public}メンバフィールドをリフレクトする{@link Field}オブジェクトを取得する。
	 * 
	 * @param clazz クラスの{@link Class}オブジェクト
	 * @param name フィールド名
	 * @return {@code name}で指定されたこのクラスの{@link Field}オブジェクト
	 * @throws NoSuchFieldException フィールドが見つからなかった場合
	 * @throws SecurityException セキュリティ違反が発生した場合
	 * @see Class#getField(String)
	 */
	public static Field getField(final Class<?> clazz, final String name) throws SecurityException,
			NoSuchFieldException {
		return clazz.getField(name);
	}
	
	/**
	 * {@link Class}オブジェクトが表すクラスまたはインタフェースの指定された{@code public}メンバメソッドを
	 * リフレクトする{@link Method}オブジェクトを取得する。
	 * 
	 * @param clazz	クラスの{@link Class}オブジェクト
	 * @param name メソッドの名前
	 * @param argTypes パラメータのリスト
	 * @return 指定された{@code name}および{@code argTypes}と一致する{@link Method}オブジェクト
	 * @throws NoSuchMethodException メソッドが見つからなかった場合
	 * @throws SecurityException セキュリティ違反が発生した場合
	 * @see Class#getMethod(String, Class[])
	 */
	public static Method getMethod(final Class<?> clazz, final String name, final Class<?>... argTypes)
			throws SecurityException, NoSuchMethodException {
		return clazz.getMethod(name, argTypes);
	}
	
	/**
	 * 指定されたオブジェクトについて、{@link Field}によって表される{@code static}フィールドの値を取得する。
	 * 
	 * @param <T> フィールドの型
	 * @param field フィールド
	 * @return {@code static}フィールドで表現される値
	 * @throws IllegalAccessException 基本となるフィールドにアクセスできない場合
	 * @see Field#get(Object)
	 */
	@SuppressWarnings("unchecked")
	public static <T>T getStaticValue(final Field field) throws IllegalAccessException {
		return (T) getValue(field, null);
	}
	
	/**
	 * 指定されたオブジェクトについて、{@link Field}によって表されるフィールドの値を取得する。
	 * 
	 * @param <T> フィールドの型
	 * @param field フィールド
	 * @param target 表現されるフィールド値の抽出元オブジェクト
	 * @return オブジェクト{@code obj}内で表現される値
	 * @throws IllegalAccessException 基本となるフィールドにアクセスできない場合
	 * @see Field#get(Object)
	 */
	@SuppressWarnings("unchecked")
	public static <T>T getValue(final Field field, final Object target) throws IllegalAccessException {
		return (T) field.get(target);
	}
	
	/**
	 * {@link Method}オブジェクトによって表される基本となるメソッドを、指定したオブジェクトに対して指定したパラメータで呼び出す。
	 * 
	 * @param <T> メソッドの戻り値の型
	 * @param method メソッド
	 * @param target 基本となるメソッドの呼び出し元のオブジェクト
	 * @param args メソッド呼び出しに使用される引数
	 * @return このオブジェクトが表すメソッドを、パラメータ{@code args}を使用して{@code obj}にディスパッチした結果
	 * @throws InvocationTargetException 基本となるメソッドが例外をスローする場合
	 * @throws IllegalAccessException この{@link Method}オブジェクトがJava言語アクセス制御を実施し基本となるメソッドにアクセスできない場合
	 * @throws IllegalArgumentException 不正な引数、または不適切な引数をメソッドに渡した場合
	 * @see Method#invoke(Object, Object[])
	 */
	@SuppressWarnings("unchecked")
	public static <T>T invoke(final Method method, final Object target, final Object... args)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		return (T) method.invoke(target, args);
	}
	
	/**
	 * {@link Method}オブジェクトによって表される基本となる{@code static}メソッドを指定したパラメータで呼び出す。
	 * 
	 * @param <T> メソッドの戻り値の型
	 * @param method メソッド
	 * @param args メソッド呼び出しに使用される引数
	 * @return このオブジェクトが表す{@code static}メソッドを、パラメータ{@code args}を使用してディスパッチした結果
	 * @throws InvocationTargetException 基本となるメソッドが例外をスローする場合
	 * @throws IllegalAccessException この{@link Method}オブジェクトがJava言語アクセス制御を実施し基本となるメソッドにアクセスできない場合
	 * @throws IllegalArgumentException 不正な引数、または不適切な引数をメソッドに渡した場合
	 * @see Method#invoke(Object, Object[])
	 */
	@SuppressWarnings("unchecked")
	public static <T>T invokeStatic(final Method method, final Object... args) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		return (T) invoke(method, null, args);
	}
	
	/**
	 * メソッドがアクセサメソッドかどうかを調べる。
	 * 
	 * @param method メソッド
	 * @return アクセサであれば{@code true}
	 */
	public static boolean isAccessor(Method method) {
		return isGetter(method) || isSetter(method);
	}
	
	/**
	 * メソッドがgetterかどうか調べる。
	 * 
	 * @param method メソッド
	 * @return getterであれば{@code true}
	 */
	public static boolean isGetter(Method method) {
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
	 */
	public static boolean isSetter(Method method) {
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
	
	/**
	 * 指定されたクラスのデフォルトコンストラクタで、クラスの新しいインスタンスを作成および初期化する。
	 * 
	 * @param <T> {@link Class}オブジェクトが表すクラス
	 * @param clazz クラスを表す{@link Class}オブジェクト
	 * @return このオブジェクトが表すコンストラクタを呼び出すことで作成される新規オブジェクト
	 * @throws InstantiationException 基本となるコンストラクタを宣言するクラスが{@code abstract}クラスを表す場合
	 * @throws IllegalAccessException 実パラメータ数と仮パラメータ数が異なる場合、 
	 * プリミティブ引数のラップ解除変換が失敗した場合、 またはラップ解除後、
	 * メソッド呼び出し変換によってパラメータ値を対応する仮パラメータ型に変換できない場合、
	 * このコンストラクタが列挙型に関連している場合
	 * @see Constructor#newInstance(Object[])
	 */
	public static <T>T newInstance(final Class<T> clazz) throws InstantiationException, IllegalAccessException {
		return clazz.newInstance();
	}
	
	/**
	 * 指定された初期化パラメータで、コンストラクタの宣言クラスの新しいインスタンスを作成および初期化する。
	 * 
	 * @param <T> コンストラクタの宣言クラス
	 * @param constructor コンストラクタ
	 * @param args コンストラクタ呼び出しに引数として渡すオブジェクトの配列
	 * @return コンストラクタを呼び出すことで作成される新規オブジェクト
	 * @throws InstantiationException 基本となるコンストラクタを宣言するクラスが{@code abstract}クラスを表す場合
	 * @throws IllegalAccessException 実パラメータ数と仮パラメータ数が異なる場合、
	 * 	プリミティブ引数のラップ解除変換が失敗した場合、 またはラップ解除後、
	 *  メソッド呼び出し変換によってパラメータ値を対応する仮パラメータ型に変換できない場合、
	 *  このコンストラクタが列挙型に関連している場合
	 * @throws InvocationTargetException 基本となるメソッドが例外をスローする場合
	 * @throws IllegalArgumentException 不正な引数、または不適切な引数をメソッドに渡した場合
	 * @see Constructor#newInstance(Object[])
	 */
	public static <T>T newInstance(final Constructor<T> constructor, final Object... args)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return constructor.newInstance(args);
	}
	
	/**
	 * {@link Field}オブジェクトによって表される{@code static}フィールドを、指定された新しい値に設定する。
	 * 
	 * @param field フィールド
	 * @param value {@code static}フィールドの新しい値
	 * @throws IllegalAccessException 基本となるフィールドにアクセスできない場合
	 * @see Field#set(Object, Object)
	 */
	public static void setStaticValue(final Field field, final Object value) throws IllegalAccessException {
		setValue(field, null, value);
	}
	
	/**
	 * {@link Field}オブジェクトによって表される指定されたオブジェクト引数のフィールドを、指定された新しい値に設定する。
	 * 
	 * @param field フィールド
	 * @param target フィールドを変更するオブジェクト
	 * @param value 変更中の{@code target}の新しいフィールド値
	 * @throws IllegalAccessException 基本となるフィールドにアクセスできない場合
	 * @see Field#set(Object, Object)
	 */
	public static void setValue(final Field field, final Object target, final Object value)
			throws IllegalAccessException {
		field.set(target, value);
	}
	
	private ReflectionUtil() {
	}
}
