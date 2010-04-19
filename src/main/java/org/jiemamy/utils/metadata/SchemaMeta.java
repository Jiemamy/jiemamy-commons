/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
 * {@link DatabaseMetaData#getSchemas()}の結果の一つを表す値クラス。
 * 
 * @author daisuke
 */
public class SchemaMeta {
	
	/** schema name */
	public final String tableSchem;
	
	/** catalog name (may be null) */
	public final String tableCat;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param schema {@link DatabaseMetaData#getSchemas()}の結果{@link ResultSet}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SchemaMeta(ResultSet schema) {
		Validate.notNull(schema);
		tableSchem = ResultSetUtil.getString(schema, "TABLE_SCHEM");
		tableCat = ResultSetUtil.getString(schema, "TABLE_CAT");
		
		assert tableSchem != null;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
