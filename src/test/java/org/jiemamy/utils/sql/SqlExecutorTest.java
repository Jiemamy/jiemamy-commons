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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbutils.DbUtils;
import org.h2.Driver;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * {@link SqlExecutor} のテストクラス。
 * 
 * @author Keisuke.K
 */
public class SqlExecutorTest {
	
	/**
	 * テストクラスの初期化。
	 * 
	 * <p>H2ドライバを登録する。</p>
	 * 
	 * @throws java.lang.Exception 例外が発生した場合
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DriverManager.registerDriver(new Driver());
	}
	

	boolean executed;
	
	int count;
	
	Connection conn;
	

	/**
	 * テストの初期化。
	 * 
	 * <p>H2コネクションを取得する。</p>
	 * 
	 * @throws java.lang.Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		conn = DriverManager.getConnection("jdbc:h2:mem:");
		
		// テスト用テーブルの作成
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("CREATE TABLE SQL_EXECUTOR_TEST (KEY INT PRIMARY KEY, VALUE VARCHAR(20))");
		stmt.close();
		conn.commit();
		
		// 各値の初期化
		executed = false;
		count = 0;
	}
	
	/**
	 * テストの終了処理。
	 * 
	 * <p>H2コネクションをクローズする。</p>
	 * 
	 * @throws java.lang.Exception 例外が発生した場合
	 */
	@After
	public void tearDown() throws Exception {
		DbUtils.closeQuietly(conn);
	}
	
	/**
	 * 単純なSQLを実行し、正しくResultSetが取得できるかを確認する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_単純なSQLの実行() throws Exception {
		SqlExecutor executor = new SqlExecutor(conn);
		executor.execute(new StringReader("SELECT 1 FROM DUAL;"), new SqlExecutorHandler() {
			
			public void handleResultSet(String sql, ResultSet rs) throws SQLException {
				assertThat(sql, is("SELECT 1 FROM DUAL"));
				assertThat(rs.next(), is(true));
				assertThat(rs.getInt(1), is(1));
				
				executed = true;
				count++;
			}
			
			public void handleUpdateCount(String sql, int count) {
				fail("更新系のクエリではない");
			}
			
		});
		
		assertThat(executed, is(true));
		assertThat(count, is(1));
	}
	
	/**
	 * 複数のSQLを実行し、正しくResultSetが取得できるかを確認する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_複数のSQLの実行() throws Exception {
		SqlExecutor executor = new SqlExecutor(conn);
		executor.execute(new StringReader("SELECT 1 FROM DUAL; SELECT 2 FROM DUAL;"), new SqlExecutorHandler() {
			
			public void handleResultSet(String sql, ResultSet rs) throws SQLException {
				switch (count) {
					case 0:
						assertThat(sql, is("SELECT 1 FROM DUAL"));
						assertThat(rs.next(), is(true));
						assertThat(rs.getInt(1), is(1));
						break;
					case 1:
						assertThat(sql, is("SELECT 2 FROM DUAL"));
						assertThat(rs.next(), is(true));
						assertThat(rs.getInt(1), is(2));
						break;
					default:
						fail("ここにはこないはず");
				}
				
				executed = true;
				count++;
			}
			
			public void handleUpdateCount(String sql, int count) {
				fail("更新系のクエリではない");
			}
		});
		
		assertThat(executed, is(true));
		assertThat(count, is(2));
	}
	
	/**
	 * セミコロンを含むSQLを実行し、正しくResultSetが取得できるかを確認する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_セミコロンを含むSQLの実行() throws Exception {
		SqlExecutor executor = new SqlExecutor(conn);
		executor.execute(new StringReader("SELECT 'a;b' FROM DUAL; SELECT 'ab' FROM DUAL;"), new SqlExecutorHandler() {
			
			public void handleResultSet(String sql, ResultSet rs) throws SQLException {
				switch (count) {
					case 0:
						assertThat(sql, is("SELECT 'a;b' FROM DUAL"));
						assertThat(rs.next(), is(true));
						assertThat(rs.getString(1), is("a;b"));
						break;
					case 1:
						assertThat(sql, is("SELECT 'ab' FROM DUAL"));
						assertThat(rs.next(), is(true));
						assertThat(rs.getString(1), is("ab"));
						break;
					default:
						fail("ここにはこないはず");
				}
				
				executed = true;
				count++;
			}
			
			public void handleUpdateCount(String sql, int count) {
				fail("更新系のクエリではない");
			}
		});
		
		assertThat(executed, is(true));
		assertThat(count, is(2));
	}
	
	/**
	 * 更新系のSQLが正しく動作するか確認。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test04_更新系SQLの実行() throws Exception {
		SqlExecutor executor = new SqlExecutor(conn);
		executor.execute("INSERT INTO SQL_EXECUTOR_TEST VALUES (1, 'ab');", new SqlExecutorHandler() {
			
			public void handleResultSet(String sql, ResultSet rs) {
				fail("検索系クエリではない");
			}
			
			public void handleUpdateCount(String sql, int count) {
				assertThat(sql, is("INSERT INTO SQL_EXECUTOR_TEST VALUES (1, 'ab')"));
				assertThat(count, is(1));
				
				executed = true;
				SqlExecutorTest.this.count++;
			}
		});
		
		assertThat(executed, is(true));
		assertThat(count, is(1));
	}
	
	/**
	 * エラーとなるSQL文を含む複数のSQLを実行し、例外の発生を確認する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test05_error_SQLの実行() throws Exception {
		SqlExecutor executor = new SqlExecutor(conn);
		try {
			executor.execute(new StringReader("SELECT 1 FROM DUAL; XXX YYY ZZZ; SELECT 2 FROM DUAL;"),
					new SqlExecutorHandler() {
						
						public void handleResultSet(String sql, ResultSet rs) throws SQLException {
							switch (count) {
								case 0:
									assertThat(sql, is("SELECT 1 FROM DUAL"));
									assertThat(rs.next(), is(true));
									assertThat(rs.getInt(1), is(1));
									break;
								case 1:
									assertThat(sql, is("SELECT 2 FROM DUAL"));
									assertThat(rs.next(), is(true));
									assertThat(rs.getInt(1), is(2));
									break;
								default:
									fail("ここにはこないはず");
							}
							
							executed = true;
							count++;
						}
						
						public void handleUpdateCount(String sql, int count) {
							fail("更新系のクエリではない");
						}
					});
			fail();
		} catch (SQLException e) {
			// success
		}
		
		assertThat(executed, is(true));
		assertThat(count, is(1));
	}
}
