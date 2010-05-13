/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy.utils;

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
 * @author daisuke
 */
public final class ResultSetUtil {
	
	/**
	 * {@link ResultSet#getAsciiStream(String)}の結果を取得する。
	 * 
	 * @param rs {@link ResultSet}
	 * @param columnName カラム名
	 * @return 結果. {@link SQLException}が発生した場合は{@code null}
	 */
	public static InputStream getAsciiStream(ResultSet rs, String columnName) {
		try {
			return rs.getAsciiStream(columnName);
		} catch (SQLException e) {
			return null;
		}
	}
	
	/**
	 * {@link ResultSet#getBinaryStream(String)}の結果を取得する。
	 * 
	 * @param rs {@link ResultSet}
	 * @param columnName カラム名
	 * @return 結果. {@link SQLException}が発生した場合は{@code null}
	 */
	public static InputStream getBinaryStream(ResultSet rs, String columnName) {
		try {
			return rs.getBinaryStream(columnName);
		} catch (SQLException e) {
			return null;
		}
	}
	
	/**
	 * {@link ResultSet#getBoolean(String)}の結果を取得する。
	 * 
	 * @param rs {@link ResultSet}
	 * @param columnName カラム名
	 * @return 結果. {@link SQLException}が発生した場合は{@code false}
	 */
	public static boolean getBoolean(ResultSet rs, String columnName) {
		try {
			return rs.getBoolean(columnName);
		} catch (SQLException e) {
			return false;
		}
	}
	
	/**
	 * {@link ResultSet#getByte(String)}の結果を取得する。
	 * 
	 * @param rs {@link ResultSet}
	 * @param columnName カラム名
	 * @return 結果. {@link SQLException}が発生した場合は{@code 0}
	 */
	public static byte getByte(ResultSet rs, String columnName) {
		try {
			return rs.getByte(columnName);
		} catch (SQLException e) {
			return 0;
		}
	}
	
	/**
	 * {@link ResultSet#getBytes(String)}の結果を取得する。
	 * 
	 * @param rs {@link ResultSet}
	 * @param columnName カラム名
	 * @return 結果. {@link SQLException}が発生した場合は長さ0の配列
	 */
	public static byte[] getBytes(ResultSet rs, String columnName) {
		try {
			return rs.getBytes(columnName);
		} catch (SQLException e) {
			return new byte[0];
		}
	}
	
	/**
	 * {@link ResultSet#getDate(String)}の結果を取得する。
	 * 
	 * @param rs {@link ResultSet}
	 * @param columnName カラム名
	 * @return 結果. {@link SQLException}が発生した場合は{@code null}
	 */
	public static java.sql.Date getDate(ResultSet rs, String columnName) {
		try {
			return rs.getDate(columnName);
		} catch (SQLException e) {
			return null;
		}
	}
	
	/**
	 * {@link ResultSet#getDouble(String)}の結果を取得する。
	 * 
	 * @param rs {@link ResultSet}
	 * @param columnName カラム名
	 * @return 結果. {@link SQLException}が発生した場合は{@code 0}
	 */
	public static double getDouble(ResultSet rs, String columnName) {
		try {
			return rs.getDouble(columnName);
		} catch (SQLException e) {
			return 0;
		}
	}
	
	/**
	 * {@link ResultSet#getFloat(String)}の結果を取得する。
	 * 
	 * @param rs {@link ResultSet}
	 * @param columnName カラム名
	 * @return 結果. {@link SQLException}が発生した場合は{@code 0}
	 */
	public static float getFloat(ResultSet rs, String columnName) {
		try {
			return rs.getFloat(columnName);
		} catch (SQLException e) {
			return 0;
		}
	}
	
	/**
	 * {@link ResultSet#getInt(String)}の結果を取得する。
	 * 
	 * @param rs {@link ResultSet}
	 * @param columnName カラム名
	 * @return 結果. {@link SQLException}が発生した場合は{@code 0}
	 */
	public static int getInt(ResultSet rs, String columnName) {
		try {
			return rs.getInt(columnName);
		} catch (SQLException e) {
			return 0;
		}
	}
	
	/**
	 * {@link ResultSet#getLong(String)}の結果を取得する。
	 * 
	 * @param rs {@link ResultSet}
	 * @param columnName カラム名
	 * @return 結果. {@link SQLException}が発生した場合は{@code 0}
	 */
	public static long getLong(ResultSet rs, String columnName) {
		try {
			return rs.getLong(columnName);
		} catch (SQLException e) {
			return 0;
		}
	}
	
	/**
	 * {@link ResultSet#getShort(String)}の結果を取得する。
	 * 
	 * @param rs {@link ResultSet}
	 * @param columnName カラム名
	 * @return 結果. {@link SQLException}が発生した場合は{@code 0}
	 */
	public static short getShort(ResultSet rs, String columnName) {
		try {
			return rs.getShort(columnName);
		} catch (SQLException e) {
			return 0;
		}
	}
	
	/**
	 * {@link ResultSet#getString(String)}の結果を取得する。
	 * 
	 * @param rs {@link ResultSet}
	 * @param columnName カラム名
	 * @return 結果. {@link SQLException}が発生した場合は{@code null}
	 */
	public static String getString(ResultSet rs, String columnName) {
		try {
			return rs.getString(columnName);
		} catch (SQLException e) {
			return null;
		}
	}
	
	/**
	 * {@link ResultSet#getTime(String)}の結果を取得する。
	 * 
	 * @param rs {@link ResultSet}
	 * @param columnName カラム名
	 * @return 結果. {@link SQLException}が発生した場合は{@code null}
	 */
	public static java.sql.Time getTime(ResultSet rs, String columnName) {
		try {
			return rs.getTime(columnName);
		} catch (SQLException e) {
			return null;
		}
	}
	
	/**
	 * {@link ResultSet#getTimestamp(String)}の結果を取得する。
	 * 
	 * @param rs {@link ResultSet}
	 * @param columnName カラム名
	 * @return 結果. {@link SQLException}が発生した場合は{@code null}
	 */
	public static java.sql.Timestamp getTimestamp(ResultSet rs, String columnName) {
		try {
			return rs.getTimestamp(columnName);
		} catch (SQLException e) {
			return null;
		}
	}
	
	/**
	 * {@link ResultSet}から、指定したカラム名のデータを指定した型で取り出す。
	 * 
	 * @param <T> 取り出す値の型
	 * @param clazz 取り出す値の型
	 * @param rs 取り出し元の {@link ResultSet}
	 * @param columnName カラム名
	 * @param defaultValue {@link SQLException}が発生した場合のデフォルト値
	 * @return 取り出した値、またはデフォルト値
	 * @throws IllegalArgumentException 引数{@code clazz}, {@code rs}, {@code columnName}に{@code null}を与えた場合
	 */
	public static <T>T getValue(Class<T> clazz, ResultSet rs, String columnName, T defaultValue) {
		Validate.notNull(clazz);
		Validate.notNull(rs);
		Validate.notNull(columnName);
		try {
			Method[] methods = ResultSet.class.getMethods();
			for (Method method : methods) {
				Class<?>[] parameterTypes = method.getParameterTypes();
				if (method.getName().startsWith("get") == false || parameterTypes.length != 1
						|| parameterTypes[0].equals(String.class) == false) {
					continue;
				}
				
				if (clazz.equals(method.getReturnType())) {
					@SuppressWarnings("unchecked")
					T result = (T) method.invoke(rs, new Object[] {
						columnName
					});
					return result;
				}
			}
		} catch (IllegalArgumentException e) {
			throw new JiemamyError("Method#parameterTypes のsizeが1で、それがString.classであることを確認済みであるのにIAEが飛んだ。", e);
		} catch (IllegalAccessException e) {
			throw new JiemamyError("", e);
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof SQLException == false) {
				throw new JiemamyError("unknown", e.getCause());
			}
			// ignore
		}
		return defaultValue;
	}
	
	private ResultSetUtil() {
	}
	
}
