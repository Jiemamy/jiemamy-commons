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
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import org.h2.Driver;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.jiemamy.utils.JmIOUtil;
import org.jiemamy.utils.sql.SqlExecutor;
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
		SqlExecutor executor = new SqlExecutor(conn);
		SqlExecutorHandler handler = new SqlExecutorHandler() {
			
			public void sqlExecuted(String sql, ResultSet rs) {
				assertThat(sql, is("SELECT * FROM DUAL"));
				assertThat(rs, is(notNullValue()));
			}
			
		};
		
		executor.setHandler(handler);
		executor.execute("SELECT * FROM DUAL");
	}
}
