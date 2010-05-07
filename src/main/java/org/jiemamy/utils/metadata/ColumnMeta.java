/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2008/12/25
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
package org.jiemamy.utils.metadata;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.utils.ResultSetUtil;

/**
 * DBから読み出したカラム情報の保持クラス。
 * 
 * <p>{@link DatabaseMetaData#getTables(String, String, String, String[])}の結果の一つを表す値クラス。</p>
 * 
 * @author daisuke
 */
public class ColumnMeta {
	
	/** unused */
	public final int sqlDataType;
	
	/** unused */
	public final int sqlDatetimeSub;
	
	/** table catalog (may be null) */
	public final String tableCat;
	
	/** table schema (may be null) */
	public final String tableSchem;
	
	/** table name */
	public final String tableName;
	
	/** column name */
	public final String columnName;
	
	/** SQL type from java.sql.Types */
	public final int dataType;
	
	/** Data source dependent type name, for a UDT the type name is fully qualified */
	public final String typeName;
	
	/**
	 * column size.
	 * For char or date types this is the maximum number of characters,
	 * for numeric or decimal types this is precision. 
	 */
	public final int columnSize;
	
	/** is not used. */
	public final int bufferLength;
	
	/** the number of fractional digits */
	public final int decimalDigits;
	
	/** Radix (typically either 10 or 2) */
	public final int numPrecRadix;
	
	/** is NULL allowed. */
	public final Nullable nullable;
	
	/** comment describing column (may be null) */
	public final String remarks;
	
	/** default value (may be null) */
	public final String columnDef;
	
	/** for char types the maximum number of bytes in the column */
	public final int charOctetLength;
	
	/** index of column in table (starting at 1) */
	public final int ordinalPosition;
	
	/**
	 * "NO" means column definitely does not allow NULL values;
	 * "YES" means the column might allow NULL values.
	 * An empty string means nobody knows.
	 */
	public final String isNullable;
	
	/** catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF) */
	public final String scopeCatalog;
	
	/** schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF) */
	public final String scopeSchema;
	
	/** table name that this the scope of a reference attribure (null if the DATA_TYPE isn't REF) */
	public final String scopeTable;
	
	/**
	 * source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types
	 * (null if DATA_TYPE isn't DISTINCT or user-generated REF)
	 */
	public final short sourceDataType;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param column {@link DatabaseMetaData#getColumns(String, String, String, String)}の結果{@link ResultSet}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public ColumnMeta(ResultSet column) {
		Validate.notNull(column);
		tableCat = ResultSetUtil.getString(column, "TABLE_CAT");
		tableSchem = ResultSetUtil.getString(column, "TABLE_SCHEM");
		tableName = ResultSetUtil.getString(column, "TABLE_NAME");
		columnName = ResultSetUtil.getString(column, "COLUMN_NAME");
		dataType = ResultSetUtil.getInt(column, "DATA_TYPE");
		typeName = ResultSetUtil.getString(column, "TYPE_NAME");
		columnSize = ResultSetUtil.getInt(column, "COLUMN_SIZE");
		bufferLength = ResultSetUtil.getInt(column, "BUFFER_LENGTH");
		decimalDigits = ResultSetUtil.getInt(column, "DECIMAL_DIGITS");
		numPrecRadix = ResultSetUtil.getInt(column, "NUM_PREC_RADIX");
		nullable = Nullable.getNullable(ResultSetUtil.getShort(column, "NULLABLE"));
		remarks = ResultSetUtil.getString(column, "REMARKS");
		columnDef = ResultSetUtil.getString(column, "COLUMN_DEF");
		sqlDataType = ResultSetUtil.getInt(column, "SQL_DATA_TYPE");
		sqlDatetimeSub = ResultSetUtil.getInt(column, "SQL_DATETIME_SUB");
		charOctetLength = ResultSetUtil.getInt(column, "CHAR_OCTET_LENGTH");
		ordinalPosition = ResultSetUtil.getInt(column, "ORDINAL_POSITION");
		isNullable = ResultSetUtil.getString(column, "IS_NULLABLE"); // THINK enum化？
		// IS_NULLABLE String =>
		//    "NO" means column definitely does not allow NULL values;
		//    "YES" means the column might allow NULL values.
		//    An empty string means nobody knows. 
		
		scopeCatalog = ResultSetUtil.getString(column, "SCOPE_CATLOG");
		scopeSchema = ResultSetUtil.getString(column, "SCOPE_SCHEMA");
		scopeTable = ResultSetUtil.getString(column, "SCOPE_TABLE");
		sourceDataType = ResultSetUtil.getShort(column, "SOURCE_DATA_TYPE");
		
		assert tableName != null;
		assert columnName != null;
		assert typeName != null;
		assert nullable != null;
		assert isNullable != null;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	

	/**
	 * カラムに対してNULLを設定可能かどうかを表す列挙型。
	 * @author daisuke
	 */
	public static enum Nullable {
		
		/** might not allow NULL values */
		NO_NULLS(DatabaseMetaData.columnNoNulls),

		/** definitely allows NULL values */
		NULLABLE(DatabaseMetaData.columnNullable),

		/** nullability unknown */
		UNKNOWN(DatabaseMetaData.columnNullableUnknown);
		
		private static Nullable getNullable(short value) {
			for (Nullable nullable : Nullable.values()) {
				if (nullable.value == value) {
					return nullable;
				}
			}
			return null;
		}
		

		private final int value;
		

		Nullable(int value) {
			this.value = value;
		}
	}
	
}
