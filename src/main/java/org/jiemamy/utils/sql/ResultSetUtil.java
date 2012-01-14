/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/04/14
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
package org.jiemamy.utils.sql;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyError;

/**
 * {@link ResultSet}のユーティリティクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class ResultSetUtil {
	
	/**
	 * {@link ResultSet#getAsciiStream(String)}を使って、指定したカラムインデックスのデータを取り出す。
	 * ただし、取り出し時に {@link SQLException}が発生した場合は、{@code defaultValue}を返す。
	 * 
	 * <p>{@link #getValue(Class, ResultSet, int, Object)}は、{@link ResultSet#getBinaryStream(int)}と
	 * かち合うので使用できない。</p>
	 * 
	 * @param rs {@link ResultSet}
	 * @param columnIndex カラムインデックス
	 * @param defaultValue {@link SQLException}が発生した場合や、該当するgetterが存在しなかった場合のデフォルト値
	 * @return 取り出した値、またはデフォルト値
	 * @throws IllegalArgumentException 引数{@code rs}に{@code null}を与えた場合
	 */
	public static InputStream getAsciiStream(ResultSet rs, int columnIndex, InputStream defaultValue) {
		Validate.notNull(rs);
		try {
			return rs.getAsciiStream(columnIndex);
		} catch (SQLException e) {
			// ignore
		}
		return defaultValue;
	}
	
	/**
	 * {@link ResultSet#getAsciiStream(String)}を使って、指定したカラム名のデータを取り出す。
	 * ただし、取り出し時に {@link SQLException}が発生した場合は、{@code defaultValue}を返す。
	 * 
	 * <p>{@link #getValue(Class, ResultSet, String, Object)}は、{@link ResultSet#getBinaryStream(String)}と
	 * かち合うので使用できない。</p>
	 * 
	 * @param rs {@link ResultSet}
	 * @param columnName カラム名
	 * @param defaultValue {@link SQLException}が発生した場合や、該当するgetterが存在しなかった場合のデフォルト値
	 * @return 取り出した値、またはデフォルト値
	 * @throws IllegalArgumentException 引数{@code rs}に{@code null}を与えた場合
	 */
	public static InputStream getAsciiStream(ResultSet rs, String columnName, InputStream defaultValue) {
		Validate.notNull(rs);
		try {
			return rs.getAsciiStream(columnName);
		} catch (SQLException e) {
			// ignore
		}
		return defaultValue;
	}
	
	/**
	 * {@link ResultSet#getBinaryStream(String)}を使って、指定したカラムインデックスのデータを取り出す。
	 * ただし、取り出し時に {@link SQLException}が発生した場合は、{@code defaultValue}を返す。
	 * 
	 * <p>{@link #getValue(Class, ResultSet, int, Object)}は、{@link ResultSet#getAsciiStream(int)}と
	 * かち合うので使用できない。</p>
	 * 
	 * @param rs {@link ResultSet}
	 * @param columnIndex カラムインデックス
	 * @param defaultValue {@link SQLException}が発生した場合や、該当するgetterが存在しなかった場合のデフォルト値
	 * @return 取り出した値、またはデフォルト値
	 * @throws IllegalArgumentException 引数{@code rs}に{@code null}を与えた場合
	 */
	public static InputStream getBinaryStream(ResultSet rs, int columnIndex, InputStream defaultValue) {
		Validate.notNull(rs);
		try {
			return rs.getBinaryStream(columnIndex);
		} catch (SQLException e) {
			// ignore
		}
		return defaultValue;
	}
	
	/**
	 * {@link ResultSet#getBinaryStream(String)}を使って、指定したカラム名のデータを取り出す。
	 * ただし、取り出し時に {@link SQLException}が発生した場合は、{@code defaultValue}を返す。
	 * 
	 * <p>{@link #getValue(Class, ResultSet, String, Object)}は、{@link ResultSet#getAsciiStream(String)}と
	 * かち合うので使用できない。</p>
	 * 
	 * @param rs {@link ResultSet}
	 * @param columnName カラム名
	 * @param defaultValue {@link SQLException}が発生した場合や、該当するgetterが存在しなかった場合のデフォルト値
	 * @return 取り出した値、またはデフォルト値
	 * @throws IllegalArgumentException 引数{@code rs}に{@code null}を与えた場合
	 */
	public static InputStream getBinaryStream(ResultSet rs, String columnName, InputStream defaultValue) {
		Validate.notNull(rs);
		try {
			return rs.getBinaryStream(columnName);
		} catch (SQLException e) {
			// ignore
		}
		return defaultValue;
	}
	
	/**
	 * {@link ResultSet}から、指定したカラムインデックスのデータを指定した型で取り出す。
	 * ただし、取り出し時に {@link SQLException}が発生した場合、または
	 * 該当するgetterが存在しなかった場合は、{@code defaultValue}を返す。
	 * 
	 * @param <T> 取り出す値の型
	 * @param returnType 取り出す値の型
	 * @param rs 取り出し元の {@link ResultSet}
	 * @param columnIndex カラムインデックス
	 * @param defaultValue {@link SQLException}が発生した場合や、該当するgetterが存在しなかった場合のデフォルト値
	 * @return 取り出した値、またはデフォルト値
	 * @throws IllegalArgumentException 引数{@code clazz}, {@code rs}, {@code columnName}に{@code null}を与えた場合
	 */
	public static <T>T getValue(Class<T> returnType, ResultSet rs, int columnIndex, T defaultValue) {
		Validate.notNull(returnType);
		Validate.notNull(rs);
		return getValueInternal(returnType, rs, int.class, new Object[] {
			columnIndex
		}, defaultValue);
	}
	
	/**
	 * {@link ResultSet}から、指定したカラム名のデータを指定した型で取り出す。
	 * ただし、取り出し時に {@link SQLException}が発生した場合、または
	 * 該当するgetterが存在しなかった場合は、{@code defaultValue}を返す。
	 * 
	 * @param <T> 取り出す値の型
	 * @param returnType 取り出す値の型
	 * @param rs 取り出し元の {@link ResultSet}
	 * @param columnName カラム名
	 * @param defaultValue {@link SQLException}が発生した場合や、該当するgetterが存在しなかった場合のデフォルト値
	 * @return 取り出した値、またはデフォルト値
	 * @throws IllegalArgumentException 引数{@code clazz}, {@code rs}, {@code columnName}に{@code null}を与えた場合
	 */
	public static <T>T getValue(Class<T> returnType, ResultSet rs, String columnName, T defaultValue) {
		Validate.notNull(returnType);
		Validate.notNull(rs);
		Validate.notNull(columnName);
		return getValueInternal(returnType, rs, String.class, new Object[] {
			columnName
		}, defaultValue);
	}
	
	private static Method findMethod(Class<?> returnType, Class<?> parameterType) {
		for (Method method : ResultSet.class.getMethods()) {
			Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes.length == 1 && parameterTypes[0].equals(parameterType)
					&& returnType.equals(method.getReturnType()) && method.getName().startsWith("get")) {
				return method;
			}
		}
		return null;
	}
	
	private static <T>T getValueInternal(Class<T> returnType, ResultSet rs, Class<?> parameterType, Object[] parameter,
			T defaultValue) {
		Method method = findMethod(returnType, parameterType);
		if (method == null) {
			return defaultValue;
		}
		try {
			// bug in JDK http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6456930
//			return clazz.cast(method.invoke(rs, parameter));
			@SuppressWarnings("unchecked")
			T result = (T) method.invoke(rs, parameter);
			return result;
		} catch (IllegalArgumentException e) {
			throw new JiemamyError("The signature of the method must be verified.", e);
		} catch (IllegalAccessException e) {
			throw new JiemamyError("The method returned by getMethods() must be public.", e);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof RuntimeException) {
				throw (RuntimeException) cause;
			}
			if (e.getCause() instanceof SQLException == false) {
				throw new JiemamyError("Checked exception which the invocation target thrown is not SQLException.",
						e.getCause());
			}
			
			// ignore
		}
		return defaultValue;
	}
	
	private ResultSetUtil() {
	}
	
}
