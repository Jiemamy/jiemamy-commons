/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2012/01/14
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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.apache.commons.lang.Validate;

/**
 * {@link DatabaseMetaData}を利用するユーティリティクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class MetadataUtil {
	
	/**
	 * 指定した名前のテーブルが存在するかどうかを返す。
	 * 
	 * @param name テーブル名
	 * @param conn コネクション
	 * @return 存在する場合は{@code true}、そうでない場合は{@code false}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws SQLException
	 */
	public static boolean isExistTable(String name, Connection conn) throws SQLException {
		Validate.notNull(conn);
		Validate.notNull(name);
		TypeSafeDatabaseMetaData metadata = new TypeSafeDatabaseMetaData(conn);
		TypeSafeResultSet<TableMeta> tables = metadata.getTables(null, null, name, null);
		return tables.next();
	}
	
	private MetadataUtil() {
	}
	
}
