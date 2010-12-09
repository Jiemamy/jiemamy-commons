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

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

import org.apache.commons.lang.Validate;

/**
 * Genericsを扱うためのユーティリティ・クラス。
 * 
 * @version $Id$
 * @author j5ik2o
 */
public final class GenericUtil {
	
	/**
	 * {@code type}の実際の型を取得する。
	 * <ul>
	 * <li>{@code type}が{@code Class}の場合はそのまま返す。</li>
	 * <li>{@code type}がパラメータ化された型の場合はその原型を返す。</li>
	 * <li>{@code type}がワイルドカード型の場合は(最初の)上限境界を返す。</li>
	 * <li>{@code type}が型変数の場合はその変数の実際の型引数を返す。</li>
	 * <li>{@code type}が配列の場合はその要素の実際の型の配列を返す。</li>
	 * <li>その他の場合は{@code null}を返す。</li>
	 * </ul>
	 * 
	 * @param type タイプ
	 * @param map パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
	 * @return {@code type}の実際の型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> getActualClass(Type type, Map<TypeVariable<?>, Type> map) {
		Validate.notNull(type);
		Validate.notNull(map);
		if (Class.class.isInstance(type)) {
			return Class.class.cast(type);
		}
		if (ParameterizedType.class.isInstance(type)) {
			return getActualClass(ParameterizedType.class.cast(type).getRawType(), map);
		}
		if (WildcardType.class.isInstance(type)) {
			return getActualClass(WildcardType.class.cast(type).getUpperBounds()[0], map);
		}
		if (TypeVariable.class.isInstance(type)) {
			return getActualClass(map.get(TypeVariable.class.cast(type)), map);
		}
		if (GenericArrayType.class.isInstance(type)) {
			GenericArrayType genericArrayType = GenericArrayType.class.cast(type);
			Class<?> componentClass = getActualClass(genericArrayType.getGenericComponentType(), map);
			return Array.newInstance(componentClass, 0).getClass();
		}
		return null;
	}
	
	/**
	 * パラメータ化された型を要素とする配列の実際の要素型を取得する。
	 * <ul>
	 * <li>{@code type}がパラメータ化された型の配列でない場合は{@code null}を取得する。</li>
	 * <li>{@code type}が{@code Class}の場合はそのまま返す。</li>
	 * <li>{@code type}がパラメータ化された型の場合はその原型を返す。</li>
	 * <li>{@code type}がワイルドカード型の場合は(最初の)上限境界を返す。</li>
	 * <li>{@code type}が型変数の場合はその変数の実際の型引数を返す。</li>
	 * <li>{@code type}が配列の場合はその要素の実際の型の配列を返す。</li>
	 * <li>その他の場合は{@code null}を返す。</li>
	 * </ul>
	 * 
	 * @param type パラメータ化された型を要素とする配列
	 * @param map パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
	 * @return パラメータ化された型を要素とする配列の実際の要素型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> getActualElementClassOfArray(Type type, Map<TypeVariable<?>, Type> map) {
		Validate.notNull(type);
		Validate.notNull(map);
		if (GenericArrayType.class.isInstance(type) == false) {
			return null;
		}
		return getActualClass(GenericArrayType.class.cast(type).getGenericComponentType(), map);
	}
	
	/**
	 * パラメータ化された{@link Collection}の実際の要素型を返す。
	 * <ul>
	 * <li>{@code type}がパラメータ化された{@link Collection}でない場合は{@code null}
	 * を返す。</li>
	 * <li>{@code type}が{@code Class}の場合はそのまま返す。</li>
	 * <li>{@code type}がパラメータ化された型の場合はその原型を返す。</li>
	 * <li>{@code type}がワイルドカード型の場合は(最初の)上限境界を返す。</li>
	 * <li>{@code type}が型変数の場合はその変数の実際の型引数を返す。</li>
	 * <li>{@code type}が配列の場合はその要素の実際の型の配列を返す。</li>
	 * <li>その他の場合は{@code null}を返す。</li>
	 * </ul>
	 * 
	 * @param type パラメータ化された{@link Collection}
	 * @param map パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
	 * @return パラメータ化された{@link Collection}の実際の要素型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> getActualElementClassOfCollection(Type type, Map<TypeVariable<?>, Type> map) {
		Validate.notNull(type);
		Validate.notNull(map);
		if (isTypeOf(type, Collection.class) == false) {
			return null;
		}
		return getActualClass(getGenericParameter(type, 0), map);
	}
	
	/**
	 * パラメータ化された{@link List}の実際の要素型を取得する。
	 * <ul>
	 * <li>{@code type}がパラメータ化された{@link List}でない場合は{@code null}を返す。</li>
	 * <li>{@code type}が{@code Class}の場合はそのまま返す。</li>
	 * <li>{@code type}がパラメータ化された型の場合はその原型を返す。</li>
	 * <li>{@code type}がワイルドカード型の場合は(最初の)上限境界を返す。</li>
	 * <li>{@code type}が型変数の場合はその変数の実際の型引数を返す。</li>
	 * <li>{@code type}が配列の場合はその要素の実際の型の配列を返す。</li>
	 * <li>その他の場合は{@code null}を返す。</li>
	 * </ul>
	 * 
	 * @param type パラメータ化された{@link List}
	 * @param map パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
	 * @return パラメータ化された{@link List}の実際の要素型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> getActualElementClassOfList(Type type, Map<TypeVariable<?>, Type> map) {
		Validate.notNull(type);
		Validate.notNull(map);
		if (isTypeOf(type, List.class) == false) {
			return null;
		}
		return getActualClass(getGenericParameter(type, 0), map);
	}
	
	/**
	 * パラメータ化された{@link Set}の実際の要素型を取得する。
	 * <ul>
	 * <li>{@code type}がパラメータ化された{@link Set}でない場合は{@code null}を返す。</li>
	 * <li>{@code type}が{@code Class}の場合はそのまま返す。</li>
	 * <li>{@code type}がパラメータ化された型の場合はその原型を返す。</li>
	 * <li>{@code type}がワイルドカード型の場合は(最初の)上限境界を返す。</li>
	 * <li>{@code type}が型変数の場合はその変数の実際の型引数を返す。</li>
	 * <li>{@code type}が配列の場合はその要素の実際の型の配列を返す。</li>
	 * <li>その他の場合は{@code null}を返す。</li>
	 * </ul>
	 * 
	 * @param type パラメータ化された{@link Set}
	 * @param map パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
	 * @return パラメータ化された{@link Set}の実際の要素型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> getActualElementClassOfSet(Type type, Map<TypeVariable<?>, Type> map) {
		Validate.notNull(type);
		Validate.notNull(map);
		if (isTypeOf(type, Set.class) == false) {
			return null;
		}
		return getActualClass(getGenericParameter(type, 0), map);
	}
	
	/**
	 * パラメータ化された{@link Map}のキーの実際の型を取得する。
	 * <ul>
	 * <li>キー型がパラメータ化された{@link Map}でない場合は{@code null}を返す。</li>
	 * <li>{@code type}が{@code Class}の場合はそのまま返す。</li>
	 * <li>{@code type}がパラメータ化された型の場合はその原型を返す。</li>
	 * <li>{@code type}がワイルドカード型の場合は(最初の)上限境界を返す。</li>
	 * <li>{@code type}が型変数の場合はその変数の実際の型引数を返す。</li>
	 * <li>{@code type}が配列の場合はその要素の実際の型の配列を返す。</li>
	 * <li>その他の場合は{@code null}を返す。</li>
	 * </ul>
	 * 
	 * @param type パラメータ化された{@link Map}
	 * @param map パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
	 * @return パラメータ化された{@link Map}のキーの実際の型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> getActualKeyClassOfMap(Type type, Map<TypeVariable<?>, Type> map) {
		Validate.notNull(type);
		Validate.notNull(map);
		if (isTypeOf(type, Map.class) == false) {
			return null;
		}
		return getActualClass(getGenericParameter(type, 0), map);
	}
	
	/**
	 * パラメータ化された{@link Map}の値の実際の型を取得する。
	 * <ul>
	 * <li>{@code type}がパラメータ化された{@link Map}でない場合は{@code null}を返す。</li>
	 * <li>{@code type}が{@code Class}の場合はそのまま返す。</li>
	 * <li>{@code type}がパラメータ化された型の場合はその原型を返す。</li>
	 * <li>{@code type}がワイルドカード型の場合は(最初の)上限境界を返す。</li>
	 * <li>{@code type}が型変数の場合はその変数の実際の型引数を返す。</li>
	 * <li>{@code type}が配列の場合はその要素の実際の型の配列を返す。</li>
	 * <li>その他の場合は{@code null}を返す。</li>
	 * </ul>
	 * 
	 * @param type パラメータ化された{@link Map}
	 * @param map パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
	 * @return パラメータ化された{@link Map}の値の実際の型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> getActualValueClassOfMap(Type type, Map<TypeVariable<?>, Type> map) {
		Validate.notNull(type);
		Validate.notNull(map);
		if (isTypeOf(type, Map.class) == false) {
			return null;
		}
		return getActualClass(getGenericParameter(type, 1), map);
	}
	
	/**
	 * パラメータ化された型を要素とする配列の要素型を取得する。
	 * <p>
	 * {@code type}がパラメータ化された型の配列でない場合は{@code null}を返す。
	 * </p>
	 * 
	 * @param type パラメータ化された型を要素とする配列
	 * @return パラメータ化された型を要素とする配列の要素型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Type getElementTypeOfArray(Type type) {
		Validate.notNull(type);
		if (GenericArrayType.class.isInstance(type) == false) {
			return null;
		}
		return GenericArrayType.class.cast(type).getGenericComponentType();
	}
	
	/**
	 * パラメータ化された{@link Collection}の要素型を取得する。
	 * <p>
	 * {@code type}がパラメータ化された{@link List}でない場合は{@code null}を返す。
	 * </p>
	 * 
	 * @param type パラメータ化された{@link List}
	 * @return パラメータ化された{@link List}の要素型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Type getElementTypeOfCollection(Type type) {
		Validate.notNull(type);
		if (isTypeOf(type, Collection.class) == false) {
			return null;
		}
		return getGenericParameter(type, 0);
	}
	
	/**
	 * パラメータ化された{@link List}の要素型を取得する。
	 * <p>
	 * {@code type}がパラメータ化された{@link List}でない場合は{@code null}を返す。
	 * </p>
	 * 
	 * @param type パラメータ化された{@link List}
	 * @return パラメータ化された{@link List}の要素型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Type getElementTypeOfList(Type type) {
		Validate.notNull(type);
		if (isTypeOf(type, List.class) == false) {
			return null;
		}
		return getGenericParameter(type, 0);
	}
	
	/**
	 * パラメータ化された{@link Set}の要素型を取得する。
	 * <p>
	 * {@code type}がパラメータ化された{@link Set}でない場合は{@code null}を返す。
	 * </p>
	 * 
	 * @param type パラメータ化された{@link Set}
	 * @return パラメータ化された{@link Set}の要素型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Type getElementTypeOfSet(Type type) {
		Validate.notNull(type);
		if (isTypeOf(type, Set.class) == false) {
			return null;
		}
		return getGenericParameter(type, 0);
	}
	
	/**
	 * {@code type}の型引数の配列を取得する。
	 * <p>
	 * {@code type}がパラメータ化された型でない場合は{@code null}を返す。
	 * </p>
	 * 
	 * @param type タイプ
	 * @return {@code type}の型引数の配列
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Type[] getGenericParameter(Type type) {
		Validate.notNull(type);
		if (ParameterizedType.class.isInstance(type)) {
			return ParameterizedType.class.cast(type).getActualTypeArguments();
		}
		if (GenericArrayType.class.isInstance(type)) {
			return getGenericParameter(GenericArrayType.class.cast(type).getGenericComponentType());
		}
		return new Type[0];
	}
	
	/**
	 * 指定された位置の{@code type}の型引数を取得する。
	 * <p>
	 * {@code type}がパラメータ化された型でない場合は{@code null}を返す。
	 * </p>
	 * 
	 * @param type タイプ
	 * @param index 位置
	 * @return 指定された位置の{@code type}の型引数
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws ArrayIndexOutOfBoundsException {@code index}がパラメータの数以上の場合
	 */
	public static Type getGenericParameter(Type type, int index) {
		Validate.notNull(type);
		if (ParameterizedType.class.isInstance(type) == false) {
			return null;
		}
		Type[] genericParameter = getGenericParameter(type);
		if (genericParameter == null) {
			return null;
		}
		return genericParameter[index];
	}
	
	/**
	 * パラメータ化された{@link Map}のキーの型を取得する。
	 * <p>
	 * {@code type}がパラメータ化された{@link Map}でない場合は{@code null}を返す。
	 * </p>
	 * 
	 * @param type パラメータ化された{@link Map}
	 * @return パラメータ化された{@link Map}のキーの型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Type getKeyTypeOfMap(Type type) {
		Validate.notNull(type);
		if (isTypeOf(type, Map.class) == false) {
			return null;
		}
		return getGenericParameter(type, 0);
	}
	
	/**
	 * {@code type}の原型を取得する。
	 * <ul>
	 * <li>{@code type}が{@code Class}の場合はそのまま返す。</li>
	 * <li>{@code type}がパラメータ化された型の場合はその原型を返す。</li>
	 * <li>{@code type}がワイルドカード型の場合は(最初の)上限境界を返す。</li>
	 * <li>{@code type}が配列の場合はその要素の実際の型の配列を返す。</li>
	 * <li>その他の場合は{@code null}を返す。</li>
	 * </ul>
	 * 
	 * @param type タイプ
	 * @return {@code type}の原型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> getRawClass(Type type) {
		Validate.notNull(type);
		if (Class.class.isInstance(type)) {
			return Class.class.cast(type);
		}
		if (ParameterizedType.class.isInstance(type)) {
			ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
			return getRawClass(parameterizedType.getRawType());
		}
		if (WildcardType.class.isInstance(type)) {
			WildcardType wildcardType = WildcardType.class.cast(type);
			Type[] types = wildcardType.getUpperBounds();
			return getRawClass(types[0]);
		}
		if (GenericArrayType.class.isInstance(type)) {
			GenericArrayType genericArrayType = GenericArrayType.class.cast(type);
			Class<?> rawClass = getRawClass(genericArrayType.getGenericComponentType());
			return Array.newInstance(rawClass, 0).getClass();
		}
		return null;
	}
	
	/**
	 * パラメータ化された型(クラスまたはインタフェース)が持つ型変数をキー、型引数を値とする{@link Map}を取得する。
	 * 
	 * @param clazz パラメータ化された型(クラスまたはインタフェース)
	 * @return パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Map<TypeVariable<?>, Type> getTypeVariableMap(Class<?> clazz) {
		Validate.notNull(clazz);
		Map<TypeVariable<?>, Type> map = Maps.newLinkedHashMap();
		
		Class<?> superClass = clazz.getSuperclass();
		Type superClassType = clazz.getGenericSuperclass();
		if (superClass != null) {
			gatherTypeVariables(superClass, superClassType, map);
		}
		
		Class<?>[] interfaces = clazz.getInterfaces();
		Type[] interfaceTypes = clazz.getGenericInterfaces();
		for (int i = 0; i < interfaces.length; ++i) {
			gatherTypeVariables(interfaces[i], interfaceTypes[i], map);
		}
		
		return map;
	}
	
	/**
	 * パラメータ化された{@link Map}の値の型を取得する。
	 * <p>
	 * {@code type}がパラメータ化された{@link Map}でない場合は{@code null}を返す。
	 * </p>
	 * 
	 * @param type パラメータ化された{@link Map}
	 * @return パラメータ化された{@link Map}の値の型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Type getValueTypeOfMap(Type type) {
		Validate.notNull(type);
		if (isTypeOf(type, Map.class) == false) {
			return null;
		}
		return getGenericParameter(type, 1);
	}
	
	/**
	 * {@code type}の原型が{@code clazz}に代入可能であれば{@code true}を、
	 * それ以外の場合は{@code false}を取得する。
	 * 
	 * @param type タイプ
	 * @param clazz クラス
	 * @return {@code type}の原型が{@code clazz}に代入可能であれば{@code true}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static boolean isTypeOf(Type type, Class<?> clazz) {
		Validate.notNull(clazz);
		if (Class.class.isInstance(type)) {
			return clazz.isAssignableFrom(Class.class.cast(type));
		}
		if (ParameterizedType.class.isInstance(type)) {
			ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
			return isTypeOf(parameterizedType.getRawType(), clazz);
		}
		return false;
	}
	
	/**
	 * パラメータ化された型(クラスまたはインタフェース)が持つ型変数および型引数を集めて{@code map}に追加する。
	 * 
	 * @param clazz クラス
	 * @param type 型
	 * @param map パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
	 */
	protected static void gatherTypeVariables(Class<?> clazz, Type type, Map<TypeVariable<?>, Type> map) {
		if (clazz == null) {
			return;
		}
		gatherTypeVariables(type, map);
		
		Class<?> superClass = clazz.getSuperclass();
		Type superClassType = clazz.getGenericSuperclass();
		if (superClass != null) {
			gatherTypeVariables(superClass, superClassType, map);
		}
		
		Class<?>[] interfaces = clazz.getInterfaces();
		Type[] interfaceTypes = clazz.getGenericInterfaces();
		for (int i = 0; i < interfaces.length; ++i) {
			gatherTypeVariables(interfaces[i], interfaceTypes[i], map);
		}
	}
	
	/**
	 * パラメータ化された型(クラスまたはインタフェース)が持つ型変数および型引数を集めて {@code map}に追加する。
	 * 
	 * @param type 型
	 * @param map パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	protected static void gatherTypeVariables(Type type, Map<TypeVariable<?>, Type> map) {
		Validate.notNull(type);
		Validate.notNull(map);
		if (ParameterizedType.class.isInstance(type)) {
			ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
			TypeVariable<?>[] typeVariables =
					GenericDeclaration.class.cast(parameterizedType.getRawType()).getTypeParameters();
			Type[] actualTypes = parameterizedType.getActualTypeArguments();
			for (int i = 0; i < actualTypes.length; ++i) {
				map.put(typeVariables[i], actualTypes[i]);
			}
		}
	}
	
	private GenericUtil() {
	}
	
}
