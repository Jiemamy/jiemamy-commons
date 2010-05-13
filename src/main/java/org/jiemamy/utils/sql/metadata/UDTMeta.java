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
 * {@link DatabaseMetaData#getUDTs(String, String, String, int[])}の結果の一つを表す値クラス。
 * 
 * @author daisuke
 */
public class UDTMeta {
	
	/** the type's catalog (may be null) */
	public final String typeCat;
	
	/** type's schema (may be null) */
	public final String typeSchem;
	
	/** type name */
	public final String typeName;
	
	/** Java class name */
	public final String className;
	
	/** type value defined in java.sql.Types. One of JAVA_OBJECT, STRUCT, or DISTINCT */
	public final int dataType;
	
	/** explanatory comment on the type */
	public final String remarks;
	
	/**
	 * type code of the source type of a DISTINCT type or the type that implements the user-generated reference type
	 * of the SELF_REFERENCING_COLUMN of a structured type as defined in java.sql.Types (null if DATA_TYPE is
	 * not DISTINCT or not STRUCT with REFERENCE_GENERATION = USER_DEFINED)
	 */
	public final short baseType;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param udt {@link DatabaseMetaData#getUDTs(String, String, String, int[])}の結果{@link ResultSet}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public UDTMeta(ResultSet udt) {
		Validate.notNull(udt);
		typeCat = ResultSetUtil.getValue(String.class, udt, "TYPE_CAT", null);
		typeSchem = ResultSetUtil.getValue(String.class, udt, "TYPE_SCHEM", null);
		typeName = ResultSetUtil.getValue(String.class, udt, "TYPE_NAME", null);
		className = ResultSetUtil.getValue(String.class, udt, "CLASS_NAME", null);
		dataType = ResultSetUtil.getValue(int.class, udt, "DATA_TYPE", null);
		remarks = ResultSetUtil.getValue(String.class, udt, "REMARKS", null);
		baseType = ResultSetUtil.getValue(short.class, udt, "BASE_TYPE", null);
		
		assert typeName != null;
		assert className != null;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
