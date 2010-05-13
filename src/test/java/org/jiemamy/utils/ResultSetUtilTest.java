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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

/**
 * {@link ResultSetUtil}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class ResultSetUtilTest {
	
	private static final String COL = "COL";
	

	/**
	 * {@link ResultSet#getBoolean(String)}が正常に呼ばれ、その戻り値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getBoolean() throws Exception {
		ResultSet mock = mock(ResultSet.class);
		when(mock.getBoolean(COL)).thenReturn(true);
		
		assertThat(ResultSetUtil.getValue(boolean.class, mock, COL, false), is(true));
		
		// getBooleanが引数COLで、1回だけ呼ばれる
		verify(mock, only()).getBoolean(COL);
	}
	
	/**
	 * {@link ResultSet#getBoolean(String)}が正常に呼ばれ、デフォルト値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getBoolean2() throws Exception {
		ResultSet mock = mock(ResultSet.class);
		when(mock.getBoolean(COL)).thenThrow(new SQLException());
		
		assertThat(ResultSetUtil.getValue(boolean.class, mock, COL, false), is(false));
		
		// getBooleanが引数COLで、1回だけ呼ばれる
		verify(mock, only()).getBoolean(COL);
	}
	
	/**
	 * {@link ResultSet#getByte(String)}が正常に呼ばれ、その戻り値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getByte() throws Exception {
		ResultSet mock = mock(ResultSet.class);
		when(mock.getByte(COL)).thenReturn(Byte.MAX_VALUE);
		
		assertThat(ResultSetUtil.getValue(byte.class, mock, COL, Byte.MIN_VALUE), is(Byte.MAX_VALUE));
		
		// getByteが引数COLで、1回だけ呼ばれる
		verify(mock, only()).getByte(COL);
	}
	
	/**
	 * {@link ResultSet#getByte(String)}が正常に呼ばれ、デフォルト値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getByte2() throws Exception {
		ResultSet mock = mock(ResultSet.class);
		when(mock.getByte(COL)).thenThrow(new SQLException());
		
		assertThat(ResultSetUtil.getValue(byte.class, mock, COL, Byte.MIN_VALUE), is(Byte.MIN_VALUE));
		
		// getByteが引数COLで、1回だけ呼ばれる
		verify(mock, only()).getByte(COL);
	}
	
	/**
	 * {@link ResultSet#getString(String)}が正常に呼ばれ、その戻り値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getBytes() throws Exception {
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
	public void test_getBytes2() throws Exception {
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
	public void test_getString() throws Exception {
		ResultSet mock = mock(ResultSet.class);
		when(mock.getString(COL)).thenReturn("bar");
		
		assertThat(ResultSetUtil.getValue(String.class, mock, COL, "foo"), is("bar"));
		
		// getByteが引数COLで、1回だけ呼ばれる
		verify(mock, only()).getString(COL);
	}
	
	/**
	 * {@link ResultSet#getString(String)}が正常に呼ばれ、デフォルト値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getString2() throws Exception {
		ResultSet mock = mock(ResultSet.class);
		when(mock.getString(COL)).thenThrow(new SQLException());
		
		assertThat(ResultSetUtil.getValue(String.class, mock, COL, "foo"), is("foo"));
		
		// getByteが引数COLで、1回だけ呼ばれる
		verify(mock, only()).getString(COL);
	}
	
	/**
	 * {@link ResultSet}のメソッドは一度も呼ばれず、デフォルト値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_unknown() throws Exception {
		ResultSet mock = mock(ResultSet.class);
		
		assertThat(ResultSetUtil.getValue(BigInteger.class, mock, COL, BigInteger.TEN), is(BigInteger.TEN));
		
		// モックのメソッドは一回も呼ばれない
		verifyZeroInteractions(mock);
	}
}
