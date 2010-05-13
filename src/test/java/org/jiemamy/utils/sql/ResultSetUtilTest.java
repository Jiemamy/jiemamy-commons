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
package org.jiemamy.utils.sql;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import org.jiemamy.utils.sql.ResultSetUtil;

/**
 * {@link ResultSetUtil}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class ResultSetUtilTest {
	
	private static final String COL = "COL";
	

	/**
	 * {@link ResultSet#getBoolean(String)}が正常に呼ばれ、デフォルト値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getBoolean_byIndex_usingDefault() throws Exception {
		ResultSet mock = mock(ResultSet.class);
		when(mock.getBoolean(99)).thenThrow(new SQLException());
		
		assertThat(ResultSetUtil.getValue(boolean.class, mock, 99, false), is(false));
		
		// getBooleanが引数COLで、1回だけ呼ばれる
		verify(mock, only()).getBoolean(eq(99));
	}
	
	/**
	 * {@link ResultSet#getBoolean(int)}が正常に呼ばれ、その戻り値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getBoolean_byIndex_usingResult() throws Exception {
		ResultSet mock = mock(ResultSet.class);
		when(mock.getBoolean(99)).thenReturn(true);
		
		assertThat(ResultSetUtil.getValue(boolean.class, mock, 99, false), is(true));
		
		// getBooleanが引数COLで、1回だけ呼ばれる
		verify(mock, only()).getBoolean(eq(99));
	}
	
	/**
	 * {@link ResultSet#getBoolean(String)}が正常に呼ばれ、デフォルト値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getBoolean_byName_usingDefault() throws Exception {
		ResultSet mock = mock(ResultSet.class);
		when(mock.getBoolean(COL)).thenThrow(new SQLException());
		
		assertThat(ResultSetUtil.getValue(boolean.class, mock, COL, false), is(false));
		
		// getBooleanが引数COLで、1回だけ呼ばれる
		verify(mock, only()).getBoolean(eq(COL));
	}
	
	/**
	 * {@link ResultSet#getBoolean(String)}が正常に呼ばれ、その戻り値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getBoolean_byName_usingResult() throws Exception {
		ResultSet mock = mock(ResultSet.class);
		when(mock.getBoolean(COL)).thenReturn(true);
		
		assertThat(ResultSetUtil.getValue(boolean.class, mock, COL, false), is(true));
		
		// getBooleanが引数COLで、1回だけ呼ばれる
		verify(mock, only()).getBoolean(eq(COL));
	}
	
	/**
	 * {@link ResultSet#getByte(String)}が正常に呼ばれ、デフォルト値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getByte_byName_usingDefault() throws Exception {
		ResultSet mock = mock(ResultSet.class);
		when(mock.getByte(COL)).thenThrow(new SQLException());
		
		assertThat(ResultSetUtil.getValue(byte.class, mock, COL, Byte.MIN_VALUE), is(Byte.MIN_VALUE));
		
		// getByteが引数COLで、1回だけ呼ばれる
		verify(mock, only()).getByte(COL);
	}
	
	/**
	 * {@link ResultSet#getByte(String)}が正常に呼ばれ、その戻り値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getByte_byName_usingResult() throws Exception {
		ResultSet mock = mock(ResultSet.class);
		when(mock.getByte(COL)).thenReturn(Byte.MAX_VALUE);
		
		assertThat(ResultSetUtil.getValue(byte.class, mock, COL, Byte.MIN_VALUE), is(Byte.MAX_VALUE));
		
		// getByteが引数COLで、1回だけ呼ばれる
		verify(mock, only()).getByte(COL);
	}
	
	/**
	 * {@link ResultSet#getString(String)}が正常に呼ばれ、デフォルト値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getBytes_byName_usingDefault() throws Exception {
		byte[] defaultValue = new byte[] {
			3,
			4
		};
		ResultSet mock = mock(ResultSet.class);
		when(mock.getBytes(COL)).thenThrow(new SQLException());
		
		assertThat(ResultSetUtil.getValue(byte[].class, mock, COL, defaultValue), is(defaultValue));
		
		// getByteが引数COLで、1回だけ呼ばれる
		verify(mock, only()).getBytes(COL);
	}
	
	/**
	 * {@link ResultSet#getString(String)}が正常に呼ばれ、その戻り値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getBytes_byName_usingResult() throws Exception {
		byte[] returnValue = new byte[] {
			1,
			2
		};
		byte[] defaultValue = new byte[] {
			3,
			4
		};
		ResultSet mock = mock(ResultSet.class);
		when(mock.getBytes(COL)).thenReturn(returnValue);
		
		assertThat(ResultSetUtil.getValue(byte[].class, mock, COL, defaultValue), is(returnValue));
		
		// getByteが引数COLで、1回だけ呼ばれる
		verify(mock, only()).getBytes(COL);
	}
	
	/**
	 * {@link ResultSet#getString(String)}が正常に呼ばれ、デフォルト値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getString_byName_usingDefault() throws Exception {
		ResultSet mock = mock(ResultSet.class);
		when(mock.getString(COL)).thenThrow(new SQLException());
		
		assertThat(ResultSetUtil.getValue(String.class, mock, COL, "foo"), is("foo"));
		
		// getByteが引数COLで、1回だけ呼ばれる
		verify(mock, only()).getString(COL);
	}
	
	/**
	 * {@link ResultSet#getString(String)}が正常に呼ばれ、その戻り値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getString_byName_usingResult() throws Exception {
		ResultSet mock = mock(ResultSet.class);
		when(mock.getString(COL)).thenReturn("bar");
		
		assertThat(ResultSetUtil.getValue(String.class, mock, COL, "foo"), is("bar"));
		
		// getByteが引数COLで、1回だけ呼ばれる
		verify(mock, only()).getString(COL);
	}
	
	/**
	 * {@link ResultSet}のメソッドは一度も呼ばれず、デフォルト値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getUnknown_byIndex_usingDefault() throws Exception {
		ResultSet mock = mock(ResultSet.class);
		
		assertThat(ResultSetUtil.getValue(BigInteger.class, mock, 99, BigInteger.TEN), is(BigInteger.TEN));
		
		// モックのメソッドは一回も呼ばれない
		verifyZeroInteractions(mock);
	}
	
	/**
	 * {@link ResultSet}のメソッドは一度も呼ばれず、デフォルト値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getUnknown_byName_usingDefault() throws Exception {
		ResultSet mock = mock(ResultSet.class);
		
		assertThat(ResultSetUtil.getValue(BigInteger.class, mock, COL, BigInteger.TEN), is(BigInteger.TEN));
		
		// モックのメソッドは一回も呼ばれない
		verifyZeroInteractions(mock);
	}
}
