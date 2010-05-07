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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * {@link Method}用のユーティリティクラス。
 * 
 * @author j5ik2o
 * 
 */
public class MethodUtil {
	
	/** バッファサイズ */
	private static final int BUFF_SIZE = 100;
	
	private static final Method IS_BRIDGE_METHOD = getIsBridgeMethod();
	
	private static final Method IS_SYNTHETIC_METHOD = getIsSyntheticMethod();
	
	/** {@link #getElementTypeOfCollectionFromParameterType(Method, int)}への定数参照 */
	protected static final Method GET_ELEMENT_TYPE_OF_COLLECTION_FROM_PARAMETER_METHOD =
			getElementTypeFromParameterMethod("Collection");
	
	/** {@link #getElementTypeOfCollectionFromReturnType(Method)}への定数参照 */
	protected static final Method GET_ELEMENT_TYPE_OF_COLLECTION_FROM_RETURN_METHOD =
			getElementTypeFromReturnMethod("Collection");
	
	/** {@link #getElementTypeOfListFromParameterType(Method, int)}への定数参照 */
	protected static final Method GET_ELEMENT_TYPE_OF_LIST_FROM_PARAMETER_METHOD =
			getElementTypeFromParameterMethod("List");
	
	/** {@link #getElementTypeOfListFromReturnType(Method)}への定数参照 */
	protected static final Method GET_ELEMENT_TYPE_OF_LIST_FROM_RETURN_METHOD = getElementTypeFromReturnMethod("List");
	
	/** {@link #getElementTypeOfSetFromParameterType(Method, int)}への定数参照 */
	protected static final Method GET_ELEMENT_TYPE_OF_SET_FROM_PARAMETER_METHOD =
			getElementTypeFromParameterMethod("Set");
	
	/** {@link #getElementTypeOfSetFromReturnType(Method)}への定数参照 */
	protected static final Method GET_ELEMENT_TYPE_OF_SET_FROM_RETURN_METHOD = getElementTypeFromReturnMethod("Set");
	

	/**
	 * メソッドの引数型 (パラメタ化されたコレクション)の要素型を取得する。
	 * 
	 * @param method メソッド
	 * @param position パラメタ化されたコレクションが宣言されているメソッド引数の位置
	 * @return 指定されたメソッドの引数型として宣言されているパラメタ化されたコレクションの要素型
	 * @throws InvocationTargetException メソッドが例外をスローする場合
	 * @throws IllegalAccessException メソッドにアクセスできない場合
	 * @throws IllegalArgumentException 引数が正しくない場合
	 */
	public static Class<?> getElementTypeOfCollectionFromParameterType(Method method, int position)
			throws IllegalAccessException, InvocationTargetException {
		return (Class<?>) invoke(GET_ELEMENT_TYPE_OF_COLLECTION_FROM_PARAMETER_METHOD, null, new Object[] {
			method,
			Integer.valueOf(position)
		});
	}
	
	/**
	 * 指定されたメソッドの戻り値型として宣言されているパラメタ化されたコレクションの要素型を取得する。
	 * 
	 * @param method メソッド
	 * @return 指定されたメソッドの戻り値型として宣言されているパラメタ化されたコレクションの要素型
	 * @throws InvocationTargetException メソッドが例外をスローする場合
	 * @throws IllegalAccessException メソッドにアクセスできない場合
	 * @throws IllegalArgumentException 引数が正しくない場合
	 */
	public static Class<?> getElementTypeOfCollectionFromReturnType(Method method) throws IllegalAccessException,
			InvocationTargetException {
		return (Class<?>) invoke(GET_ELEMENT_TYPE_OF_COLLECTION_FROM_RETURN_METHOD, null, new Object[] {
			method
		});
	}
	
	/**
	 * メソッドの引数型 (パラメタ化されたリスト) の要素型を取得する。
	 * 
	 * @param method メソッド
	 * @param position パラメタ化されたリストが宣言されているメソッド引数の位置
	 * @return 指定されたメソッドの引数型として宣言されているパラメタ化されたリストの要素型
	 * @throws InvocationTargetException メソッドが例外をスローする場合
	 * @throws IllegalAccessException メソッドにアクセスできない場合
	 * @throws IllegalArgumentException 引数が正しくない場合
	 */
	public static Class<?> getElementTypeOfListFromParameterType(Method method, int position)
			throws IllegalAccessException, InvocationTargetException {
		return (Class<?>) invoke(GET_ELEMENT_TYPE_OF_LIST_FROM_PARAMETER_METHOD, null, new Object[] {
			method,
			Integer.valueOf(position)
		});
	}
	
	/**
	 * 指定されたメソッドの戻り値型として宣言されているパラメタ化されたリストの要素型を取得する。
	 * 
	 * @param method メソッド
	 * @return 指定されたメソッドの戻り値型として宣言されているパラメタ化されたリストの要素型
	 * @throws InvocationTargetException メソッドが例外をスローする場合
	 * @throws IllegalAccessException メソッドにアクセスできない場合
	 * @throws IllegalArgumentException 引数が正しくない場合
	 */
	public static Class<?> getElementTypeOfListFromReturnType(Method method) throws IllegalAccessException,
			InvocationTargetException {
		return (Class<?>) invoke(GET_ELEMENT_TYPE_OF_LIST_FROM_RETURN_METHOD, null, new Object[] {
			method
		});
	}
	
	/**
	 * メソッドの引数型 (パラメタ化されたセット) の要素型を取得する。
	 * 
	 * @param method メソッド
	 * @param position パラメタ化されたコレクションが宣言されているメソッド引数の位置
	 * @return 指定されたメソッドの引数型として宣言されているパラメタ化されたセットの要素型
	 * @throws InvocationTargetException メソッドが例外をスローする場合
	 * @throws IllegalAccessException メソッドにアクセスできない場合
	 * @throws IllegalArgumentException 引数が正しくない場合
	 */
	public static Class<?> getElementTypeOfSetFromParameterType(Method method, int position)
			throws IllegalAccessException, InvocationTargetException {
		return (Class<?>) invoke(GET_ELEMENT_TYPE_OF_SET_FROM_PARAMETER_METHOD, null, new Object[] {
			method,
			Integer.valueOf(position)
		});
	}
	
	/**
	 * 指定されたメソッドの戻り値型として宣言されているパラメタ化されたセットの要素型を取得する。
	 * 
	 * @param method メソッド
	 * @return 指定されたメソッドの戻り値型として宣言されているパラメタ化されたセットの要素型
	 * @throws InvocationTargetException メソッドが例外をスローする場合
	 * @throws IllegalAccessException メソッドにアクセスできない場合
	 * @throws IllegalArgumentException 引数が正しくない場合
	 */
	public static Class<?> getElementTypeOfSetFromReturnType(Method method) throws IllegalAccessException,
			InvocationTargetException {
		if (GET_ELEMENT_TYPE_OF_SET_FROM_RETURN_METHOD == null) {
			return null;
		}
		return (Class<?>) invoke(GET_ELEMENT_TYPE_OF_SET_FROM_RETURN_METHOD, null, new Object[] {
			method
		});
	}
	
	/**
	 * シグニチャを取得する。
	 * 
	 * <p>例： {@code getSignature(java.lang.String, java.lang.Class[])}
	 * 
	 * @param methodName メソッド名
	 * @param argTypes 引数
	 * @return シグニチャ
	 */
	public static String getSignature(String methodName, Class<?>[] argTypes) {
		StringBuffer buf = new StringBuffer(BUFF_SIZE);
		buf.append(methodName);
		buf.append("(");
		if (argTypes != null) {
			for (int i = 0; i < argTypes.length; ++i) {
				if (i > 0) {
					buf.append(", ");
				}
				buf.append(argTypes[i].getName());
			}
		}
		buf.append(")");
		return buf.toString();
	}
	
	/**
	 * シグニチャを取得する。
	 * 
	 * @param methodName メソッド名
	 * @param methodArgs メソッドの引数
	 * @return シグニチャ
	 */
	public static String getSignature(String methodName, Object[] methodArgs) {
		StringBuffer buf = new StringBuffer(BUFF_SIZE);
		buf.append(methodName);
		buf.append("(");
		if (methodArgs != null) {
			for (int i = 0; i < methodArgs.length; ++i) {
				if (i > 0) {
					buf.append(", ");
				}
				if (methodArgs[i] != null) {
					buf.append(methodArgs[i].getClass().getName());
				} else {
					buf.append("null");
				}
			}
		}
		buf.append(")");
		return buf.toString();
	}
	
	/**
	 * {@link Method#invoke(Object, Object[])}の例外処理をラップする。
	 * 
	 * @param method メソッド
	 * @param target ターゲット
	 * @param args 引数
	 * @return 戻り値
	 * @throws IllegalAccessException メソッドにアクセスできない場合
	 * @throws IllegalArgumentException 引数が正しくない場合
	 * @throws InvocationTargetException メソッドが例外をスローする場合
	 * @see Method#invoke(Object, Object[])
	 */
	public static Object invoke(Method method, Object target, Object[] args) throws IllegalAccessException,
			InvocationTargetException {
		try {
			return method.invoke(target, args);
		} catch (InvocationTargetException ex) {
			Throwable t = ex.getCause();
			if (t instanceof RuntimeException) {
				throw (RuntimeException) t;
			}
			if (t instanceof Error) {
				throw (Error) t;
			}
			throw ex;
		}
	}
	
	/**
	 * {@code abstract}メソッドかどうかを取得する。
	 * 
	 * @param method メソッド
	 * @return {@code abstract}の場合は{@code true}、そうでない場合は{@code false}
	 */
	public static boolean isAbstract(Method method) {
		int mod = method.getModifiers();
		return Modifier.isAbstract(mod);
	}
	
	/**
	 * ブリッジメソッドかどうか取得する。
	 * 
	 * @param method {@link Method}
	 * @return ブリッジメソッドかどうか
	 * @throws InvocationTargetException メソッドが例外をスローする場合
	 * @throws IllegalAccessException メソッドにアクセス場合
	 * @throws IllegalArgumentException 引数が正しくない場合
	 */
	public static boolean isBridgeMethod(Method method) throws IllegalAccessException, InvocationTargetException {
		if (IS_BRIDGE_METHOD == null) {
			return false;
		}
		return ((Boolean) invoke(IS_BRIDGE_METHOD, method, null)).booleanValue();
	}
	
	/**
	 * {@link #equals(Object)}メソッドかどうかを取得する。
	 * 
	 * @param method {@link Method}
	 * @return equalsメソッドかどうか
	 */
	public static boolean isEqualsMethod(Method method) {
		return method != null && method.getName().equals("equals") && method.getReturnType() == boolean.class
				&& method.getParameterTypes().length == 1 && method.getParameterTypes()[0] == Object.class;
	}
	
	/**
	 * {@link #hashCode()}メソッドかどうか取得する。
	 * 
	 * @param method {@link Method}
	 * @return hashCodeメソッドかどうか
	 */
	public static boolean isHashCodeMethod(Method method) {
		return method != null && method.getName().equals("hashCode") && method.getReturnType() == int.class
				&& method.getParameterTypes().length == 0;
	}
	
	/**
	 * 合成メソッドかどうかを取得する。
	 * 
	 * @param method {@link Method}
	 * @return 合成メソッドかどうか
	 * @throws InvocationTargetException メソッドが例外をスローする場合
	 * @throws IllegalAccessException メソッドにアクセス場合
	 * @throws IllegalArgumentException 引数が正しくない場合
	 */
	public static boolean isSyntheticMethod(Method method) throws IllegalAccessException, InvocationTargetException {
		if (IS_SYNTHETIC_METHOD == null) {
			return false;
		}
		return ((Boolean) invoke(IS_SYNTHETIC_METHOD, method, null)).booleanValue();
	}
	
	/**
	 * {@link #toString()}メソッドかどうか取得する。
	 * 
	 * @param method {@link Method}
	 * @return toStringメソッドかどうか
	 */
	public static boolean isToStringMethod(Method method) {
		return method != null && method.getName().equals("toString") && method.getReturnType() == String.class
				&& method.getParameterTypes().length == 0;
	}
	
	/**
	 * {@code ReflectionUtil#getElementTypeOf<var>Xxx</var>FromParameter}の{@link Method}を取得する。
	 * 
	 * @param type 取得するメソッドが対象とする型名
	 * @return {@link Method}
	 */
	protected static Method getElementTypeFromParameterMethod(String type) {
		try {
			Class<?> reflectionUtilClass = ReflectionUtil.class;
			return reflectionUtilClass.getMethod("getElementTypeOf" + type + "FromParameterType", new Class[] {
				Method.class,
				int.class
			});
		} catch (Throwable ignore) {
			// ignore
		}
		return null;
	}
	
	/**
	 * {@code ReflectionUtil#getElementTypeOf<var>Xxx</var>FromReturn}の{@link Method}を取得する。
	 * 
	 * @param type 取得するメソッドが対象とする型名
	 * @return {@link Method}
	 */
	protected static Method getElementTypeFromReturnMethod(String type) {
		try {
			Class<?> reflectionUtilClass = ReflectionUtil.class;
			return reflectionUtilClass.getMethod("getElementTypeOf" + type + "FromReturnType", new Class[] {
				Method.class
			});
		} catch (Throwable ignore) {
			// ignore
		}
		return null;
	}
	
	private static Method getIsBridgeMethod() {
		try {
			return Method.class.getMethod("isBridge", new Class<?>[] {
				null
			});
		} catch (NoSuchMethodException e) {
			return null;
		}
	}
	
	private static Method getIsSyntheticMethod() {
		try {
			return Method.class.getMethod("isSynthetic", new Class<?>[] {
				null
			});
		} catch (NoSuchMethodException e) {
			return null;
		}
	}
	
	private MethodUtil() {
	}
	
}
