/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.lang.Validate;

/**
 * {@link Modifier}用のユーティリティクラスです。
 * 
 * @version $Id$
 * @author j5ik2o
 */
public final class ModifierUtil {
	
	/**
	 * {@code abstract}かどうかを取得する。
	 * 
	 * @param clazz クラス
	 * @return {@code abstract}である場合は{@code true}、そうでない場合は{@code false}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static boolean isAbstract(Class<?> clazz) {
		Validate.notNull(clazz);
		return Modifier.isAbstract(clazz.getModifiers());
	}
	
	/**
	 * {@code abstract}メソッドかどうかを取得する。
	 * 
	 * @param method メソッド
	 * @return {@code abstract}の場合は{@code true}、そうでない場合は{@code false}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static boolean isAbstract(Method method) {
		Validate.notNull(method);
		int mod = method.getModifiers();
		return Modifier.isAbstract(mod);
	}
	
	/**
	 * {@code final}であるかどうか調べる。
	 * 
	 * @param member メソッド
	 * @return {@code final}である場合は{@code true}、そうでない場合は{@code false}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static boolean isFinal(Member member) {
		Validate.notNull(member);
		return Modifier.isFinal(member.getModifiers());
	}
	
	/**
	 * インスタンスメンバであるかどうか調べる。
	 * 
	 * @param member フィールド
	 * @return インスタンスメンバである場合は{@code true}、そうでない場合は{@code false}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static boolean isInstanceMember(Member member) {
		Validate.notNull(member);
		int mod = member.getModifiers();
		return Modifier.isStatic(mod) == false;
	}
	
	/**
	 * {@code public}であるかどうか調べる。
	 * 
	 * @param member フィールド
	 * @return パブリックである場合は{@code true}、そうでない場合は{@code false}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static boolean isPublic(Member member) {
		Validate.notNull(member);
		return Modifier.isPublic(member.getModifiers());
	}
	
	/**
	 * {@code public},{@code static},{@code final}である場合は{@code true}、そうでない場合は{@code false}を取得する。
	 * 
	 * @param modifier 修飾子
	 * @return {@code public},{@code static},{@code final}である場合は{@code true}、そうでない場合は{@code false}
	 */
	public static boolean isPublicStaticFinal(int modifier) {
		return Modifier.isPublic(modifier) && Modifier.isStatic(modifier) && Modifier.isFinal(modifier);
	}
	
	/**
	 * {@code public},{@code static},{@code final}である場合は{@code true}、そうでない場合は{@code false}を取得する。
	 * 
	 * @param member フィールド
	 * @return {@code public},{@code static},{@code final}である場合は{@code true}、そうでない場合は{@code false}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static boolean isPublicStaticFinal(Member member) {
		Validate.notNull(member);
		return isPublicStaticFinal(member.getModifiers());
	}
	
	/**
	 * {@code transient}である場合は{@code true}、そうでない場合は{@code false}を取得する。
	 * 
	 * @param field フィールド
	 * @return {@code transient}である場合は{@code true}、そうでない場合は{@code false}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static boolean isTransient(Field field) {
		Validate.notNull(field);
		return Modifier.isTransient(field.getModifiers());
	}
	
	private ModifierUtil() {
	}
}
