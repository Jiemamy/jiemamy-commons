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
	 * {@link JmStringUtil#appendHex(StringBuilder, byte)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_appendHex_byte() throws Exception {
		StringBuilder sb = new StringBuilder();
		JmStringUtil.appendHex(sb, (byte) 0xde);
		assertThat(sb.toString(), is("de"));
		JmStringUtil.appendHex(sb, (byte) 0xad);
		assertThat(sb.toString(), is("dead"));
		JmStringUtil.appendHex(sb, (byte) 0xbe);
		assertThat(sb.toString(), is("deadbe"));
		JmStringUtil.appendHex(sb, (byte) 0xef);
		assertThat(sb.toString(), is("deadbeef"));
	}
	
	/**
	 * {@link JmStringUtil#appendHex(StringBuilder, int)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_appendHex_int() throws Exception {
		StringBuilder sb = new StringBuilder();
		JmStringUtil.appendHex(sb, 0x4c);
		assertThat(sb.toString(), is("0000004c"));
		JmStringUtil.appendHex(sb, 0xffffffff);
		assertThat(sb.toString(), is("0000004cffffffff"));
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
	 * {@link JmStringUtil#capitalize(String)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_capitalize() throws Exception {
		assertThat(JmStringUtil.capitalize("foo"), is("Foo"));
		assertThat(JmStringUtil.capitalize("Bar"), is("Bar"));
		assertThat(JmStringUtil.capitalize("bazQux"), is("BazQux"));
		assertThat(JmStringUtil.capitalize(""), is(""));
		assertThat(JmStringUtil.capitalize(null), is(nullValue()));
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
		
		assertThat(JmStringUtil.containsIgnoreCase(null, "foo"), is(false));
		assertThat(JmStringUtil.containsIgnoreCase(array, null), is(false));
		assertThat(JmStringUtil.containsIgnoreCase(null, null), is(false));
	}
	
	/**
	 * {@link JmStringUtil#decamelize(String)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_decamelize() throws Exception {
		assertThat(JmStringUtil.decamelize("getFooBar"), is("GET_FOO_BAR"));
		assertThat(JmStringUtil.decamelize("GET_FOO_BAR"), is("G_E_T__F_O_O__B_A_R"));
		assertThat(JmStringUtil.decamelize(""), is(""));
		assertThat(JmStringUtil.decamelize(null), is(nullValue()));
		assertThat(JmStringUtil.decamelize("x"), is("X"));
	}
	
	/**
	 * {@link JmStringUtil#capitalize(String)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_decapitalize() throws Exception {
		assertThat(JmStringUtil.decapitalize("Foo"), is("foo"));
		assertThat(JmStringUtil.decapitalize("bar"), is("bar"));
		assertThat(JmStringUtil.decapitalize("BazQux"), is("bazQux"));
		assertThat(JmStringUtil.decapitalize("ABC"), is("ABC"));
		assertThat(JmStringUtil.decapitalize(""), is(""));
		assertThat(JmStringUtil.decapitalize(null), is(nullValue()));
	}
	
	/**
	 * {@link JmStringUtil#endsWithIgnoreCase(String, String)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_endsWithIgnoreCase() throws Exception {
		assertThat(JmStringUtil.endsWithIgnoreCase("fooBarBaz", "Baz"), is(true));
		assertThat(JmStringUtil.endsWithIgnoreCase("fooBarBaz", "baz"), is(true));
		assertThat(JmStringUtil.endsWithIgnoreCase("fooBarBaz", "foo"), is(false));
		assertThat(JmStringUtil.endsWithIgnoreCase("fooBarBaz", null), is(false));
		assertThat(JmStringUtil.endsWithIgnoreCase(null, "Baz"), is(false));
		assertThat(JmStringUtil.endsWithIgnoreCase(null, null), is(false));
	}
	
	/**
	 * {@link JmStringUtil#equals(String, String)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_equals() throws Exception {
		assertThat(JmStringUtil.equals("foo", "foo"), is(true));
		assertThat(JmStringUtil.equals("foo", "Foo"), is(false));
		assertThat(JmStringUtil.equals("foo", "bar"), is(false));
		assertThat(JmStringUtil.equals("foobar", "foo"), is(false));
		assertThat(JmStringUtil.equals("foobar", "bar"), is(false));
		assertThat(JmStringUtil.equals("", ""), is(true));
		assertThat(JmStringUtil.equals("", null), is(false));
		assertThat(JmStringUtil.equals(null, ""), is(false));
		assertThat(JmStringUtil.equals(null, null), is(true));
	}
	
	/**
	 * {@link JmStringUtil#equalsIgnoreCase(String, String)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_equalsIgnoreCase() throws Exception {
		assertThat(JmStringUtil.equalsIgnoreCase("foo", "foo"), is(true));
		assertThat(JmStringUtil.equalsIgnoreCase("foo", "Foo"), is(true));
		assertThat(JmStringUtil.equalsIgnoreCase("foo", "bar"), is(false));
		assertThat(JmStringUtil.equalsIgnoreCase("foobar", "foo"), is(false));
		assertThat(JmStringUtil.equalsIgnoreCase("foobar", "bar"), is(false));
		assertThat(JmStringUtil.equalsIgnoreCase("", ""), is(true));
		assertThat(JmStringUtil.equalsIgnoreCase("", null), is(false));
		assertThat(JmStringUtil.equalsIgnoreCase(null, ""), is(false));
		assertThat(JmStringUtil.equalsIgnoreCase(null, null), is(true));
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
		
		assertThat(JmStringUtil.indexOfIgnoreCase(null, "foo"), is(-1));
		assertThat(JmStringUtil.indexOfIgnoreCase(array, null), is(-1));
		assertThat(JmStringUtil.indexOfIgnoreCase(null, null), is(-1));
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
		
		assertThat(JmStringUtil.indexOfIgnoreCase(null, "foo", 1), is(-1));
		assertThat(JmStringUtil.indexOfIgnoreCase(array, null, 1), is(-1));
		assertThat(JmStringUtil.indexOfIgnoreCase(null, null, 1), is(-1));
		
		assertThat(JmStringUtil.indexOfIgnoreCase(array, "foo", -1), is(0));
		assertThat(JmStringUtil.indexOfIgnoreCase(array, "bar", -1), is(1));
		assertThat(JmStringUtil.indexOfIgnoreCase(array, "baz", -1), is(2));
	}
	
	/**
	 * {@link JmStringUtil#isBlank(String)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_isBlank() throws Exception {
		assertThat(JmStringUtil.isBlank(""), is(true));
		assertThat(JmStringUtil.isBlank("  "), is(true));
		assertThat(JmStringUtil.isBlank("  ¥t "), is(false));
		assertThat(JmStringUtil.isBlank("  ¥r "), is(false));
		assertThat(JmStringUtil.isBlank("  ¥n "), is(false));
		assertThat(JmStringUtil.isBlank("daisuke"), is(false));
		assertThat(JmStringUtil.isBlank("  m "), is(false));
		assertThat(JmStringUtil.isBlank(null), is(true));
	}
	
	/**
	 * {@link JmStringUtil#isEmpty(String)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_isEmpty() throws Exception {
		assertThat(JmStringUtil.isEmpty(""), is(true));
		assertThat(JmStringUtil.isEmpty(" "), is(false));
		assertThat(JmStringUtil.isEmpty("a"), is(false));
		assertThat(JmStringUtil.isEmpty("aa"), is(false));
		assertThat(JmStringUtil.isEmpty(null), is(true));
	}
	
	/**
	 * {@link JmStringUtil#isNotBlank(String)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_isNotBlank() throws Exception {
		assertThat(JmStringUtil.isNotBlank(""), is(false));
		assertThat(JmStringUtil.isNotBlank("  "), is(false));
		assertThat(JmStringUtil.isNotBlank("  ¥t "), is(true));
		assertThat(JmStringUtil.isNotBlank("  ¥r "), is(true));
		assertThat(JmStringUtil.isNotBlank("  ¥n "), is(true));
		assertThat(JmStringUtil.isNotBlank("daisuke"), is(true));
		assertThat(JmStringUtil.isNotBlank("  m "), is(true));
		assertThat(JmStringUtil.isNotBlank(null), is(false));
	}
	
	/**
	 * {@link JmStringUtil#isNotEmpty(String)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_isNotEmpty() throws Exception {
		assertThat(JmStringUtil.isNotEmpty(""), is(false));
		assertThat(JmStringUtil.isNotEmpty(" "), is(true));
		assertThat(JmStringUtil.isNotEmpty("a"), is(true));
		assertThat(JmStringUtil.isNotEmpty("aa"), is(true));
		assertThat(JmStringUtil.isNotEmpty(null), is(false));
	}
	
	/**
	 * {@link JmStringUtil#isNumber(String)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_isNumber() throws Exception {
		assertThat(JmStringUtil.isNumber("0"), is(true));
		assertThat(JmStringUtil.isNumber("10"), is(true));
		
		assertThat(JmStringUtil.isNumber("-1"), is(false));
		assertThat(JmStringUtil.isNumber("0x2f"), is(false));
		assertThat(JmStringUtil.isNumber("0a"), is(false));
		assertThat(JmStringUtil.isNumber("xx"), is(false));
		assertThat(JmStringUtil.isNumber("4%"), is(false));
		
		assertThat(JmStringUtil.isNumber(""), is(false));
		assertThat(JmStringUtil.isNumber(null), is(false));
	}
	
	/**
	 * {@link JmStringUtil#ltrim}
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
		
		assertThat(JmStringUtil.ltrim("t_xxx", "t_"), is("xxx"));
		assertThat(JmStringUtil.ltrim("t_xxxt_", "t_"), is("xxxt_"));
		assertThat(JmStringUtil.ltrim("xxxt_", "t_"), is("xxxt_"));
		assertThat(JmStringUtil.ltrim("xxx", "t_"), is("xxx"));
		assertThat(JmStringUtil.ltrim("xxx", null), is("xxx"));
		assertThat(JmStringUtil.ltrim("  xxx", null), is("xxx"));
		assertThat(JmStringUtil.ltrim(null, "t_"), is(nullValue()));
		assertThat(JmStringUtil.ltrim(null, null), is(nullValue()));
	}
	
	/**
	 * {@link JmStringUtil#rtrim}
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
		
		assertThat(JmStringUtil.rtrim("t_xxx", "t_"), is("t_xxx"));
		assertThat(JmStringUtil.rtrim("t_xxxt_", "t_"), is("t_xxx"));
		assertThat(JmStringUtil.rtrim("xxxt_", "t_"), is("xxx"));
		assertThat(JmStringUtil.rtrim("xxx", "t_"), is("xxx"));
		assertThat(JmStringUtil.rtrim("xxx", null), is("xxx"));
		assertThat(JmStringUtil.rtrim("xxx  ", null), is("xxx"));
		assertThat(JmStringUtil.rtrim(null, "t_"), is(nullValue()));
		assertThat(JmStringUtil.rtrim(null, null), is(nullValue()));
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
		// TODO コメントアウトのテストが通るように…。
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
