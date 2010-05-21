/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2008/12/30
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
package org.jiemamy.utils.sql.metadata;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.utils.sql.ResultSetUtil;

/**
 * {@link DatabaseMetaData#getProcedureColumns(String, String, String, String)}の結果の一つを表す値クラス。
 * 
 * @author daisuke
 */
public class ProcedureColumnsMeta {
	
	/** procedure catalog (may be null) */
	public final String procedureCat;
	
	/** procedure schema (may be null) */
	public final String procedureSchem;
	
	/** procedure name */
	public final String procedureName;
	
	/** column/parameter name */
	public final String columnName;
	
	/** kind of column/parameter */
	public final ColumnType columnType;
	
	/** SQL type from java.sql.Types */
	public final int dataType;
	
	/** SQL type name, for a UDT type the type name is fully qualified */
	public final String typeName;
	
	/** precision */
	public final int precision;
	
	/** length in bytes of data */
	public final int length;
	
	/** scale */
	public final short scale;
	
	/** radix */
	public final short radix;
	
	/** can it contain NULL. */
	public final Nullable nullable;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param procedureColumn {@link DatabaseMetaData#getProcedureColumns(String, String, String, String)}の結果{@link ResultSet}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	ProcedureColumnsMeta(ResultSet procedureColumn) {
		Validate.notNull(procedureColumn);
		procedureCat = ResultSetUtil.getValue(String.class, procedureColumn, "PROCEDURE_CAT", null);
		procedureSchem = ResultSetUtil.getValue(String.class, procedureColumn, "PROCEDURE_SCHEM", null);
		procedureName = ResultSetUtil.getValue(String.class, procedureColumn, "PROCEDURE_NAME", null);
		columnName = ResultSetUtil.getValue(String.class, procedureColumn, "COLUMN_NAME", null);
		columnType =
				ColumnType.getColumnType(ResultSetUtil.getValue(short.class, procedureColumn, "COLUMN_TYPE", null));
		dataType = ResultSetUtil.getValue(int.class, procedureColumn, "DATA_TYPE", 0);
		typeName = ResultSetUtil.getValue(String.class, procedureColumn, "TYPE_NAME", null);
		precision = ResultSetUtil.getValue(int.class, procedureColumn, "PRECISION", 0);
		length = ResultSetUtil.getValue(int.class, procedureColumn, "LENGTH", 0);
		scale = ResultSetUtil.getValue(short.class, procedureColumn, "SCALE", (short) 0);
		radix = ResultSetUtil.getValue(short.class, procedureColumn, "RADIX", (short) 0);
		nullable = Nullable.getNullable(ResultSetUtil.getValue(short.class, procedureColumn, "NULLABLE", (short) 0));
		
		assert procedureName != null;
		assert columnName != null;
		assert columnType != null;
		assert typeName != null;
		assert nullable != null;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	

	/**
	 * 列の種類/パラメータを表す列挙型。
	 * @author daisuke
	 */
	public static enum ColumnType {
		
		/** nobody knows */
		UNKNOWN(DatabaseMetaData.procedureColumnUnknown),

		/** IN parameter */
		IN(DatabaseMetaData.procedureColumnIn),

		/** INOUT parameter */
		INOUT(DatabaseMetaData.procedureColumnInOut),

		/** OUT parameter */
		OUT(DatabaseMetaData.procedureColumnOut),

		/** procedure return value */
		RETURN(DatabaseMetaData.procedureColumnReturn),

		/** result column in ResultSet */
		RESULT(DatabaseMetaData.procedureColumnResult);
		
		private static ColumnType getColumnType(short value) {
			for (ColumnType columnType : ColumnType.values()) {
				if (columnType.value == value) {
					return columnType;
				}
			}
			return null;
		}
		

		private int value;
		

		ColumnType(int value) {
			this.value = value;
		}
	}
	
	/**
	 * NULL を含めることができるかを表す列挙型。
	 * @author daisuke
	 */
	public static enum Nullable {
		
		/** does not allow NULL values */
		NO_NULLS(DatabaseMetaData.procedureNoNulls),

		/** allows NULL values */
		NULLABLE(DatabaseMetaData.procedureNullable),

		/** nullability unknown */
		UNKNOWN(DatabaseMetaData.procedureNullableUnknown);
		
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
