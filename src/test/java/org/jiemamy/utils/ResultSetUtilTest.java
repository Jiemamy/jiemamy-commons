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

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link ResultSetUtil}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class ResultSetUtilTest {
	
	private Mockery context = new JUnit4Mockery();
	
	private ResultSet rs;
	
	private static final String COL = "COL";
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		rs = context.mock(ResultSet.class);
	}
	
	/**
	 * {@link ResultSet#getBoolean(String)}が正常に呼ばれ、その戻り値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getBoolean() throws Exception {
		// FORMAT-OFF
	    context.checking(new Expectations() {{
	    	// このモックはgetBooleanがCOLを引数にとって1回だけ呼ばれることを期待する
	    	one(rs).getBoolean(with(equal(COL)));
	    	will(returnValue(true)); // 呼ばれたらtrueをreturnする
	    }});
		// FORMAT-ON
		
		// さて、やってみよう
		assertThat(ResultSetUtil.getValue(Boolean.class, rs, COL, false), is(true));
	}
	
	/**
	 * {@link ResultSet#getBoolean(String)}が正常に呼ばれ、デフォルト値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getBoolean2() throws Exception {
		// FORMAT-OFF
	    context.checking(new Expectations() {{
	    	// このモックはgetBooleanがCOLを引数にとって1回だけ呼ばれることを期待する
	    	one(rs).getBoolean(with(equal(COL)));
	    	will(throwException(new SQLException())); // 呼ばれたらSQLExceptionをthrowする
	    }});
		// FORMAT-ON
		
		// さて、やってみよう
		assertThat(ResultSetUtil.getValue(Boolean.class, rs, COL, false), is(false));
	}
	
	/**
	 * {@link ResultSet#getByte(String)}が正常に呼ばれ、その戻り値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getByte() throws Exception {
		// FORMAT-OFF
	    context.checking(new Expectations() {{
	    	// このモックはgetByteがCOLを引数にとって1回だけ呼ばれることを期待する
	        one(rs).getByte(with(equal(COL)));
	    	will(returnValue(Byte.MAX_VALUE)); // 呼ばれたらByte.MAX_VALUEをreturnする
	    }});
		// FORMAT-ON
		
		// さて、やってみよう
		assertThat(ResultSetUtil.getValue(Byte.class, rs, COL, Byte.MIN_VALUE), is(Byte.MAX_VALUE));
	}
	
	/**
	 * {@link ResultSet#getByte(String)}が正常に呼ばれ、デフォルト値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getByte2() throws Exception {
		// FORMAT-OFF
	    context.checking(new Expectations() {{
	    	// このモックはgetByteがCOLを引数にとって1回だけ呼ばれることを期待する
	        one(rs).getByte(with(equal(COL)));
	    	will(throwException(new SQLException())); // 呼ばれたらSQLExceptionをthrowする
	    }});
		// FORMAT-ON
		
		// さて、やってみよう
		assertThat(ResultSetUtil.getValue(Byte.class, rs, COL, Byte.MIN_VALUE), is(Byte.MIN_VALUE));
	}
	
	/**
	 * {@link ResultSet#getString(String)}が正常に呼ばれ、その戻り値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getString() throws Exception {
		// FORMAT-OFF
	    context.checking(new Expectations() {{
	    	// このモックはgetByteがCOLを引数にとって1回だけ呼ばれることを期待する
	        one(rs).getString(with(equal(COL)));
	    	will(returnValue("bar"));
	    }});
		// FORMAT-ON
		
		// さて、やってみよう
		assertThat(ResultSetUtil.getValue(String.class, rs, COL, "foo"), is("bar"));
	}
	
	/**
	 * {@link ResultSet}のメソッドは一度も呼ばれず、デフォルト値が結果となること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_unknown() throws Exception {
		// FORMAT-OFF
	    context.checking(new Expectations() {{
	    	// このモックのメソッドは一回も呼ばれないことを期待する
	    	never(rs);
	    }});
		// FORMAT-ON
		
		// さて、やってみよう
		assertThat(ResultSetUtil.getValue(BigInteger.class, rs, COL, BigInteger.TEN), is(BigInteger.TEN));
	}
}
