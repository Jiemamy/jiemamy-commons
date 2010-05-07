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
package org.jiemamy.utils.metadata;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.utils.ResultSetUtil;

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
	public ProcedureColumnsMeta(ResultSet procedureColumn) {
		Validate.notNull(procedureColumn);
		procedureCat = ResultSetUtil.getString(procedureColumn, "PROCEDURE_CAT");
		procedureSchem = ResultSetUtil.getString(procedureColumn, "PROCEDURE_SCHEM");
		procedureName = ResultSetUtil.getString(procedureColumn, "PROCEDURE_NAME");
		columnName = ResultSetUtil.getString(procedureColumn, "COLUMN_NAME");
		columnType = ColumnType.getColumnType(ResultSetUtil.getShort(procedureColumn, "COLUMN_TYPE"));
		dataType = ResultSetUtil.getInt(procedureColumn, "DATA_TYPE");
		typeName = ResultSetUtil.getString(procedureColumn, "TYPE_NAME");
		precision = ResultSetUtil.getInt(procedureColumn, "PRECISION");
		length = ResultSetUtil.getInt(procedureColumn, "LENGTH");
		scale = ResultSetUtil.getShort(procedureColumn, "SCALE");
		radix = ResultSetUtil.getShort(procedureColumn, "RADIX");
		nullable = Nullable.getNullable(ResultSetUtil.getShort(procedureColumn, "NULLABLE"));
		
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
