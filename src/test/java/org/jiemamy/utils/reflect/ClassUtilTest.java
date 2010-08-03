/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/08/03
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
package org.jiemamy.utils.reflect;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * {@link ClassUtil}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class ClassUtilTest {
	
	/**
	 * {@link ClassUtil#concatName(String, String)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_concatName() throws Exception {
		assertThat(ClassUtil.concatName("org", "jiemamy"), is("org.jiemamy"));
		assertThat(ClassUtil.concatName("org.jiemamy", "util"), is("org.jiemamy.util"));
		assertThat(ClassUtil.concatName("org.jiemamy", "JiemamyError"), is("org.jiemamy.JiemamyError"));
		assertThat(ClassUtil.concatName("", ""), is(""));
		assertThat(ClassUtil.concatName(null, "a"), is("a"));
		assertThat(ClassUtil.concatName("b", null), is("b"));
		assertThat(ClassUtil.concatName(null, null), is(nullValue()));
	}
	
	/**
	 * {@link ClassUtil#getPackageName(Class)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getPackageName() throws Exception {
		assertThat(ClassUtil.getPackageName(this.getClass()), is("org.jiemamy.utils.reflect"));
		assertThat(ClassUtil.getPackageName(Integer.class), is("java.lang"));
	}
	
	/**
	 * {@link ClassUtil#getResourcePath(Class)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getResourcePathClass() throws Exception {
		assertThat(ClassUtil.getResourcePath(Integer.class), is("java/lang/Integer.class"));
	}
	
	/**
	 * {@link ClassUtil#getResourcePath(String)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getResourcePathString() throws Exception {
		assertThat(ClassUtil.getResourcePath(Integer.class.getName()), is("java/lang/Integer.class"));
	}
}
