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

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.exception.JiemamyError;

/**
 * {@link Field}用のユーティリティクラスです。
 * 
 * @author j5ik2o
 */
public class FieldUtil {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(FieldUtil.class);
	
	/**
	 * {@link #getElementTypeOfCollectionFromFieldType(Field)}への定数参照です
	 */
	protected static final Method GET_ELEMENT_TYPE_OF_COLLECTION_FROM_FIELD_TYPE_METHOD =
			getElementTypeFromFieldTypeMethod("Collection");
	
	/**
	 * {@link #getElementTypeOfListFromFieldType(Field)}への定数参照です
	 */
	protected static final Method GET_ELEMENT_TYPE_OF_LIST_FROM_FIELD_TYPE_METHOD =
			getElementTypeFromFieldTypeMethod("List");
	
	/**
	 * {@link #getElementTypeOfSetFromFieldType(Field)}への定数参照です
	 */
	protected static final Method GET_ELEMENT_TYPE_OF_SET_FROM_FIELD_TYPE_METHOD =
			getElementTypeFromFieldTypeMethod("Set");
	

	/**
	 * {@link Field}の値をオブジェクトとして取得する。
	 * 
	 * @param field フィールド
	 * @param target ターゲット
	 * @return {@link Object}
	 * @throws IllegalAccessException コンストラクタにアクセスできない場合
	 * @throws IllegalArgumentException 引数が正しくない場合
	 * @see Field#get(Object)
	 */
	public static Object get(Field field, Object target) throws IllegalArgumentException, IllegalAccessException {
		Validate.notNull(field);
		Validate.notNull(target);
		return field.get(target);
	}
	
	/**
	 * <code>ReflectionUtil#getElementTypeOf<var>Xxx</var>FromFieldType()</code>
	 * の {@link Method}を取得する。
	 * 
	 * @param type 取得するメソッドが対象とする型名
	 * @return
	 *         <code>ReflectionUtil#getElementTypeOf<var>Xxx</var>FromFieldType()</code>
	 *         の{@link Method}
	 */
	protected static Method getElementTypeFromFieldTypeMethod(final String type) {
		Validate.notNull(type);
		try {
			final Class<?> reflectionUtilClass = ReflectionUtil.class;
			return reflectionUtilClass.getMethod("getElementTypeOf" + type + "FromFieldType", new Class[] {
				Field.class
			});
		} catch (Throwable ignore) {
			//ignore
		}
		return null;
	}
	
	/**
	 * 指定されたフィールドのパラメタ化されたコレクションの要素型を取得する。
	 * 
	 * @param field フィールド
	 * @return フィールドのパラメタ化されたコレクションの要素型
	 */
	public static Class<?> getElementTypeOfCollectionFromFieldType(final Field field) {
		Validate.notNull(field);
		try {
			return (Class<?>) MethodUtil.invoke(GET_ELEMENT_TYPE_OF_COLLECTION_FROM_FIELD_TYPE_METHOD, null,
					new Object[] {
						field
					});
		} catch (Throwable ignore) {
			//ignore
		}
		return null;
	}
	
	/**
	 * 指定されたフィールドのパラメタ化されたリストの要素型を取得する。
	 * 
	 * @param field フィールド
	 * @return フィールドのパラメタ化されたリストの要素型
	 */
	public static Class<?> getElementTypeOfListFromFieldType(final Field field) {
		Validate.notNull(field);
		try {
			return (Class<?>) MethodUtil.invoke(GET_ELEMENT_TYPE_OF_LIST_FROM_FIELD_TYPE_METHOD, null, new Object[] {
				field
			});
		} catch (Throwable ignore) {
			//ignore
		}
		return null;
	}
	
	/**
	 * 指定されたフィールドのパラメタ化されたセットの要素型を取得する。
	 * 
	 * @param field フィールド
	 * @return フィールドのパラメタ化されたセットの要素型
	 */
	public static Class<?> getElementTypeOfSetFromFieldType(final Field field) {
		Validate.notNull(field);
		try {
			return (Class<?>) MethodUtil.invoke(GET_ELEMENT_TYPE_OF_SET_FROM_FIELD_TYPE_METHOD, null, new Object[] {
				field
			});
		} catch (Throwable ignore) {
			//ignore
		}
		return null;
	}
	
	/**
	 * staticな {@link Field}の値をintとして取得する。
	 * 
	 * @param field フィールド
	 * @return intの値 
	 * @throws IllegalAccessException フィールドにアクセスできない場合
	 * @throws IllegalArgumentException フィールドを宣言するクラスまたはインタフェースのインスタンスではない場合
	 * @see #getInt(Field, Object)
	 */
	public static int getInt(Field field) throws IllegalArgumentException, IllegalAccessException {
		Validate.notNull(field);
		return getInt(field, null);
	}
	
	/**
	 * {@link Field}の値をintとして取得する。
	 * 
	 * @param field フィールド
	 * @param target ターゲット
	 * @return intの値
	 * @throws IllegalAccessException フィールドにアクセスできない場合
	 * @throws IllegalArgumentException フィールドを宣言するクラスまたはインタフェースのインスタンスではない場合
	 * @see Field#getInt(Object)
	 */
	public static int getInt(Field field, Object target) throws IllegalArgumentException, IllegalAccessException {
		Validate.notNull(field);
		Validate.notNull(target);
		return field.getInt(target);
	}
	
	/**
	 * staticな {@link Field}の値を {@link String}として取得する。
	 * 
	 * @param field フィールド
	 * @return {@link String}の値
	 * @throws IllegalAccessException フィールドにアクセスできない場合
	 * @throws IllegalArgumentException フィールドを宣言するクラスまたはインタフェースのインスタンスではない場合
	 * @see #getString(Field, Object)
	 */
	public static String getString(Field field) throws IllegalArgumentException, IllegalAccessException {
		Validate.notNull(field);
		return getString(field, null);
	}
	
	/**
	 * {@link Field}の値を {@link String}として取得する。
	 * 
	 * @param field フィールド
	 * @param target ターゲット
	 * @return {@link String}の値
	 * @throws IllegalAccessException フィールドにアクセスできない場合
	 * @throws IllegalArgumentException フィールドを宣言するクラスまたはインタフェースのインスタンスではない場合
	 * @see Field#get(Object)
	 */
	public static String getString(Field field, Object target) throws IllegalArgumentException, IllegalAccessException {
		Validate.notNull(field);
		Validate.notNull(target);
		return (String) field.get(target);
	}
	
	/**
	 * インスタンスフィールドがどうかを取得する。
	 * 
	 * @param field フィールド
	 * @return インスタンスフィールドかどうか
	 */
	public static boolean isInstanceField(Field field) {
		Validate.notNull(field);
		int mod = field.getModifiers();
		return !Modifier.isStatic(mod) && !Modifier.isFinal(mod);
	}
	
	/**
	 * パブリックフィールドかどうかを取得する。
	 * 
	 * @param field フィールド
	 * @return パブリックフィールドかどうか
	 */
	public static boolean isPublicField(Field field) {
		Validate.notNull(field);
		int mod = field.getModifiers();
		return Modifier.isPublic(mod);
	}
	
	/**
	 * {@link Field}に値を設定する。
	 * 
	 * @param field フィールド
	 * @param target ターゲット
	 * @param value 値
	 * @throws IllegalAccessException フィールドにアクセスできない場合
	 * @throws IllegalArgumentException フィールドを宣言するクラスまたはインタフェースのインスタンスではない場合
	 * あるいはラップ解除変換が失敗した場合
	 * @see Field#set(Object, Object)
	 */
	public static void set(Field field, Object target, Object value) throws IllegalArgumentException,
			IllegalAccessException {
		Validate.notNull(field);
		Validate.notNull(target);
		Validate.notNull(value);
		field.set(target, value);
	}
	
	private FieldUtil() {
	}
	
}
