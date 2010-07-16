/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/07/15
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
package org.jiemamy.utils.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@link SqlExecutor}が、1つのSQLを実行後に呼び出すハンドラインタフェース。
 * 
 * @version $Id$
 * @author Keisuke.K
 */
public interface SqlExecutorHandler {
	
	/**
	 * SQLが実行されると呼び出されるハンドラメソッド。
	 * 
	 * @param sql 実行したSQL
	 * @param rs 実行結果の {@link ResultSet}。SQLの実行結果が {@link ResultSet} とならないSQLの場合、{@code null}。
	 * @throws SQLException SQL例外が発生した場合
	 */
	void handleResultSet(String sql, ResultSet rs) throws SQLException;
	
	/**
	 * SQLを実行した結果、更新カウントを取得できた場合に呼び出されるハンドラメソッド。
	 * 
	 * <p>
	 * UPDATEやINSERT等の更新系クエリを実行した場合に呼び出され、処理的には{@link java.sql.Statement#getUpdateCount()}の
	 * 結果が{@code -1}ではないときに、その結果が{@code count}として渡される。
	 * </p>
	 * 
	 * @param sql 実行したSQL
	 * @param count 実行結果の更新カウント
	 */
	void handleUpdateCount(String sql, int count);
	
}
