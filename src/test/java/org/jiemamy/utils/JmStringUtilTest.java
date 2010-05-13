/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2009/01/29
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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link JmStringUtil}のテストクラス。
 * 
 * @author daisuke
 */
public class JmStringUtilTest {
	
	private String[] array;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		array = new String[] {
			"foo",
			"bar",
			"baz"
		};
	}
	
	/**
	 * テストの情報を破棄する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@After
	public void tearDown() throws Exception {
		array = null;
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_camelizee() throws Exception {
		assertThat(JmStringUtil.camelize("AAA"), is("Aaa"));
		assertThat(JmStringUtil.camelize("AAA_BBB_CCC"), is("AaaBbbCcc"));
		assertThat(JmStringUtil.camelize(null), is(nullValue()));
	}
	
	/**
	 * Test method for {@link JmStringUtil#containsIgnoreCase(String[], String)}.
	 */
	@Test
	public void test_containsIgnoreCase() {
		assertThat(JmStringUtil.containsIgnoreCase(array, "foo"), is(true));
		assertThat(JmStringUtil.containsIgnoreCase(array, "bar"), is(true));
		assertThat(JmStringUtil.containsIgnoreCase(array, "baz"), is(true));
		assertThat(JmStringUtil.containsIgnoreCase(array, "qux"), is(false));
		
		assertThat(JmStringUtil.containsIgnoreCase(array, "FOO"), is(true));
		assertThat(JmStringUtil.containsIgnoreCase(array, "Bar"), is(true));
		assertThat(JmStringUtil.containsIgnoreCase(array, "bAz"), is(true));
		assertThat(JmStringUtil.containsIgnoreCase(array, "quX"), is(false));
	}
	
	/**
	 * Test method for {@link JmStringUtil#indexOfIgnoreCase(String[], String)}.
	 */
	@Test
	public void test_indexOfIgnoreCaseStringArrayString() {
		assertThat(JmStringUtil.indexOfIgnoreCase(array, "foo"), is(0));
		assertThat(JmStringUtil.indexOfIgnoreCase(array, "bar"), is(1));
		assertThat(JmStringUtil.indexOfIgnoreCase(array, "baz"), is(2));
		assertThat(JmStringUtil.indexOfIgnoreCase(array, "qux"), is(-1));
		
		assertThat(JmStringUtil.indexOfIgnoreCase(array, "FOO"), is(0));
		assertThat(JmStringUtil.indexOfIgnoreCase(array, "Bar"), is(1));
		assertThat(JmStringUtil.indexOfIgnoreCase(array, "bAz"), is(2));
		assertThat(JmStringUtil.indexOfIgnoreCase(array, "quX"), is(-1));
	}
	
	/**
	 * Test method for {@link JmStringUtil#indexOfIgnoreCase(String[], String, int)}.
	 */
	@Test
	public void test_indexOfIgnoreCaseStringArrayStringInt() {
		assertThat(JmStringUtil.indexOfIgnoreCase(array, "foo", 1), is(-1));
		assertThat(JmStringUtil.indexOfIgnoreCase(array, "bar", 1), is(1));
		assertThat(JmStringUtil.indexOfIgnoreCase(array, "baz", 1), is(2));
		assertThat(JmStringUtil.indexOfIgnoreCase(array, "qux", 1), is(-1));
		
		assertThat(JmStringUtil.indexOfIgnoreCase(array, "FOO", 1), is(-1));
		assertThat(JmStringUtil.indexOfIgnoreCase(array, "Bar", 1), is(1));
		assertThat(JmStringUtil.indexOfIgnoreCase(array, "bAz", 1), is(2));
		assertThat(JmStringUtil.indexOfIgnoreCase(array, "quX", 1), is(-1));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_ltrim() throws Exception {
		assertThat(JmStringUtil.ltrim("   xxx"), is("xxx"));
		assertThat(JmStringUtil.ltrim("   xxx   "), is("xxx   "));
		assertThat(JmStringUtil.ltrim("xxx   "), is("xxx   "));
		assertThat(JmStringUtil.ltrim("xxx"), is("xxx"));
		assertThat(JmStringUtil.ltrim(null), is(nullValue()));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_rtrim() throws Exception {
		assertThat(JmStringUtil.rtrim("   xxx"), is("   xxx"));
		assertThat(JmStringUtil.rtrim("   xxx   "), is("   xxx"));
		assertThat(JmStringUtil.rtrim("xxx   "), is("xxx"));
		assertThat(JmStringUtil.rtrim("xxx"), is("xxx"));
		assertThat(JmStringUtil.rtrim(null), is(nullValue()));
	}
	
	/**
	 * Test method for {@link JmStringUtil#toCapital(String)}.
	 */
	@Test
	public void test_toCapital() {
		assertThat(JmStringUtil.toCapital("foo"), is("Foo"));
		assertThat(JmStringUtil.toCapital("fooBar"), is("FooBar"));
		assertThat(JmStringUtil.toCapital("qName"), is("QName"));
	}
	
	/**
	 * Test method for {@link JmStringUtil#toJavaClassName(String)}.
	 */
	@Test
	public void test_toJavaClassNameString() {
		assertThat(JmStringUtil.toJavaClassName("foo"), is("Foo"));
//		assertThat(JmStringUtil.toJavaClassName("fooBar"), is("FooBar"));
		assertThat(JmStringUtil.toJavaClassName("FOO_BAR"), is("FooBar"));
		assertThat(JmStringUtil.toJavaClassName("Q_NAME"), is("QName"));
		assertThat(JmStringUtil.toJavaClassName("SQL_STRING"), is("SqlString"));
	}
	
	/**
	 * Test method for {@link JmStringUtil#toJavaClassName(String, String)}.
	 */
	@Test
	public void test_toJavaClassNameStringString() {
		assertThat(JmStringUtil.toJavaClassName("HOGE_foo", "HOGE"), is("Foo"));
//		assertThat(JmStringUtil.toJavaClassName("HOGE_fooBar", "HOGE"), is("FooBar"));
		assertThat(JmStringUtil.toJavaClassName("HOGE_FOO_BAR", "HOGE"), is("FooBar"));
		assertThat(JmStringUtil.toJavaClassName("HOGE_Q_NAME", "HOGE"), is("QName"));
		assertThat(JmStringUtil.toJavaClassName("HOGE_SQL_STRING", "HOGE"), is("SqlString"));
	}
	
	/**
	 * Test method for {@link JmStringUtil#toJavaName(String)}.
	 */
	@Test
	public void test_toJavaNameString() {
		assertThat(JmStringUtil.toJavaName("foo"), is("foo"));
//		assertThat(JmStringUtil.toJavaName("fooBar"), is("fooBar"));
		assertThat(JmStringUtil.toJavaName("FOO_BAR"), is("fooBar"));
		assertThat(JmStringUtil.toJavaName("Q_NAME"), is("qName"));
		assertThat(JmStringUtil.toJavaName("SQL_STRING"), is("sqlString"));
		assertThat(JmStringUtil.toJavaName(null), is(nullValue()));
	}
	
	/**
	 * Test method for {@link JmStringUtil#toJavaName(String, String)}.
	 */
	@Test
	public void test_toJavaNameStringString() {
//		fail("Not yet implemented");
	}
	
	/**
	 * Test method for {@link JmStringUtil#toSQLName(String, String)}.
	 */
	@Test
	public void test_toSQLName() {
		assertThat(JmStringUtil.toSQLName("foo"), is("FOO"));
//		assertThat(JmStringUtil.toSQLName("Foo"), is("FOO"));
//		assertThat(JmStringUtil.toSQLName("FOO"), is("FOO"));
		assertThat(JmStringUtil.toSQLName("fooBar"), is("FOO_BAR"));
//		assertThat(JmStringUtil.toSQLName("FooBar"), is("FOO_BAR"));
//		assertThat(JmStringUtil.toSQLName("FOO_BAR"), is("FOO_BAR"));
		assertThat(JmStringUtil.toSQLName("qName"), is("Q_NAME"));
//		assertThat(JmStringUtil.toSQLName("QName"), is("Q_NAME"));
//		assertThat(JmStringUtil.toSQLName("Q_NAME"), is("Q_NAME"));
		assertThat(JmStringUtil.toSQLName("sqlString"), is("SQL_STRING"));
//		assertThat(JmStringUtil.toSQLName("SqlString"), is("SQL_STRING"));
//		assertThat(JmStringUtil.toSQLName("SQL_STRING"), is("SQL_STRING"));
	}
	
	/**
	 * Test method for {@link JmStringUtil#toUnCapital(String)}.
	 */
	@Test
	public void test_toUnCapital() {
		assertThat(JmStringUtil.toUnCapital("Foo"), is("foo"));
		assertThat(JmStringUtil.toUnCapital("FooBar"), is("fooBar"));
		assertThat(JmStringUtil.toUnCapital("QName"), is("qName"));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_trimPrefix() throws Exception {
		assertThat(JmStringUtil.trimPrefix("foobar", "foo"), is("bar"));
		assertThat(JmStringUtil.trimPrefix("foobar", null), is("foobar"));
		assertThat(JmStringUtil.trimPrefix(null, "foo"), is(nullValue()));
		assertThat(JmStringUtil.trimPrefix("foobar", "baz"), is("foobar"));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_trimSuffix() throws Exception {
		assertThat(JmStringUtil.trimSuffix("foobar", "bar"), is("foo"));
		assertThat(JmStringUtil.trimSuffix("foobar", null), is("foobar"));
		assertThat(JmStringUtil.trimSuffix(null, "foo"), is(nullValue()));
		assertThat(JmStringUtil.trimSuffix("foobar", "baz"), is("foobar"));
	}
}
