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
package org.jiemamy.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQLを実行するクラス。
 * 
 * @author Keisuke.K
 */
public class SqlExecutor {
	
	private static Logger logger = LoggerFactory.getLogger(SqlExecutor.class);
	
	private static final char SINGLEQUOTE = '\'';
	
	private static final char SEMICOLON = ';';
	
	private final Connection connection;
	
	private SqlExecutorHandler handler;
	
	private Reader in;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param connection コネクション
	 */
	public SqlExecutor(Connection connection) {
		Validate.notNull(connection);
		this.connection = connection;
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
				default:
					builder.append((char) ch);
			}
			
			if (execFlag) {
				execute(builder.toString());
				builder.setLength(0);
			}
			
			ch = in.read();
		}
	}
	
	/**
	 * SQLを実行する。
	 * 
	 * @param sql 実行するSQL
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	public void execute(String sql) throws SQLException {
		logger.info(sql);
		
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
		} finally {
			JmIOUtil.closeQuietly(rs);
			JmIOUtil.closeQuietly(stmt);
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
	 * 実行するSQLを保持する入力ストリームをセットする。
	 * 
	 * @param is 実行するSQLを保持する入力ストリーム
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void setInputStream(InputStream is) {
		Validate.notNull(is);
		in = new InputStreamReader(is);
	}
	
	/**
	 * 実行するSQLを保持する入力ストリームをセットする。
	 * 
	 * @param in 実行するSQLを保持する入力ストリーム
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void setReader(Reader in) {
		Validate.notNull(in);
		this.in = in;
	}
	
	@Override
	protected void finalize() throws Throwable {
		IOUtils.closeQuietly(in);
		
		super.finalize();
	}
	

	/**
	 * SQL実行クラスが、1つのSQLを実行後に呼び出すハンドラインタフェース。
	 * 
	 * @author Keisuke.K
	 */
	public interface SqlExecutorHandler {
		
		/**
		 * SQLが実行されると呼び出されるハンドラメソッド。
		 * 
		 * @param sql 実行したSQL
		 * @param rs 実行結果の {@link ResultSet}。SQLの実行結果が {@link ResultSet} とならないSQLの場合、{@code null}。
		 */
		void sqlExecuted(String sql, ResultSet rs);
		
	}
	
}
