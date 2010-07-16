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
 * {@link SqlExecutor} が、一つの SQL を実行する毎に呼び出すハンドラインタフェース。
 * 
 * @version $Id$
 * @author Keisuke.K
 */
public interface SqlExecutorHandler {
	
	/**
	 * {@code SELECT} など、検索系の SQL を実行した際に得られた結果をハンドルする。
	 * 
	 * @param sql 実行した SQL
	 * @param rs 実行結果の {@link ResultSet}。結果がない場合 {@code null}。
	 * @throws SQLException SQL 例外が発生した場合
	 */
	void handleResultSet(String sql, ResultSet rs) throws SQLException;
	
	/**
	 * {@code UPDATE} など、更新系の SQL を実行した際に得られた結果をハンドルする。
	 * 
	 * @param sql 実行した SQL
	 * @param count 実行結果の更新カウント。結果がない場合は {@code -1} となる。
	 */
	void handleUpdateCount(String sql, int count);
	
}
