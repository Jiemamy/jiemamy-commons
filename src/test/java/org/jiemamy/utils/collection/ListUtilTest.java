/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2009/02/23
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
package org.jiemamy.utils.collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.jiemamy.utils.collection.ListUtil;

/**
 * {@link ListUtil}のテストクラス。
 * 
 * @author daisuke
 */
public class ListUtilTest {
	
	private List<String> list;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		list = new ArrayList<String>();
		list.add("foo");
		list.add("bar");
		list.add("baz");
	}
	
	/**
	 * テストの情報を破棄する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@After
	public void tearDown() throws Exception {
		list = null;
	}
	
	/**
	 * {@link ListUtil#moveDown(List, int)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_moveDown() throws Exception {
		try {
			ListUtil.moveDown(null, 0);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
		
		ListUtil.moveDown(list, 0);
		assertThat(list.get(0), is("bar"));
		assertThat(list.get(1), is("foo"));
		assertThat(list.get(2), is("baz"));
		
		ListUtil.moveDown(list, 1);
		assertThat(list.get(0), is("bar"));
		assertThat(list.get(1), is("baz"));
		assertThat(list.get(2), is("foo"));
	}
	
	/**
	 * {@link ListUtil#moveUp(List, int)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_moveUp() throws Exception {
		try {
			ListUtil.moveUp(list, -1);
			fail();
		} catch (IndexOutOfBoundsException e) {
			// success
		}
		
		ListUtil.moveUp(list, 1);
		assertThat(list.get(0), is("bar"));
		assertThat(list.get(1), is("foo"));
		assertThat(list.get(2), is("baz"));
		
		ListUtil.moveUp(list, 2);
		assertThat(list.get(0), is("bar"));
		assertThat(list.get(1), is("baz"));
		assertThat(list.get(2), is("foo"));
	}
	
	/**
	 * {@link ListUtil#reverse(List)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_reverse() throws Exception {
		try {
			ListUtil.reverse(null);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
		
		ListUtil.reverse(list);
		assertThat(list.get(0), is("baz"));
		assertThat(list.get(1), is("bar"));
		assertThat(list.get(2), is("foo"));
	}
}
