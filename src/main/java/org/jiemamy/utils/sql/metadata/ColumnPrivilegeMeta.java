/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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
 * {@link DatabaseMetaData#getColumnPrivileges(String, String, String, String)}の結果の一つを表す値クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class ColumnPrivilegeMeta {
	
	/** table catalog (may be null) */
	public final String tableCat;
	
	/** table schema (may be null) */
	public final String tableSchem;
	
	/** table name */
	public final String tableName;
	
	/** column name */
	public final String columnName;
	
	/** grantor of access (may be null) */
	public final String grantor;
	
	/** grantee of access */
	public final String grantee;
	
	/** name of access (SELECT, INSERT, UPDATE, REFRENCES, ...) */
	public final String privilege;
	
	/** "YES" if grantee is permitted to grant to others; "NO" if not; null if unknown */
	public final String isGrantable;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param columnPrivileges {@link DatabaseMetaData#getColumnPrivileges(String, String, String, String)}の結果{@link ResultSet}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public ColumnPrivilegeMeta(ResultSet columnPrivileges) {
		Validate.notNull(columnPrivileges);
		tableCat = ResultSetUtil.getValue(String.class, columnPrivileges, "TABLE_CAT", null);
		tableSchem = ResultSetUtil.getValue(String.class, columnPrivileges, "TABLE_SCHEM", null);
		tableName = ResultSetUtil.getValue(String.class, columnPrivileges, "TABLE_NAME", null);
		columnName = ResultSetUtil.getValue(String.class, columnPrivileges, "COLUMN_NAME", null);
		grantor = ResultSetUtil.getValue(String.class, columnPrivileges, "GRANTOR", null);
		grantee = ResultSetUtil.getValue(String.class, columnPrivileges, "GRANTEE", null);
		privilege = ResultSetUtil.getValue(String.class, columnPrivileges, "PRIVILEGE", null);
		isGrantable = ResultSetUtil.getValue(String.class, columnPrivileges, "IS_GRANTABLE", null);
		
		assert tableName != null;
		assert columnName != null;
		assert grantee != null;
		assert privilege != null;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
