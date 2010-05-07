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
import java.sql.ResultSet;
import java.sql.SQLException;

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
			return rs.getAsciiStream(columnName);
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
	
	private ResultSetUtil() {
	}
	
}
