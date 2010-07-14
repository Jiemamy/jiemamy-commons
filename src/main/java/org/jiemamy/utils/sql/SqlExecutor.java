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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
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
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param connection コネクション
	 * @throws IllegalArgumentException {@code connection}または{@code in}に{@code null}を与えた場合
	 */
	public SqlExecutor(Connection connection) {
		Validate.notNull(connection);
		this.connection = connection;
	}
	
	/**
	 * SQLを実行する。
	 * 
	 * @param is SQL文の入力ストリーム
	 * @throws SQLException SQLの実行に失敗した場合
	 * @throws IOException SQLデータの取得に失敗した場合
	 * @throws IllegalArgumentException {@code is}に{@code null}を指定した場合
	 */
	public void execute(InputStream is) throws SQLException, IOException {
		execute(is, null);
	}
	
	/**
	 * SQLを実行する。
	 * 
	 * @param is SQL文の入力ストリーム
	 * @param handler SQLの実行結果を受けとるハンドラ
	 * @throws SQLException SQLの実行に失敗した場合
	 * @throws IOException SQLデータの取得に失敗した場合
	 * @throws IllegalArgumentException {@code is}に{@code null}を指定した場合
	 */
	public void execute(InputStream is, SqlExecutorHandler handler) throws SQLException, IOException {
		Validate.notNull(is);
		execute(new InputStreamReader(is), handler);
	}
	
	/**
	 * SQLを実行する。
	 * 
	 * @param in SQL文の入力ストリーム
	 * @throws SQLException SQLの実行に失敗した場合
	 * @throws IOException SQLデータの取得に失敗した場合
	 * @throws IllegalArgumentException {@code in}に{@code null}を指定した場合
	 */
	public void execute(Reader in) throws SQLException, IOException {
		execute(in, null);
	}
	
	/**
	 * SQLを実行する。
	 * 
	 * @param in SQL文の入力ストリーム
	 * @param handler SQLの実行結果を受けとるハンドラ
	 * @throws SQLException SQLの実行に失敗した場合
	 * @throws IOException SQLデータの取得に失敗した場合
	 * @throws IllegalArgumentException {@code in}に{@code null}を指定した場合
	 */
	public void execute(Reader in, SqlExecutorHandler handler) throws SQLException, IOException {
		Validate.notNull(in);
		
		StringBuilder builder = new StringBuilder();
		
		boolean quotedFlag = false;
		boolean execFlag = false;
		for (int ch = in.read(); ch != -1; ch = in.read()) {
			switch (ch) {
				case SINGLEQUOTE:
					quotedFlag ^= true;
					break;
				case SEMICOLON:
					execFlag = !quotedFlag;
					break;
				case SPACE:
					if (builder.length() == 0) {
						continue;
					}
					break;
				default:
			}
			
			if (execFlag) {
				sqlExecute(builder.toString(), handler);
				builder.setLength(0);
				execFlag = false;
			} else {
				builder.append((char) ch);
			}
		}
	}
	
	/**
	 * SQLを実行する。
	 * 
	 * @param sql 実行するSQL
	 * @throws SQLException SQLの実行に失敗した場合
	 * @throws IllegalArgumentException {@code sql}に{@code null}を指定した場合
	 */
	public void execute(String sql) throws SQLException {
		execute(sql, null);
	}
	
	/**
	 * SQLを実行する。
	 * 
	 * @param sql 実行するSQL
	 * @param handler SQLの実行結果を受けとるハンドラ
	 * @throws SQLException SQLの実行に失敗した場合
	 * @throws IllegalArgumentException {@code sql}に{@code null}を指定した場合
	 */
	public void execute(String sql, SqlExecutorHandler handler) throws SQLException {
		Validate.notNull(sql);
		try {
			execute(new StringReader(sql), handler);
		} catch (IOException e) {
			throw new SQLException(e.getMessage());
		}
	}
	
	void sqlExecute(String sql, SqlExecutorHandler handler) throws SQLException {
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
		} finally {
			connection.setAutoCommit(isAutoCommit);
			JmIOUtil.closeQuietly(rs);
			JmIOUtil.closeQuietly(stmt);
		}
	}
	
}
