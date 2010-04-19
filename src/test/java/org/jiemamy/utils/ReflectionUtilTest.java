/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link ReflectionUtil}のテストクラス。
 * 
 * @author daisuke
 */
public class ReflectionUtilTest {
	
	private Method getFoo;
	
	private Method getBar;
	
	private Method isBaz;
	
	private Method getQux;
	
	private Method getQux2;
	
	private Method setFoo;
	
	private Method setBar;
	
	private Method setBaz;
	
	private Method setQux;
	
	private Method setQux2;
	
	private Field foo;
	
	private Field bar;
	
	private Field baz;
	
	private Field qux;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		foo = TestBean.class.getDeclaredField("foo");
		bar = TestBean.class.getDeclaredField("bar");
		baz = TestBean.class.getDeclaredField("baz");
		qux = TestBean.class.getDeclaredField("qux");
		
		getFoo = TestBean.class.getDeclaredMethod("getFoo");
		getBar = TestBean.class.getDeclaredMethod("getBar");
		isBaz = TestBean.class.getDeclaredMethod("isBaz");
		getQux = TestBean.class.getDeclaredMethod("getQux");
		getQux2 = TestBean.class.getDeclaredMethod("getQux2");
		
		setFoo = TestBean.class.getDeclaredMethod("setFoo", int.class);
		setBar = TestBean.class.getDeclaredMethod("setBar", String.class);
		setBaz = TestBean.class.getDeclaredMethod("setBaz", boolean.class);
		setQux = TestBean.class.getDeclaredMethod("setQux", Boolean.class);
		setQux2 = TestBean.class.getDeclaredMethod("setQux2");
	}
	
	/**
	 * テストの情報を破棄する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@After
	public void tearDown() throws Exception {
		getFoo = null;
		getBar = null;
		isBaz = null;
		getQux = null;
		getQux2 = null;
		
		setFoo = null;
		setBar = null;
		setBaz = null;
		setQux = null;
		setQux2 = null;
	}
	
	/**
	 * Test method for {@link ReflectionUtil#convertFieldToAccessorName(java.lang.reflect.Field, String)}.
	 */
	@Test
	public void testConvertFieldToAccessorName() {
		assertThat(ReflectionUtil.convertFieldToAccessorName(foo, "get"), is("getFoo"));
		assertThat(ReflectionUtil.convertFieldToAccessorName(bar, "get"), is("getBar"));
		assertThat(ReflectionUtil.convertFieldToAccessorName(baz, "is"), is("isBaz"));
		assertThat(ReflectionUtil.convertFieldToAccessorName(qux, "get"), is("getQux"));
		
		assertThat(ReflectionUtil.convertFieldToAccessorName(foo, "set"), is("setFoo"));
		assertThat(ReflectionUtil.convertFieldToAccessorName(bar, "set"), is("setBar"));
		assertThat(ReflectionUtil.convertFieldToAccessorName(baz, "set"), is("setBaz"));
		assertThat(ReflectionUtil.convertFieldToAccessorName(qux, "set"), is("setQux"));
	}
	
	/**
	 * Test method for {@link ReflectionUtil#convertFieldToGetterName(java.lang.reflect.Field)}.
	 */
	@Test
	public void testConvertFieldToGetterName() {
		assertThat(ReflectionUtil.convertFieldToGetterName(foo), is("getFoo"));
		assertThat(ReflectionUtil.convertFieldToGetterName(bar), is("getBar"));
		assertThat(ReflectionUtil.convertFieldToGetterName(baz), is("isBaz"));
		assertThat(ReflectionUtil.convertFieldToGetterName(qux), is("isQux"));
	}
	
	/**
	 * Test method for {@link ReflectionUtil#convertFieldToSetterName(java.lang.reflect.Field)}.
	 */
	@Test
	public void testConvertFieldToSetterName() {
		assertThat(ReflectionUtil.convertFieldToSetterName(foo), is("setFoo"));
		assertThat(ReflectionUtil.convertFieldToSetterName(bar), is("setBar"));
		assertThat(ReflectionUtil.convertFieldToSetterName(baz), is("setBaz"));
		assertThat(ReflectionUtil.convertFieldToSetterName(qux), is("setQux"));
	}
	
	/**
	 * Test method for {@link ReflectionUtil#convertAccessorToFieldName(java.lang.reflect.Method)}.
	 */
	@Test
	public void testConvertGetterToFieldName() {
		assertThat(ReflectionUtil.convertAccessorToFieldName(getFoo), is("foo"));
		assertThat(ReflectionUtil.convertAccessorToFieldName(getBar), is("bar"));
		assertThat(ReflectionUtil.convertAccessorToFieldName(isBaz), is("baz"));
		assertThat(ReflectionUtil.convertAccessorToFieldName(getQux), is("qux"));
		try {
			ReflectionUtil.convertAccessorToFieldName(getQux2);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
		
		assertThat(ReflectionUtil.convertAccessorToFieldName(setFoo), is("foo"));
		assertThat(ReflectionUtil.convertAccessorToFieldName(setBar), is("bar"));
		assertThat(ReflectionUtil.convertAccessorToFieldName(setBaz), is("baz"));
		assertThat(ReflectionUtil.convertAccessorToFieldName(setQux), is("qux"));
		try {
			ReflectionUtil.convertAccessorToFieldName(setQux2);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
		
	}
	
	/**
	 * Test method for {@link ReflectionUtil#isAccessor(java.lang.reflect.Method)}.
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testIsAccessor() throws Exception {
		assertThat(ReflectionUtil.isAccessor(getFoo), is(true));
		assertThat(ReflectionUtil.isAccessor(getBar), is(true));
		assertThat(ReflectionUtil.isAccessor(isBaz), is(true));
		assertThat(ReflectionUtil.isAccessor(getQux), is(true));
		assertThat(ReflectionUtil.isAccessor(getQux2), is(false));
		
		assertThat(ReflectionUtil.isAccessor(setFoo), is(true));
		assertThat(ReflectionUtil.isAccessor(setBar), is(true));
		assertThat(ReflectionUtil.isAccessor(setBaz), is(true));
		assertThat(ReflectionUtil.isAccessor(setQux), is(true));
		assertThat(ReflectionUtil.isAccessor(setQux2), is(false));
	}
	
	/**
	 * Test method for {@link ReflectionUtil#isGetter(java.lang.reflect.Method)}.
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testIsGetter() throws Exception {
		assertThat(ReflectionUtil.isGetter(getFoo), is(true));
		assertThat(ReflectionUtil.isGetter(getBar), is(true));
		assertThat(ReflectionUtil.isGetter(isBaz), is(true));
		assertThat(ReflectionUtil.isGetter(getQux), is(true));
		assertThat(ReflectionUtil.isGetter(getQux2), is(false));
		
		assertThat(ReflectionUtil.isGetter(setFoo), is(false));
		assertThat(ReflectionUtil.isGetter(setBar), is(false));
		assertThat(ReflectionUtil.isGetter(setBaz), is(false));
		assertThat(ReflectionUtil.isGetter(setQux), is(false));
		assertThat(ReflectionUtil.isGetter(setQux2), is(false));
	}
	
	/**
	 * Test method for {@link ReflectionUtil#isSetter(java.lang.reflect.Method)}.
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testIsSetter() throws Exception {
		assertThat(ReflectionUtil.isSetter(getFoo), is(false));
		assertThat(ReflectionUtil.isSetter(getBar), is(false));
		assertThat(ReflectionUtil.isSetter(isBaz), is(false));
		assertThat(ReflectionUtil.isSetter(getQux), is(false));
		assertThat(ReflectionUtil.isSetter(getQux2), is(false));
		
		assertThat(ReflectionUtil.isSetter(setFoo), is(true));
		assertThat(ReflectionUtil.isSetter(setBar), is(true));
		assertThat(ReflectionUtil.isSetter(setBaz), is(true));
		assertThat(ReflectionUtil.isSetter(setQux), is(true));
		assertThat(ReflectionUtil.isSetter(setQux2), is(false));
	}
	

	class TestBean {
		
		private int foo;
		
		private String bar;
		
		private boolean baz;
		
		private Boolean qux;
		

		String getBar() {
			return bar;
		}
		
		int getFoo() {
			return foo;
		}
		
		Boolean getQux() {
			return qux;
		}
		
		void getQux2() {
		}
		
		boolean isBaz() {
			return baz;
		}
		
		void setBar(String bar) {
			this.bar = bar;
		}
		
		void setBaz(boolean baz) {
			this.baz = baz;
		}
		
		void setFoo(int foo) {
			this.foo = foo;
		}
		
		void setQux(Boolean qux) {
			this.qux = qux;
		}
		
		void setQux2() {
		}
		
	}
}
