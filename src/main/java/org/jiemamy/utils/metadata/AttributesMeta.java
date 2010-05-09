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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.utils.ResultSetUtil;

/**
 * {@link DatabaseMetaData#getAttributes(String, String, String, String)}の結果の一つを表す値クラス。
 * 
 * @author daisuke
 */
public class AttributesMeta {
	
	/** type catalog (may be null) */
	public final String typeCat;
	
	/** type schema (may be null) */
	public final String typeSchem;
	
	/** type name */
	public final String typeName;
	
	/** attribute name */
	public final String attrName;
	
	/** attribute type SQL type from java.sql.Types */
	public final int dataType;
	
	/**
	 * Data source dependent type name. For a UDT, the type name is fully qualified.
	 * For a REF, the type name is fully qualified and represents the target type of the reference type.
	 */
	public final String attrTypeName;
	
	/**
	 * column size. For char or date types this is the maximum number of characters;
	 * for numeric or decimal types this is precision.
	 */
	public final int attrSize;
	
	/** the number of fractional digits */
	public final int decimalDigits;
	
	/** Radix (typically either 10 or 2) */
	public final int numPrecRadix;
	
	/** whether NULL is allowed */
	public final Nullable nullable;
	
	/** comment describing column (may be null) */
	public final String remarks;
	
	/** default value (may be null) */
	public final String attrDef;
	
	/** unused */
	public final int sqlDataType;
	
	/** unused */
	public final int sqlDatetimeSub;
	
	/** for char types the maximum number of bytes in the column */
	public final int charOctetLength;
	
	/** index of column in table (starting at 1) */
	public final int ordinalPosition;
	
	/**
	 * "NO" means column definitely does not allow NULL values; "YES" means the column might allow NULL values.
	 * An empty string means unknown.
	 */
	public final String isNullable;
	
	/** catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF) */
	public final String scopeCatalog;
	
	/** schema of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF) */
	public final String scopeSchema;
	
	/** table name that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)*/
	public final String scopeTable;
	
	/**
	 * source type of a distinct type or user-generated Ref type,SQL type from java.sql.Types
	 * (null if DATA_TYPE isn't DISTINCT or user-generated REF)
	 */
	public final short sourceDataType;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param attribute {@link DatabaseMetaData#getAttributes(String, String, String, String)}の結果{@link ResultSet}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public AttributesMeta(ResultSet attribute) {
		Validate.notNull(attribute);
		typeCat = ResultSetUtil.getString(attribute, "TYPE_CAT");
		typeSchem = ResultSetUtil.getString(attribute, "TYPE_SCHEM");
		typeName = ResultSetUtil.getString(attribute, "TYPE_NAME");
		attrName = ResultSetUtil.getString(attribute, "ATTR_NAME");
		dataType = ResultSetUtil.getInt(attribute, "DATA_TYPE");
		attrTypeName = ResultSetUtil.getString(attribute, "ATTR_TYPE_NAME");
		attrSize = ResultSetUtil.getInt(attribute, "ATTR_SIZE");
		decimalDigits = ResultSetUtil.getInt(attribute, "DECIMAL_DIGITS");
		numPrecRadix = ResultSetUtil.getInt(attribute, "NUM_PREC_RADIX");
		nullable = Nullable.getNullable(ResultSetUtil.getInt(attribute, "NULLABLE"));
		remarks = ResultSetUtil.getString(attribute, "REMARKS");
		attrDef = ResultSetUtil.getString(attribute, "ATTR_DEF");
		sqlDataType = ResultSetUtil.getInt(attribute, "SQL_DATA_TYPE");
		sqlDatetimeSub = ResultSetUtil.getInt(attribute, "SQL_DATETIME_SUB");
		charOctetLength = ResultSetUtil.getInt(attribute, "CHAR_OCTET_LENGTH");
		ordinalPosition = ResultSetUtil.getInt(attribute, "ORDINAL_POSITION");
		isNullable = ResultSetUtil.getString(attribute, "IS_NULLABLE");
		scopeCatalog = ResultSetUtil.getString(attribute, "SCOPE_CATALOG");
		scopeSchema = ResultSetUtil.getString(attribute, "SCOPE_SCHEMA");
		scopeTable = ResultSetUtil.getString(attribute, "SCOPE_TABLE");
		sourceDataType = ResultSetUtil.getShort(attribute, "SOURCE_DATA_TYPE");
		
		assert typeName != null;
		assert attrName != null;
		assert attrTypeName != null;
		assert nullable != null;
		assert isNullable != null;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	

	/**
	 * NULL は許されるかを表す列挙型。
	 * 
	 * @author daisuke
	 */
	public static enum Nullable {
		
		/** might not allow NULL values */
		NO_NULLS(DatabaseMetaData.attributeNoNulls),

		/** definitely allows NULL values */
		NULLABLE(DatabaseMetaData.attributeNullable),

		/** nullability unknown */
		UNKNOWN(DatabaseMetaData.attributeNullableUnknown);
		
		private static Nullable getNullable(int value) {
			return ValueToEnum.get(value);
		}
		

		private final int value;
		

		Nullable(int value) {
			this.value = value;
		}
		

		/**
		 * 逆参照表を遅延初期化するクラス。
		 * 
		 * @author daisuke
		 */
		private static class ValueToEnum {
			
			private static final Map<Integer, Nullable> REVERSE_DICTIONARY;
			static {
				Map<Integer, Nullable> map = new HashMap<Integer, Nullable>();
				for (Nullable elem : Nullable.values()) {
					map.put(elem.value, elem);
				}
				REVERSE_DICTIONARY = Collections.unmodifiableMap(map);
			}
			

			static Nullable get(int key) {
				return REVERSE_DICTIONARY.get(key);
			}
			
			private ValueToEnum() {
			}
		}
		
	}
	
}
