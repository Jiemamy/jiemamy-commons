/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
 * DBから読み出したデータ型情報の保持クラス。
 * 
 * <p>{@link DatabaseMetaData#getTypeInfo()}の結果の一つを表す値クラス。</p>
 * 
 * @author daisuke
 */
public class TypeInfoMeta {
	
	/** Type name */
	public final String typeName;
	
	/** SQL data type from java.sql.Types */
	public final int dataType;
	
	/** maximum precision */
	public final int precision;
	
	/** prefix used to quote a literal (may be null) */
	public final String literalPrefix;
	
	/** suffix used to quote a literal (may be null) */
	public final String literalSuffix;
	
	/** parameters used in creating the type (may be null) */
	public final String createParams;
	
	/** can you use NULL for this type. */
	public final Nullable nullable;
	
	/** is it case sensitive. */
	public final boolean caseSensitive;
	
	/** can you use "WHERE" based on this type */
	public final Searchable searchable;
	
	/** is it unsigned. */
	public final boolean unsignedAttribute;
	
	/** can it be a money value. */
	public final boolean fixedPrecScale;
	
	/** can it be used for an auto-increment value. */
	public final boolean autoIncrement;
	
	/** localized version of type name (may be null) */
	public final String localTypeName;
	
	/** minimum scale supported */
	public final short minimumScale;
	
	/** maximum scale supported */
	public final short maximumScale;
	
	/** unused */
	public final int sqlDataType;
	
	/** unused */
	public final int sqlDatetimeSub;
	
	/** usually 2 or 10 */
	public final int numPrecRadix;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param typeInfo {@link DatabaseMetaData#getTypeInfo()}の結果{@link ResultSet}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public TypeInfoMeta(ResultSet typeInfo) {
		Validate.notNull(typeInfo);
		typeName = ResultSetUtil.getString(typeInfo, "TYPE_NAME");
		dataType = ResultSetUtil.getInt(typeInfo, "DATA_TYPE");
		precision = ResultSetUtil.getInt(typeInfo, "PRECISION");
		literalPrefix = ResultSetUtil.getString(typeInfo, "LITERAL_PREFIX");
		literalSuffix = ResultSetUtil.getString(typeInfo, "LITERAL_SUFFIX");
		createParams = ResultSetUtil.getString(typeInfo, "CREATE_PARAMS");
		nullable = Nullable.getNullable(ResultSetUtil.getShort(typeInfo, "NULLABLE"));
		caseSensitive = ResultSetUtil.getBoolean(typeInfo, "CASE_SENSITIVE");
		searchable = Searchable.getSearchable(ResultSetUtil.getShort(typeInfo, "SEARCHABLE"));
		unsignedAttribute = ResultSetUtil.getBoolean(typeInfo, "UNSIGNED_ATTRIBUTE");
		fixedPrecScale = ResultSetUtil.getBoolean(typeInfo, "FIXED_PREC_SCALE");
		autoIncrement = ResultSetUtil.getBoolean(typeInfo, "AUTO_INCREMENT");
		localTypeName = ResultSetUtil.getString(typeInfo, "LOCAL_TYPE_NAME");
		minimumScale = ResultSetUtil.getShort(typeInfo, "MINIMUM_SCALE");
		maximumScale = ResultSetUtil.getShort(typeInfo, "MAXIMUM_SCALE");
		sqlDataType = ResultSetUtil.getInt(typeInfo, "SQL_DATA_TYPE");
		sqlDatetimeSub = ResultSetUtil.getInt(typeInfo, "SQL_DATETIME_SUB");
		numPrecRadix = ResultSetUtil.getInt(typeInfo, "NUM_PREC_RADIX");
		
		assert typeName != null;
		assert nullable != null;
		assert searchable != null;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	

	/**
	 * データ型に対してNULLを設定可能かどうかを表す列挙型。
	 * @author daisuke
	 */
	public static enum Nullable {
		
		/** does not allow NULL values */
		NO_NULLS(DatabaseMetaData.typeNoNulls),

		/** allows NULL values */
		NULLABLE(DatabaseMetaData.typeNullable),

		/** nullability unknown */
		UNKNOWN(DatabaseMetaData.typeNullableUnknown);
		
		/**
		 * {@link Nullable}の値を取得する。
		 * @param value {@link DatabaseMetaData}から取得される値
		 * @return {@link Nullable}
		 */
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
	
	/**
	 * can you use "WHERE" based on this type.
	 * 
	 * <p>THINK 何のこっちゃ。</p>
	 * 
	 * @author daisuke
	 */
	public static enum Searchable {
		
		/** No support */
		NONE(DatabaseMetaData.typePredNone),

		/** Only supported with WHERE .. LIKE */
		CHAR(DatabaseMetaData.typePredChar),

		/** Supported except for WHERE .. LIKE */
		BASIC(DatabaseMetaData.typePredBasic),

		/** Supported for all WHERE .. */
		SEARCHABLE(DatabaseMetaData.typeSearchable);
		
		/**
		 * {@link Searchable}の値を取得する。
		 * @param value {@link DatabaseMetaData}から取得される値
		 * @return {@link Searchable}
		 */
		private static Searchable getSearchable(short value) {
			for (Searchable searchable : Searchable.values()) {
				if (searchable.value == value) {
					return searchable;
				}
			}
			return null;
		}
		

		private int value;
		

		Searchable(int value) {
			this.value = value;
		}
		
	}
	
}
