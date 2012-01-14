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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link Field}用のユーティリティクラスです。
 * 
 * @version $Id$
 * @author j5ik2o
 */
public final class FieldUtil {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(FieldUtil.class);
	
	/**
	 * {@link #getElementTypeOfCollectionFromFieldType(Field)}への定数参照
	 */
	private static final Method GET_ELEMENT_TYPE_OF_COLLECTION_FROM_FIELD_TYPE_METHOD =
			getElementTypeFromFieldTypeMethod("Collection");
	
	/**
	 * {@link #getElementTypeOfListFromFieldType(Field)}への定数参照
	 */
	private static final Method GET_ELEMENT_TYPE_OF_LIST_FROM_FIELD_TYPE_METHOD =
			getElementTypeFromFieldTypeMethod("List");
	
	/**
	 * {@link #getElementTypeOfSetFromFieldType(Field)}への定数参照
	 */
	private static final Method GET_ELEMENT_TYPE_OF_SET_FROM_FIELD_TYPE_METHOD =
			getElementTypeFromFieldTypeMethod("Set");
	

	/**
	 * 指定されたフィールドのパラメタ化されたコレクションの要素型を取得する。
	 * 
	 * @param field フィールド
	 * @return フィールドのパラメタ化されたコレクションの要素型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> getElementTypeOfCollectionFromFieldType(Field field) {
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
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> getElementTypeOfListFromFieldType(Field field) {
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
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Class<?> getElementTypeOfSetFromFieldType(Field field) {
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
	 * {@link Field}の値を {@code T}として取得する。
	 * 
	 * @param <T> 結果の型
	 * @param type 結果の型
	 * @param field フィールド
	 * @param target ターゲット
	 * @return 値
	 * @throws IllegalAccessException フィールドにアクセスできない場合
	 * @throws IllegalArgumentException フィールドを宣言するクラスまたはインタフェースのインスタンスではない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @see Field#get(Object)
	 */
	public static <T>T getValue(Class<T> type, Field field, Object target) throws IllegalArgumentException,
			IllegalAccessException {
		Validate.notNull(type);
		Validate.notNull(field);
		Validate.notNull(target);
		return type.cast(field.get(target));
	}
	
	/**
	 * インスタンスフィールドかどうかを取得する。
	 * 
	 * @param field フィールド
	 * @return インスタンスフィールドの場合は{@code true}、そうでない場合は{@code false}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static boolean isInstanceField(Field field) {
		Validate.notNull(field);
		int mod = field.getModifiers();
		return !Modifier.isStatic(mod) && !Modifier.isFinal(mod);
	}
	
	/**
	 * {@code ReflectionUtil#getElementTypeOf<var>Xxx</var>FromFieldType()}
	 * の {@link Method}を取得する。
	 * 
	 * @param type 取得するメソッドが対象とする型名
	 * @return {@code ReflectionUtil#getElementTypeOf<var>Xxx</var>FromFieldType()}の{@link Method}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	protected static Method getElementTypeFromFieldTypeMethod(String type) {
		Validate.notNull(type);
		try {
			return ReflectionUtil.class.getMethod("getElementTypeOf" + type + "FromFieldType", new Class[] {
				Field.class
			});
		} catch (Throwable ignore) {
			//ignore
		}
		return null;
	}
	
	private FieldUtil() {
	}
	
}
