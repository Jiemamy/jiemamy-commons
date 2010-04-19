/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/03/31
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
package org.jiemamy.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQLを実行するクラス。
 * 
 * @author daisuke
 * @deprecated クラス名のスペルミス。今後は {@link SqlExecutor} を使用してください。
 * @see SqlExecutor
 */
@Deprecated
public class SqlExecuter {
	
	private static Logger logger = LoggerFactory.getLogger(SqlExecuter.class);
	
	private final Connection connection;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param connection コネクション
	 */
	public SqlExecuter(Connection connection) {
		Validate.notNull(connection);
		this.connection = connection;
	}
	
	/**
	 * SQLを実行する。
	 * 
	 * @param sql 実行するSQL
	 * @return 結果の{@link ResultSet}
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	public ResultSet execute(String sql) throws SQLException {
		logger.info(sql);
		
		connection.setAutoCommit(false);
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			if (statement.execute(sql)) {
				resultSet = statement.getResultSet();
			}
			connection.commit();
		} finally {
			JmIOUtil.closeQuietly(statement);
		}
		return resultSet;
	}
	
//	 * @param sql 実行するSQL
//	 */
//	public void executeIgnoreSqlException(String sql) {
//		try {
//			execute(sql);
//		} catch (SQLException e) {
//			logger.warn("SQLException ignored: " + e.getMessage());
//			try {
//				connection.commit();
//			} catch (SQLException e2) {
//				logger.error("SQLException ignored: " + e.getMessage());
//			}
//		}
//	}
}
