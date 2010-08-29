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
 * {@link DatabaseMetaData#getPrimaryKeys(String, String, String)}の結果の一つを表す値クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class PrimaryKeyMeta {
	
	/** table catalog (may be null) */
	public final String tableCat;
	
	/** table schema (may be null) */
	public final String tableSchem;
	
	/** table name */
	public final String tableName;
	
	/** column name */
	public final String columnName;
	
	/** sequence number within primary key */
	public final short keySeq;
	
	/** primary key name (may be null) */
	public final String pkName;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param primaryKey {@link DatabaseMetaData#getPrimaryKeys(String, String, String)}の結果{@link ResultSet}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public PrimaryKeyMeta(ResultSet primaryKey) {
		Validate.notNull(primaryKey);
		tableCat = ResultSetUtil.getValue(String.class, primaryKey, "TABLE_CAT", null);
		tableSchem = ResultSetUtil.getValue(String.class, primaryKey, "TABLE_SCHEM", null);
		tableName = ResultSetUtil.getValue(String.class, primaryKey, "TABLE_NAME", null);
		columnName = ResultSetUtil.getValue(String.class, primaryKey, "COLUMN_NAME", null);
		keySeq = ResultSetUtil.getValue(short.class, primaryKey, "KEY_SEQ", (short) 0);
		pkName = ResultSetUtil.getValue(String.class, primaryKey, "PK_NAME", null);
		
		assert tableName != null;
		assert columnName != null;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
