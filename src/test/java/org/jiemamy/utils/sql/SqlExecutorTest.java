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

import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.h2.Driver;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.jiemamy.utils.JmIOUtil;
import org.jiemamy.utils.sql.SqlExecutor.SqlExecutorHandler;

/**
 * {@link SqlExecutor} のテストクラス。
 * 
 * @author Keisuke.K
 */
public class SqlExecutorTest {
	
	/**
	 * テストクラスの初期化。
	 * 
	 * <p>
	 * H2ドライバを登録する。
	 * </p>
	 * 
	 * @throws java.lang.Exception 例外が発生した場合
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DriverManager.registerDriver(new Driver());
	}
	

	private Connection conn;
	

	/**
	 * テストの初期化。
	 * 
	 * <p>
	 * H2コネクションを取得する。
	 * </p>
	 * 
	 * @throws java.lang.Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		conn = DriverManager.getConnection("jdbc:h2:./target/h2database/test", "sa", "");
	}
	
	/**
	 * テストの終了処理。
	 * 
	 * <p>
	 * H2コネクションをクローズする。
	 * </p>
	 * 
	 * @throws java.lang.Exception 例外が発生した場合
	 */
	@After
	public void tearDown() throws Exception {
		JmIOUtil.closeQuietly(conn);
	}
	
	/**
	 * 単純なSQLを実行し、正しくResultSetが取得できるかを確認する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_単純なSQLの実行() throws Exception {
		Reader in = new StringReader("SELECT 1 FROM DUAL;");
		SqlExecutor executor = new SqlExecutor(conn, in);
		
		executor.setHandler(new SqlExecutorHandler() {
			
			public void sqlExecuted(String sql, ResultSet rs) throws SQLException {
				assertThat(sql, is("SELECT 1 FROM DUAL"));
				assertThat(rs.next(), is(true));
				assertThat(rs.getInt(1), is(1));
			}
			
		});
		executor.execute();
	}
	
	/**
	 * 複数のSQLを実行し、正しくResultSetが取得できるかを確認する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_複数のSQLの実行() throws Exception {
		Reader in = new StringReader("SELECT 1 FROM DUAL; SELECT 2 FROM DUAL;");
		SqlExecutor executor = new SqlExecutor(conn, in);
		
		executor.setHandler(new SqlExecutorHandler() {
			
			int count = 0;
			

			public void sqlExecuted(String sql, ResultSet rs) throws SQLException {
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
				count++;
			}
		});
		executor.execute();
	}
	
	/**
	 * セミコロンを含むSQLを実行し、正しくResultSetが取得できるかを確認する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_セミコロンを含むSQLの実行() throws Exception {
		Reader in = new StringReader("SELECT ';' FROM DUAL; SELECT 'a' FROM DUAL;");
		SqlExecutor executor = new SqlExecutor(conn, in);
		
		executor.setHandler(new SqlExecutorHandler() {
			
			int count = 0;
			

			public void sqlExecuted(String sql, ResultSet rs) throws SQLException {
				switch (count) {
					case 0:
						assertThat(sql, is("SELECT ';' FROM DUAL"));
						assertThat(rs.next(), is(true));
						assertThat(rs.getString(1), is(";"));
						break;
					case 1:
						assertThat(sql, is("SELECT 'a' FROM DUAL"));
						assertThat(rs.next(), is(true));
						assertThat(rs.getString(1), is("a"));
						break;
					default:
						fail("ここにはこないはず");
				}
				count++;
			}
		});
		executor.execute();
	}
	
}
