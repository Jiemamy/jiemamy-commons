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
import org.jiemamy.utils.LogMarker;

/**
 * 複数の SQL 文を含む文字列、またはストリームを読み込んで順次実行する。
 * 
 * <p>単一の SQL はセミコロンを終端文字とし、それぞれの SQL をつなぐことで複数の SQL として扱うことができる。</p>
 * 
 * <p>複数の SQL を処理する場合、一つの SQL 文を実行するごとにコミットを行う。実行途中で例外が発生した場合は処理を中断し、
 * その文の実行に関してのみロールバック処理を行い、以後のSQL文は実行しない。引数に {@link SqlExecutorHandler} を持つ
 * execute メソッドは、SQL が実行される毎に {@link SqlExecutorHandler} の各ハンドラメソッドに実行結果を通知する。
 * 通知先となるメソッドは二つあり、検索系の SQL 文が実行された場合は {@link SqlExecutorHandler#handleResultSet(String, ResultSet)}、
 * 更新系の SQL 文が実行された場合は {@link SqlExecutorHandler#handleUpdateCount(String, int)} となる。</p>
 * 
 * <p>なお、{@code SqlExecutor} は {@link java.sql.Statement#getMoreResults()} の処理を考慮していない為、SQL 文を実行した
 * 結果、複数の {@link java.sql.ResultSet} 等の結果が取得される場合でも、最初に取得した結果しかハンドラに通知しない。</p>
 * 
 * <p>下記のコードは、このクラスを使う簡単な例となる。下記のコードを実行することにより、{@code "SELECT ENAME FROM EMP"} と
 * {@code "SELECT DNAME FROM DEPT"} の二つの SQL を実行する。</p>
 * <p><pre><code>
 * String sql = "SELECT ENAME FROM EMP; SELECT DNAME FROM DEPT;";
 * SqlExecutor executor = new SqlExecutor(connection);
 * executor.execute(sql);
 * </code></pre></p>
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
	 * @param connection データベース接続
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public SqlExecutor(Connection connection) {
		Validate.notNull(connection);
		this.connection = connection;
	}
	
	/**
	 * SQLを実行する。
	 * 
	 * <p>複文SQLを処理する場合、1文の実行ごとにコミットを行う。途中で例外が発生した場合は処理を中断し、
	 * その文の実行に関してのみロールバック処理を行い、以後のSQL文は実行しない。</p>
	 * 
	 * @param is SQL文の入力ストリーム。セミコロン区切りの複文を処理することもできる。
	 * @throws SQLException SQLの実行に失敗した場合
	 * @throws IOException SQLデータの取得に失敗した場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void execute(InputStream is) throws SQLException, IOException {
		execute(is, null);
	}
	
	/**
	 * SQLを実行し、結果をハンドリングする。
	 * 
	 * <p>
	 * 複文SQLを処理する場合、SQLの実行毎に {@link SqlExecutorHandler} の各ハンドラがコールバックされる。
	 * また、1文の実行ごとにコミットを行う。途中で例外が発生した場合は処理を中断し、その文の実行に関してのみ
	 * ロールバック処理を行い、以後のSQL文は実行しない。
	 * </p>
	 * 
	 * @param is SQL文の入力ストリーム。セミコロン区切りの複文を処理することもできる。
	 * @param handler SQLの実行結果を受けとるハンドラ。{@code null}の場合は結果をハンドリングしない。
	 * @throws SQLException SQLの実行に失敗した場合
	 * @throws IOException SQLデータの取得に失敗した場合
	 * @throws IllegalArgumentException 引数{@code is}に{@code null}を与えた場合
	 */
	public void execute(InputStream is, SqlExecutorHandler handler) throws SQLException, IOException {
		Validate.notNull(is);
		execute(new InputStreamReader(is), handler);
	}
	
	/**
	 * SQLを実行する。
	 * 
	 * <p>複文SQLを処理する場合、1文の実行ごとにコミットを行う。途中で例外が発生した場合は処理を中断し、
	 * その文の実行に関してのみロールバック処理を行い、以後のSQL文は実行しない。</p>
	 * 
	 * @param in SQL文の入力ストリーム。セミコロン区切りの複文を処理することもできる。
	 * @throws SQLException SQLの実行に失敗した場合
	 * @throws IOException SQLデータの取得に失敗した場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public void execute(Reader in) throws SQLException, IOException {
		execute(in, null);
	}
	
	/**
	 * SQLを実行し、結果をハンドリングする。
	 * 
	 * <p>
	 * 複文SQLを処理する場合、SQLの実行毎に {@link SqlExecutorHandler} の各ハンドラがコールバックされる。
	 * また、1文の実行ごとにコミットを行う。途中で例外が発生した場合は処理を中断し、その文の実行に関してのみ
	 * ロールバック処理を行い、以後のSQL文は実行しない。
	 * </p>
	 * 
	 * @param in SQL文の入力ストリーム。セミコロン区切りの複文を処理することもできる。
	 * @param handler SQLの実行結果を受けとるハンドラ。{@code null}の場合は結果をハンドリングしない。
	 * @throws SQLException SQLの実行に失敗した場合
	 * @throws IOException SQLデータの取得に失敗した場合
	 * @throws IllegalArgumentException 引数{@code in}に{@code null}を与えた場合
	 */
	public void execute(Reader in, SqlExecutorHandler handler) throws SQLException, IOException {
		Validate.notNull(in);
		
		StringBuilder builder = new StringBuilder();
		
		boolean quotedFlag = false;
		boolean execFlag = false;
		for (int ch = in.read(); ch != -1; ch = in.read()) {
			switch (ch) {
				case SINGLEQUOTE:
					quotedFlag = !quotedFlag;
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
				executeSingleSql(builder.toString(), handler);
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
	 * <p>複文SQLを処理する場合、1文の実行ごとにコミットを行う。途中で例外が発生した場合は処理を中断し、
	 * その文の実行に関してのみロールバック処理を行い、以後のSQL文は実行しない。</p>
	 * 
	 * @param sql 実行するSQL。セミコロン区切りの複文を処理することもできる。
	 * @throws SQLException SQLの実行に失敗した場合
	 * @throws IllegalArgumentException 引数{@code sql}に{@code null}を与えた場合
	 */
	public void execute(String sql) throws SQLException {
		execute(sql, null);
	}
	
	/**
	 * SQLを実行し、結果をハンドリングする。
	 * 
	 * <p>
	 * 複文SQLを処理する場合、SQLの実行毎に {@link SqlExecutorHandler} の各ハンドラがコールバックされる。
	 * また、1文の実行ごとにコミットを行う。途中で例外が発生した場合は処理を中断し、その文の実行に関してのみ
	 * ロールバック処理を行い、以後のSQL文は実行しない。
	 * </p>
	 * 
	 * @param sql 実行するSQL。セミコロン区切りの複文を処理することもできる。
	 * @param handler SQLの実行結果を受けとるハンドラ。{@code null}の場合は結果をハンドリングしない。
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
	
	void executeSingleSql(String sql, SqlExecutorHandler handler) throws SQLException {
		logger.info(LogMarker.DETAIL, sql);
		
		boolean isAutoCommit = connection.getAutoCommit();
		connection.setAutoCommit(false);
		
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
			
			if (stmt.execute(sql)) {
				if (handler != null) {
					rs = stmt.getResultSet();
					handler.handleResultSet(sql, rs);
				}
			} else {
				if (handler != null) {
					int count = stmt.getUpdateCount();
					if (count >= 0) {
						handler.handleUpdateCount(sql, count);
					}
				}
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
