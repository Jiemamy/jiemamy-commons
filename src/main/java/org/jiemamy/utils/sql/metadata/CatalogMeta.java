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
 * {@link DatabaseMetaData#getCatalogs()}の結果の一つを表す値クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class CatalogMeta {
	
	/** catalog name */
	public final String tableCat;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param catalogs {@link DatabaseMetaData#getCatalogs()}の結果{@link ResultSet}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public CatalogMeta(ResultSet catalogs) {
		Validate.notNull(catalogs);
		tableCat = ResultSetUtil.getValue(String.class, catalogs, "TABLE_CAT", null);
		
		assert tableCat != null;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
