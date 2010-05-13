/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/13
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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

/**
 * {@link JmIOUtil}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class JmIOUtilTest {
	
	/**
	 * {@link JmIOUtil#closeQuietly(Connection)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_closeQuietly_Connection() throws Exception {
		Connection connection = null;
		JmIOUtil.closeQuietly(connection);
		
		connection = mock(Connection.class);
		doNothing().when(connection).close();
		JmIOUtil.closeQuietly(connection);
		verify(connection, only()).close();
		
		connection = mock(Connection.class);
		doThrow(new SQLException()).when(connection).close();
		JmIOUtil.closeQuietly(connection);
		verify(connection, only()).close();
	}
	
	/**
	 * {@link JmIOUtil#closeQuietly(ResultSet)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_closeQuietly_ResultSet() throws Exception {
		ResultSet rs = null;
		JmIOUtil.closeQuietly(rs);
		
		rs = mock(ResultSet.class);
		doNothing().when(rs).close();
		JmIOUtil.closeQuietly(rs);
		verify(rs, only()).close();
		
		rs = mock(ResultSet.class);
		doThrow(new SQLException()).when(rs).close();
		JmIOUtil.closeQuietly(rs);
		verify(rs, only()).close();
	}
	
	/**
	 * {@link JmIOUtil#closeQuietly(Statement)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_closeQuietly_Statement() throws Exception {
		Statement connection = null;
		JmIOUtil.closeQuietly(connection);
		
		connection = mock(Statement.class);
		doNothing().when(connection).close();
		JmIOUtil.closeQuietly(connection);
		verify(connection, only()).close();
		
		connection = mock(Statement.class);
		doThrow(new SQLException()).when(connection).close();
		JmIOUtil.closeQuietly(connection);
		verify(connection, only()).close();
	}
	
	/**
	 * {@link JmIOUtil#flushQuietly(OutputStream)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_flushQuietly() throws Exception {
		OutputStream out = null;
		JmIOUtil.flushQuietly(out);
		
		out = mock(OutputStream.class);
		doNothing().when(out).flush();
		JmIOUtil.flushQuietly(out);
		verify(out, only()).flush();
		
		out = mock(OutputStream.class);
		doThrow(new IOException()).when(out).flush();
		JmIOUtil.flushQuietly(out);
		verify(out, only()).flush();
	}
}
