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
 */
public class ModifierUtil {
	
	static final int BRIDGE = 0x00000040;
	
	static final int SYNTHETIC = 0x00001000;
	

	/**
	 * {@code abstract}かどうかを取得する。
	 * 
	 * @param clazz クラス
	 * @return {@code abstract}である場合は{@code true}、そうでない場合は{@code false}
	 */
	public static boolean isAbstract(Class<?> clazz) {
		return isAbstract(clazz.getModifiers());
	}
	
	/**
	 * {@code abstract}である場合は{@code true}、そうでない場合は{@code false}を取得する。
	 * 
	 * @param modifier モディファイヤ
	 * @return {@code abstract}である場合は{@code true}、そうでない場合は{@code false}
	 */
	public static boolean isAbstract(int modifier) {
		return Modifier.isAbstract(modifier);
	}
	
	/**
	 * {@code final}である場合は{@code true}、そうでない場合は{@code false}を取得する。
	 * 
	 * @param modifier モディファイヤ
	 * @return {@code final}である場合は{@code true}、そうでない場合は{@code false}
	 */
	public static boolean isFinal(int modifier) {
		return Modifier.isFinal(modifier);
	}
	
	/**
	 * {@code final}である場合は{@code true}、そうでない場合は{@code false}を取得する。
	 * 
	 * @param method メソッド
	 * @return {@code final}である場合は{@code true}、そうでない場合は{@code false}
	 */
	public static boolean isFinal(Method method) {
		return isFinal(method.getModifiers());
	}
	
	/**
	 * インスタンスフィールドである場合は{@code true}、そうでない場合は{@code false}を取得する。
	 * 
	 * @param field フィールド
	 * @return インスタンスフィールドである場合は{@code true}、そうでない場合は{@code false}
	 */
	public static boolean isInstanceField(Field field) {
		int m = field.getModifiers();
		return !isStatic(m) && !isFinal(m);
	}
	
	/**
	 * {@code public}である場合は{@code true}、そうでない場合は{@code false}を取得する。
	 * 
	 * @param f フィールド
	 * @return パブリックである場合は{@code true}、そうでない場合は{@code false}
	 */
	public static boolean isPublic(Field f) {
		return isPublic(f.getModifiers());
	}
	
	/**
	 * {@code public}である場合は{@code true}、そうでない場合は{@code false}を取得する。
	 * 
	 * @param modifier モディファイヤ
	 * @return {@code public}である場合は{@code true}、そうでない場合は{@code false}
	 */
	public static boolean isPublic(int modifier) {
		return Modifier.isPublic(modifier);
	}
	
	/**
	 * {@code public}である場合は{@code true}、そうでない場合は{@code false}を取得する。
	 * 
	 * @param m メソッド
	 * @return パブリックである場合は{@code true}、そうでない場合は{@code false}
	 */
	public static boolean isPublic(Method m) {
		return isPublic(m.getModifiers());
	}
	
	/**
	 * {@code public},{@code static},{@code final}である場合は{@code true}、そうでない場合は{@code false}を取得する。
	 * 
	 * @param modifier モディファイヤ
	 * @return {@code public},{@code static},{@code final}である場合は{@code true}、そうでない場合は{@code false}
	 */
	public static boolean isPublicStaticFinal(int modifier) {
		return isPublic(modifier) && isStatic(modifier) && isFinal(modifier);
	}
	
	/**
	 * {@code public},{@code static},{@code final}である場合は{@code true}、そうでない場合は{@code false}を取得する。
	 * 
	 * @param f フィールド
	 * @return {@code public},{@code static},{@code final}である場合は{@code true}、そうでない場合は{@code false}
	 */
	public static boolean isPublicStaticFinalField(Field f) {
		return isPublicStaticFinal(f.getModifiers());
	}
	
	/**
	 * {@code static}である場合は{@code true}、そうでない場合は{@code false}を取得する。
	 * 
	 * @param modifier モディファイヤ
	 * @return {@code static}である場合は{@code true}、そうでない場合は{@code false}
	 */
	public static boolean isStatic(int modifier) {
		return Modifier.isStatic(modifier);
	}
	
	/**
	 * {@code transient}である場合は{@code true}、そうでない場合は{@code false}を取得する。
	 * 
	 * @param field フィールド
	 * @return {@code transient}である場合は{@code true}、そうでない場合は{@code false}
	 * @see #isTransient(int)
	 */
	public static boolean isTransient(Field field) {
		return isTransient(field.getModifiers());
	}
	
	/**
	 * {@code transient}である場合は{@code true}、そうでない場合は{@code false}を取得する。
	 * 
	 * @param modifier モディファイヤ
	 * @return {@code transient}である場合は{@code true}、そうでない場合は{@code false}
	 */
	public static boolean isTransient(int modifier) {
		return Modifier.isTransient(modifier);
	}
	
	private ModifierUtil() {
	}
}
