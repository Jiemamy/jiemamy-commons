/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2009/05/22
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

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.utils.JmIOUtil;

/**
 * SQLを実行するクラス。
 * 
 * @author Keisuke.K
 */
public class SqlExecutor {
	
	private static Logger logger = LoggerFactory.getLogger(SqlExecutor.class);
	
	static final char SINGLEQUOTE = '\'';
	
	static final char SEMICOLON = ';';
	
	static final char SPACE = ' ';
	
	final Connection connection;
	
	SqlExecutorHandler handler;
	
	Reader in;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param connection コネクション
	 * @param in SQL文入力ストリーム
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SqlExecutor(Connection connection, Reader in) {
		Validate.notNull(connection);
		Validate.notNull(in);
		this.connection = connection;
		this.in = in;
	}
	
	/**
	 * SQLを実行する。
	 * 
	 * @throws SQLException SQLの実行に失敗した場合
	 * @throws IOException SQLデータの取得に失敗した場合
	 */
	public void execute() throws SQLException, IOException {
		StringBuilder builder = new StringBuilder();
		
		boolean quotedFlag = false;
		boolean execFlag = false;
		int ch = in.read();
		while (ch != -1) {
			switch (ch) {
				case SINGLEQUOTE:
					quotedFlag ^= true;
					break;
				case SEMICOLON:
					execFlag = !quotedFlag;
					break;
				case SPACE:
					if (builder.length() == 0) {
						break;
					}
					builder.append((char) ch);
					break;
				default:
					builder.append((char) ch);
			}
			
			if (execFlag) {
				execute(builder.toString());
				builder.setLength(0);
				execFlag = false;
			}
			
			ch = in.read();
		}
	}
	
	/**
	 * SQL実行後に呼び出すハンドラをセットする。
	 * 
	 * @param handler ハンドラ
	 */
	public void setHandler(SqlExecutorHandler handler) {
		this.handler = handler;
	}
	
	/**
	 * SQLを実行する。
	 * 
	 * @param sql 実行するSQL
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	void execute(String sql) throws SQLException {
		logger.info(sql);
		
		boolean isAutoCommit = connection.getAutoCommit();
		connection.setAutoCommit(false);
		
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
			
			if (stmt.execute(sql)) {
				rs = stmt.getResultSet();
			}
			
			if (handler != null) {
				handler.sqlExecuted(sql, rs);
			}
			
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			connection.setAutoCommit(isAutoCommit);
		} finally {
			JmIOUtil.closeQuietly(rs);
			JmIOUtil.closeQuietly(stmt);
		}
	}
	

	/**
	 * SQL実行クラスが、1つのSQLを実行後に呼び出すハンドラインタフェース。
	 * 
	 * @author Keisuke.K
	 */
	public static interface SqlExecutorHandler {
		
		/**
		 * SQLが実行されると呼び出されるハンドラメソッド。
		 * 
		 * @param sql 実行したSQL
		 * @param rs 実行結果の {@link ResultSet}。SQLの実行結果が {@link ResultSet} とならないSQLの場合、{@code null}。
		 * @throws SQLException SQL例外が発生した場合
		 */
		void sqlExecuted(String sql, ResultSet rs) throws SQLException;
		
	}
	
}
