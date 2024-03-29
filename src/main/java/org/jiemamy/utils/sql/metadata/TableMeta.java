/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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
package org.jiemamy.utils.sql.metadata;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.utils.sql.ResultSetUtil;

/**
 * DBから読み出したテーブル情報の保持クラス。
 * 
 * <p>{@link DatabaseMetaData#getTables(String, String, String, String[])}の結果の一つを表す値クラス。</p>
 * 
 * @version $Id$
 * @author daisuke
 */
public class TableMeta {
	
	/** table catalog (may be null) */
	public final String tableCat;
	
	/** table schema (may be null) */
	public final String tableSchem;
	
	/** table name */
	public final String tableName;
	
	/**
	 * table type.
	 * Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
	 */
	public final String tableType;
	
	/** explanatory comment on the table */
	public final String remarks;
	
	/** the types catalog (may be null) */
	public final String typeCat;
	
	/** the types schema (may be null) */
	public final String typeSchem;
	
	/** type name (may be null) */
	public final String typeName;
	
	/** name of the designated "identifier" column of a typed table (may be null) */
	public final String selfReferencingColName;
	
	/** specifies how values in SELF_REFERENCING_COL_NAME are created (may be null) */
	public final RefGeneration refGeneration;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param table {@link DatabaseMetaData#getTables(String, String, String, String[])}の結果{@link ResultSet}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public TableMeta(ResultSet table) {
		Validate.notNull(table);
		tableCat = ResultSetUtil.getValue(String.class, table, "TABLE_CAT", null);
		tableSchem = ResultSetUtil.getValue(String.class, table, "TABLE_SCHEM", null);
		tableName = ResultSetUtil.getValue(String.class, table, "TABLE_NAME", null);
		tableType = ResultSetUtil.getValue(String.class, table, "TABLE_TYPE", null);
		remarks = ResultSetUtil.getValue(String.class, table, "REMARKS", null);
		typeCat = ResultSetUtil.getValue(String.class, table, "TYPE_CAT", null);
		typeSchem = ResultSetUtil.getValue(String.class, table, "TYPE_SCHEM", null);
		typeName = ResultSetUtil.getValue(String.class, table, "TYPE_NAME", null);
		selfReferencingColName = ResultSetUtil.getValue(String.class, table, "SELF_REFERENCING_COL_NAME", null);
		
		String refGenerationString = ResultSetUtil.getValue(String.class, table, "REF_GENERATION", null);
		if (refGenerationString == null) {
			refGeneration = null;
		} else {
			refGeneration = RefGeneration.valueOf(refGenerationString);
		}
		
		assert tableName != null;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	

	/**
	 * ELF_REFERENCING_COL_NAME の値の作成方法を示す列挙型。
	 * @author daisuke
	 */
	public enum RefGeneration {
		/** システムテーブル */
		SYSTEM,

		/** ユーザテーブル */
		USER,

		/** 派生テーブル */
		DERIVED
	}
	
}
