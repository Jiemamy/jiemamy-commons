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
import static org.hamcrest.Matchers.hasItemInArray;
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
	 * {@link JmStringUtil#appendSqlPrefix(String, String)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_appendSqlPrefix() throws Exception {
		assertThat(JmStringUtil.appendSqlPrefix(null, "HOGE"), nullValue());
		assertThat(JmStringUtil.appendSqlPrefix("", "HOGE"), is(""));
		assertThat(JmStringUtil.appendSqlPrefix("FOO", null), is("FOO"));
		assertThat(JmStringUtil.appendSqlPrefix("FOO", ""), is("FOO"));
		assertThat(JmStringUtil.appendSqlPrefix("FOO", "HOGE"), is("HOGE_FOO"));
		assertThat(JmStringUtil.appendSqlPrefix("FOO", "HOGE_"), is("HOGE_FOO"));
	}
	
	/**
	 * {@link JmStringUtil#camelize(String)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_camelize() throws Exception {
		assertThat(JmStringUtil.camelize("AAA"), is("Aaa"));
		assertThat(JmStringUtil.camelize("AAA_BBB_CCC"), is("AaaBbbCcc"));
		assertThat(JmStringUtil.camelize(null), is(nullValue()));
	}
	
	/**
	 * {@link JmStringUtil#capitalize(String)}のテスト。
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
	 * {@link JmStringUtil#decamelize(String)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_decamelize() throws Exception {
		assertThat(JmStringUtil.decamelize("getFooBar"), is("GET_FOO_BAR"));
		assertThat(JmStringUtil.decamelize("FooBar"), is("FOO_BAR"));
		assertThat(JmStringUtil.decamelize("GET_FOO_BAR"), is("G_E_T__F_O_O__B_A_R"));
		assertThat(JmStringUtil.decamelize("x"), is("X"));
		assertThat(JmStringUtil.decamelize(""), is(""));
		assertThat(JmStringUtil.decamelize(null), is(nullValue()));
	}
	
	/**
	 * {@link JmStringUtil#capitalize(String)}のテスト。
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
	 * {@link JmStringUtil#endsWithIgnoreCase(String, String)}のテスト。
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
		
		assertThat(JmStringUtil.endsWithIgnoreCase("a", "abc"), is(false));
	}
	
	/**
	 * {@link JmStringUtil#equals(String, String)}のテスト。
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
	 * {@link JmStringUtil#equalsIgnoreCase(String, String)}のテスト。
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
		
		assertThat(JmStringUtil.indexOfIgnoreCase(new String[] {
			"foo",
			"bar",
			null
		}, null), is(2));
		
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
	 * {@link JmStringUtil#isBlank(String)}のテスト。
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
	 * {@link JmStringUtil#isEmpty(String)}のテスト。
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
	 * {@link JmStringUtil#isJavaClassName(String)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_isJavaClassName() throws Exception {
		assertThat(JmStringUtil.isJavaClassName("x"), is(false));
		assertThat(JmStringUtil.isJavaClassName("xY"), is(false));
		assertThat(JmStringUtil.isJavaClassName("xYZ"), is(false));
		assertThat(JmStringUtil.isJavaClassName("xYZz"), is(false));
		assertThat(JmStringUtil.isJavaClassName("X"), is(false)); // 全て大文字はfalse
		assertThat(JmStringUtil.isJavaClassName("foo"), is(false));
		assertThat(JmStringUtil.isJavaClassName("Foo"), is(true));
		assertThat(JmStringUtil.isJavaClassName("FOO"), is(false)); // 全て大文字はfalse
		assertThat(JmStringUtil.isJavaClassName("fooBar"), is(false));
		assertThat(JmStringUtil.isJavaClassName("fooBarB"), is(false));
		assertThat(JmStringUtil.isJavaClassName("foo_bar"), is(false));
		assertThat(JmStringUtil.isJavaClassName("FooBar"), is(true));
		assertThat(JmStringUtil.isJavaClassName("FOO_BAR"), is(false));
		assertThat(JmStringUtil.isJavaClassName("fooBarBaz"), is(false));
		assertThat(JmStringUtil.isJavaClassName("foo_Bar_Baz"), is(false));
		assertThat(JmStringUtil.isJavaClassName("FooBarBaz"), is(true));
		assertThat(JmStringUtil.isJavaClassName("FOO_BAR_BAZ"), is(false));
		
		assertThat(JmStringUtil.isJavaClassName("1"), is(false));
		assertThat(JmStringUtil.isJavaClassName("X1"), is(true));
		assertThat(JmStringUtil.isJavaClassName("X11"), is(true));
		assertThat(JmStringUtil.isJavaClassName("x1X1"), is(false));
	}
	
	/**
	 * {@link JmStringUtil#isJavaName(String)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_isJavaName() throws Exception {
		assertThat(JmStringUtil.isJavaName("x"), is(true));
		assertThat(JmStringUtil.isJavaName("xY"), is(true));
		assertThat(JmStringUtil.isJavaName("xYZ"), is(true));
		assertThat(JmStringUtil.isJavaName("xYZz"), is(true));
		assertThat(JmStringUtil.isJavaName("X"), is(false));
		assertThat(JmStringUtil.isJavaName("foo"), is(true));
		assertThat(JmStringUtil.isJavaName("Foo"), is(false));
		assertThat(JmStringUtil.isJavaName("FOO"), is(false));
		assertThat(JmStringUtil.isJavaName("fooBar"), is(true));
		assertThat(JmStringUtil.isJavaName("fooBarB"), is(true));
		assertThat(JmStringUtil.isJavaName("foo_bar"), is(false));
		assertThat(JmStringUtil.isJavaName("FooBar"), is(false));
		assertThat(JmStringUtil.isJavaName("FOO_BAR"), is(false));
		assertThat(JmStringUtil.isJavaName("fooBarBaz"), is(true));
		assertThat(JmStringUtil.isJavaName("foo_Bar_Baz"), is(false));
		assertThat(JmStringUtil.isJavaName("FooBarBaz"), is(false));
		assertThat(JmStringUtil.isJavaName("FOO_BAR_BAZ"), is(false));
		
		assertThat(JmStringUtil.isJavaName("1"), is(false));
		assertThat(JmStringUtil.isJavaName("X1"), is(false));
		assertThat(JmStringUtil.isJavaName("X11"), is(false));
		assertThat(JmStringUtil.isJavaName("x1X1"), is(true));
		assertThat(JmStringUtil.isJavaName("a1"), is(true));
	}
	
	/**
	 * {@link JmStringUtil#isNotBlank(String)}のテスト。
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
	 * {@link JmStringUtil#isNotEmpty(String)}のテスト。
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
	 * {@link JmStringUtil#isNumber(String)}のテスト。
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
	 * link {@link JmStringUtil#isSqlName(String)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_isSqlName() throws Exception {
		assertThat(JmStringUtil.isJavaName("x"), is(true));
		assertThat(JmStringUtil.isJavaName("xY"), is(true));
		assertThat(JmStringUtil.isJavaName("xYZ"), is(true));
		assertThat(JmStringUtil.isJavaName("xYZz"), is(true));
		assertThat(JmStringUtil.isJavaName("X"), is(false));
		assertThat(JmStringUtil.isJavaName("foo"), is(true));
		assertThat(JmStringUtil.isJavaName("Foo"), is(false));
		assertThat(JmStringUtil.isJavaName("FOO"), is(false));
		assertThat(JmStringUtil.isJavaName("fooBar"), is(true));
		assertThat(JmStringUtil.isJavaName("fooBarB"), is(true));
		assertThat(JmStringUtil.isJavaName("foo_bar"), is(false));
		assertThat(JmStringUtil.isJavaName("FooBar"), is(false));
		assertThat(JmStringUtil.isJavaName("FOO_BAR"), is(false));
		assertThat(JmStringUtil.isJavaName("fooBarBaz"), is(true));
		assertThat(JmStringUtil.isJavaName("foo_Bar_Baz"), is(false));
		assertThat(JmStringUtil.isJavaName("FooBarBaz"), is(false));
		assertThat(JmStringUtil.isJavaName("FOO_BAR_BAZ"), is(false));
		
		assertThat(JmStringUtil.isJavaName("1"), is(false));
		assertThat(JmStringUtil.isJavaName("X1"), is(false));
		assertThat(JmStringUtil.isJavaName("X11"), is(false));
		assertThat(JmStringUtil.isJavaName("x1X1"), is(true));
	}
	
	/**
	 * {@link JmStringUtil#ltrim}のテスト。
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
		
		assertThat(JmStringUtil.ltrim("t_xxx", "t"), is("_xxx"));
	}
	
	/**
	 * {@link JmStringUtil#removeSqlPrefix(String, String)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_removeSqlPrefix() throws Exception {
		assertThat(JmStringUtil.removeSqlPrefix(null, "HOGE"), nullValue());
		assertThat(JmStringUtil.removeSqlPrefix("", "HOGE"), is(""));
		assertThat(JmStringUtil.removeSqlPrefix("FOO", null), is("FOO"));
		assertThat(JmStringUtil.removeSqlPrefix("FOO", ""), is("FOO"));
		assertThat(JmStringUtil.removeSqlPrefix("HOGE_FOO", "HOGE"), is("FOO"));
		assertThat(JmStringUtil.removeSqlPrefix("HOGE_FOO", "HOGE_"), is("FOO"));
	}
	
	/**
	 * {@link JmStringUtil#replace(String, String, String)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_replace() throws Exception {
		assertThat(JmStringUtil.replace("aaabbbccc", "aaa", "xxx"), is("xxxbbbccc"));
		assertThat(JmStringUtil.replace("aaabbbcccaaabbbccc", "aaa", "xxx"), is("xxxbbbcccxxxbbbccc"));
		
		assertThat(JmStringUtil.replace("abcabc", "abc", "xabc"), is("xabcxabc"));
		
		assertThat(JmStringUtil.replace("foobarbaz", null, "x"), is("foobarbaz"));
		assertThat(JmStringUtil.replace("foobarbaz", "f", null), is("foobarbaz"));
		assertThat(JmStringUtil.replace("foobarbaz", null, null), is("foobarbaz"));
		assertThat(JmStringUtil.replace(null, "foo", "bar"), is(nullValue()));
		assertThat(JmStringUtil.replace(null, null, "bar"), is(nullValue()));
		assertThat(JmStringUtil.replace(null, "foo", null), is(nullValue()));
		assertThat(JmStringUtil.replace(null, null, null), is(nullValue()));
	}
	
	/**
	 * {@link JmStringUtil#rtrim}のテスト。
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
	 * {@link JmStringUtil#split(String, String)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_split() throws Exception {
		assertThat(JmStringUtil.split("foobarbaz", "bar").length, is(2));
		assertThat(JmStringUtil.split("foobarbaz", "bar"), hasItemInArray("foo"));
		assertThat(JmStringUtil.split("foobarbaz", "bar"), hasItemInArray("baz"));
		assertThat(JmStringUtil.split("", "foo").length, is(0));
		assertThat(JmStringUtil.split(null, "foo").length, is(0));
		assertThat(JmStringUtil.split(null, null).length, is(0));
		assertThat(JmStringUtil.split("foo", null).length, is(1));
		assertThat(JmStringUtil.split("foo", null), hasItemInArray("foo"));
	}
	
	/**
	 * {@link JmStringUtil#startsWithIgnoreCase(String, String)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_startsWithIgnoreCase() throws Exception {
		assertThat(JmStringUtil.startsWithIgnoreCase("fooBarBaz", "foo"), is(true));
		assertThat(JmStringUtil.startsWithIgnoreCase("fooBarBaz", "Foo"), is(true));
		assertThat(JmStringUtil.startsWithIgnoreCase("fooBarBaz", "baz"), is(false));
		assertThat(JmStringUtil.startsWithIgnoreCase("fooBarBaz", null), is(false));
		assertThat(JmStringUtil.startsWithIgnoreCase(null, "foo"), is(false));
		assertThat(JmStringUtil.startsWithIgnoreCase(null, null), is(false));
		
		assertThat(JmStringUtil.startsWithIgnoreCase("a", "abc"), is(false));
	}
	
	/**
	 * {@link JmStringUtil#substringFromLast(String, String)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_substringFromLast() throws Exception {
		assertThat(JmStringUtil.substringFromLast("foo;bar;baz", ";"), is("foo;bar"));
		assertThat(JmStringUtil.substringFromLast("foo;bar;baz", "-"), is("foo;bar;baz"));
		assertThat(JmStringUtil.substringFromLast("foo;bar;baz", null), is("foo;bar;baz"));
		assertThat(JmStringUtil.substringFromLast(null, ";"), is(nullValue()));
		assertThat(JmStringUtil.substringFromLast(null, null), is(nullValue()));
	}
	
	/**
	 * {@link JmStringUtil#substringToLast(String, String)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_substringToLast() throws Exception {
		assertThat(JmStringUtil.substringToLast("foo;bar;baz", ";"), is("baz"));
		assertThat(JmStringUtil.substringToLast("foo;bar;baz", "-"), is("foo;bar;baz"));
		assertThat(JmStringUtil.substringToLast("foo;bar;baz", null), is("foo;bar;baz"));
		assertThat(JmStringUtil.substringToLast(null, ";"), is(nullValue()));
		assertThat(JmStringUtil.substringToLast(null, null), is(nullValue()));
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
	 * {@link JmStringUtil#toHex(byte[])}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_toHexBytes() throws Exception {
		assertThat(JmStringUtil.toHex(new byte[] {
			(byte) 0x00
		}), is("00"));
		assertThat(JmStringUtil.toHex(new byte[] {
			(byte) 0x01
		}), is("01"));
		assertThat(JmStringUtil.toHex(new byte[] {
			(byte) 0xaa
		}), is("aa"));
		assertThat(JmStringUtil.toHex(new byte[] {
			(byte) 0xaa,
			(byte) 0xbb
		}), is("aabb"));
		assertThat(JmStringUtil.toHex(new byte[0]), is(""));
		assertThat(JmStringUtil.toHex(null), is(""));
	}
	
	/**
	 * {@link JmStringUtil#toHex(int)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_toHexInt() throws Exception {
		assertThat(JmStringUtil.toHex(0x00), is("00000000"));
		assertThat(JmStringUtil.toHex(0x01), is("00000001"));
		assertThat(JmStringUtil.toHex(0xaa), is("000000aa"));
	}
	
	/**
	 * Test method for {@link JmStringUtil#toJavaClassName(String)}.
	 */
	@Test
	public void test_toJavaClassNameString() {
		assertThat(JmStringUtil.toJavaClassName(null), nullValue());
		assertThat(JmStringUtil.toJavaClassName(""), is(""));
		assertThat(JmStringUtil.toJavaClassName("x"), is("X"));
		assertThat(JmStringUtil.toJavaClassName("xY"), is("XY"));
		assertThat(JmStringUtil.toJavaClassName("xYZ"), is("XYZ"));
		assertThat(JmStringUtil.toJavaClassName("xYZz"), is("XYZz"));
		assertThat(JmStringUtil.toJavaClassName("X"), is("X"));
		assertThat(JmStringUtil.toJavaClassName("foo"), is("Foo"));
		assertThat(JmStringUtil.toJavaClassName("Foo"), is("Foo"));
		assertThat(JmStringUtil.toJavaClassName("FOO"), is("Foo")); // 全て大文字はFooとなる
		assertThat(JmStringUtil.toJavaClassName("fooBar"), is("FooBar"));
		assertThat(JmStringUtil.toJavaClassName("fooBarB"), is("FooBarB"));
		assertThat(JmStringUtil.toJavaClassName("foo_bar"), is("FOO_BAR")); // 想定外のパラメータは全てUpperCase
		assertThat(JmStringUtil.toJavaClassName("FooBar"), is("FooBar"));
		assertThat(JmStringUtil.toJavaClassName("FOO_BAR"), is("FooBar"));
		assertThat(JmStringUtil.toJavaClassName("fooBarBaz"), is("FooBarBaz"));
		assertThat(JmStringUtil.toJavaClassName("foo_Bar_Baz"), is("FOO_BAR_BAZ")); // 想定外のパラメータは全てUpperCase
		assertThat(JmStringUtil.toJavaClassName("FooBarBaz"), is("FooBarBaz"));
		assertThat(JmStringUtil.toJavaClassName("FOO_BAR_BAZ"), is("FooBarBaz"));
		
		assertThat(JmStringUtil.toJavaClassName("1"), is("1"));
		assertThat(JmStringUtil.toJavaClassName("a1"), is("A1"));
		assertThat(JmStringUtil.toJavaClassName("a11"), is("A11"));
		// assertThat(JmStringUtil.toJavaClassName("a11abC"), is("A11AbC"));
	}
	
	/**
	 * Test method for {@link JmStringUtil#toJavaName(String)}.
	 */
	@Test
	public void test_toJavaNameString() {
		assertThat(JmStringUtil.toJavaName(null), nullValue());
		assertThat(JmStringUtil.toJavaName(""), is(""));
		assertThat(JmStringUtil.toJavaName("x"), is("x"));
		assertThat(JmStringUtil.toJavaName("xY"), is("xY"));
		assertThat(JmStringUtil.toJavaName("xYZ"), is("xYZ"));
		assertThat(JmStringUtil.toJavaName("xYZz"), is("xYZz"));
		assertThat(JmStringUtil.toJavaName("X"), is("x"));
		assertThat(JmStringUtil.toJavaName("foo"), is("foo"));
		assertThat(JmStringUtil.toJavaName("Foo"), is("foo"));
		assertThat(JmStringUtil.toJavaName("FOO"), is("foo"));
		assertThat(JmStringUtil.toJavaName("fooBar"), is("fooBar"));
		assertThat(JmStringUtil.toJavaName("fooBarB"), is("fooBarB"));
		assertThat(JmStringUtil.toJavaName("foo_bar"), is("FOO_BAR")); // 想定外のパラメータは全てUpperCase
		assertThat(JmStringUtil.toJavaName("FooBar"), is("fooBar"));
		assertThat(JmStringUtil.toJavaName("FOO_BAR"), is("fooBar"));
		assertThat(JmStringUtil.toJavaName("fooBarBaz"), is("fooBarBaz"));
		assertThat(JmStringUtil.toJavaName("foo_Bar_Baz"), is("FOO_BAR_BAZ")); // 想定外のパラメータは全てUpperCase
		assertThat(JmStringUtil.toJavaName("FooBarBaz"), is("fooBarBaz"));
		assertThat(JmStringUtil.toJavaName("FOO_BAR_BAZ"), is("fooBarBaz"));
		
		assertThat(JmStringUtil.toJavaName("1"), is("1"));
		assertThat(JmStringUtil.toJavaName("a1"), is("a1"));
		assertThat(JmStringUtil.toJavaName("a11"), is("a11"));
	}
	
	/**
	 * Test method for {@link JmStringUtil#toSqlName(String)}.
	 */
	@Test
	public void test_toSqlNameString() {
		assertThat(JmStringUtil.toSqlName(null), nullValue());
		assertThat(JmStringUtil.toSqlName(""), is(""));
		assertThat(JmStringUtil.toSqlName("x"), is("X"));
		assertThat(JmStringUtil.toSqlName("xY"), is("X_Y"));
		assertThat(JmStringUtil.toSqlName("xYZ"), is("X_Y_Z"));
		assertThat(JmStringUtil.toSqlName("xYZz"), is("X_Y_ZZ"));
		assertThat(JmStringUtil.toSqlName("X"), is("X"));
		assertThat(JmStringUtil.toSqlName("foo"), is("FOO"));
		assertThat(JmStringUtil.toSqlName("Foo"), is("FOO"));
		assertThat(JmStringUtil.toSqlName("FOO"), is("FOO"));
		assertThat(JmStringUtil.toSqlName("fooBar"), is("FOO_BAR"));
		assertThat(JmStringUtil.toSqlName("fooBarB"), is("FOO_BAR_B"));
		assertThat(JmStringUtil.toSqlName("foo_bar"), is("FOO_BAR"));
		assertThat(JmStringUtil.toSqlName("FooBar"), is("FOO_BAR"));
		assertThat(JmStringUtil.toSqlName("FOO_BAR"), is("FOO_BAR"));
		assertThat(JmStringUtil.toSqlName("fooBarBaz"), is("FOO_BAR_BAZ"));
		assertThat(JmStringUtil.toSqlName("foo_Bar_Baz"), is("FOO_BAR_BAZ"));
		assertThat(JmStringUtil.toSqlName("FooBarBaz"), is("FOO_BAR_BAZ"));
		assertThat(JmStringUtil.toSqlName("FOO_BAR_BAZ"), is("FOO_BAR_BAZ"));
		
		assertThat(JmStringUtil.toSqlName("1"), is("1"));
		assertThat(JmStringUtil.toSqlName("a1"), is("A1"));
		assertThat(JmStringUtil.toSqlName("a11"), is("A11"));
	}
	
	/**
	 * Test method for {@link JmStringUtil#toUnCapital(String)}.
	 */
	@Test
	public void test_toUnCapital() {
		assertThat(JmStringUtil.toUnCapital("Foo"), is("foo"));
		assertThat(JmStringUtil.toUnCapital("FooBar"), is("fooBar"));
		assertThat(JmStringUtil.toUnCapital("QName"), is("qName"));
		
		assertThat(JmStringUtil.toUnCapital("foo"), is("foo"));
	}
	
	/**
	 * {@link JmStringUtil#trimPrefix(String, String)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_trimPrefix() throws Exception {
		assertThat(JmStringUtil.trimPrefix("foobar", "foo"), is("bar"));
		assertThat(JmStringUtil.trimPrefix("foobar", "bar"), is("foobar"));
		assertThat(JmStringUtil.trimPrefix("foobar", null), is("foobar"));
		assertThat(JmStringUtil.trimPrefix(null, "foo"), is(nullValue()));
		assertThat(JmStringUtil.trimPrefix("foobar", "baz"), is("foobar"));
	}
	
	/**
	 * {@link JmStringUtil#trimSuffix(String, String)}のテスト。
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
