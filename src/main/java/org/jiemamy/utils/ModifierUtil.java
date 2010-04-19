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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * {@link Modifier}用のユーティリティクラスです。
 * 
 * @author j5ik2o
 * 
 */
public class ModifierUtil {
	
	static final int BRIDGE = 0x00000040;
	
	static final int SYNTHETIC = 0x00001000;
	

	/**
	 * <code>abstract</code>かどうかを取得する。
	 * 
	 * @param clazz クラス
	 * @return <code>abstract</code>かどうか
	 */
	public static boolean isAbstract(Class<?> clazz) {
		return isAbstract(clazz.getModifiers());
	}
	
	/**
	 * <code>abstract</code>かどうかを取得する。
	 * 
	 * @param modifier モディファイヤ
	 * @return <code>abstract</code>かどうか
	 */
	public static boolean isAbstract(int modifier) {
		return Modifier.isAbstract(modifier);
	}
	
	/**
	 * <code>final</code>かどうかを取得する。
	 * 
	 * @param modifier モディファイヤ
	 * @return <code>final</code>かどうか
	 */
	public static boolean isFinal(int modifier) {
		return Modifier.isFinal(modifier);
	}
	
	/**
	 * <code>final</code>かどうかを取得する。
	 * 
	 * @param method メソッド
	 * @return <code>final</code>かどうか
	 */
	public static boolean isFinal(Method method) {
		return isFinal(method.getModifiers());
	}
	
	/**
	 * インスタンスフィールドかどうかを取得する。
	 * 
	 * @param field フィールド
	 * @return インスタンスフィールドかどうか
	 */
	public static boolean isInstanceField(Field field) {
		int m = field.getModifiers();
		return !isStatic(m) && !isFinal(m);
	}
	
	/**
	 * <code>public</code>かどうかを取得する。
	 * 
	 * @param f フィールド
	 * @return パブリックかどうか
	 */
	public static boolean isPublic(Field f) {
		return isPublic(f.getModifiers());
	}
	
	/**
	 * <code>public</code>かどうかを取得する。
	 * 
	 * @param modifier モディファイヤ
	 * @return <code>public</code>かどうか
	 */
	public static boolean isPublic(int modifier) {
		return Modifier.isPublic(modifier);
	}
	
	/**
	 * <code>public</code>かどうかを取得する。
	 * 
	 * @param m メソッド
	 * @return パブリックかどうか
	 */
	public static boolean isPublic(Method m) {
		return isPublic(m.getModifiers());
	}
	
	/**
	 * <code>public</code>,<code>static</code>,<code>final</code>かどうかを取得する。
	 * 
	 * @param modifier モディファイヤ
	 * @return <code>public</code>,<code>static</code>,<code>final</code>かどうか
	 */
	public static boolean isPublicStaticFinal(int modifier) {
		return isPublic(modifier) && isStatic(modifier) && isFinal(modifier);
	}
	
	/**
	 * <code>public</code>,<code>static</code>,<code>final</code>かどうかを取得する。
	 * 
	 * @param f フィールド
	 * @return <code>public</code>,<code>static</code>,<code>final</code>かどうか
	 */
	public static boolean isPublicStaticFinalField(Field f) {
		return isPublicStaticFinal(f.getModifiers());
	}
	
	/**
	 * <code>static</code>かどうかを取得する。
	 * 
	 * @param modifier モディファイヤ
	 * @return <code>static</code>かどうか
	 */
	public static boolean isStatic(int modifier) {
		return Modifier.isStatic(modifier);
	}
	
	/**
	 * <code>transient</code>かどうかを取得する。
	 * 
	 * @param field フィールド
	 * @return <code>transient</code>かどうか
	 * @see #isTransient(int)
	 */
	public static boolean isTransient(Field field) {
		return isTransient(field.getModifiers());
	}
	
	/**
	 * <code>transient</code>かどうかを取得する。
	 * 
	 * @param modifier モディファイヤ
	 * @return <code>transient</code>かどうか
	 */
	public static boolean isTransient(int modifier) {
		return Modifier.isTransient(modifier);
	}
	
	private ModifierUtil() {
	}
}
