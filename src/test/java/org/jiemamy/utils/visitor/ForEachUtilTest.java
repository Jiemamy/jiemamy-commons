/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/21
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
package org.jiemamy.utils.visitor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * {@link ForEachUtil}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class ForEachUtilTest {
	
	/**
	 * {@link ForEachUtil#accept(Iterable, org.jiemamy.utils.visitor.ForEachUtil.CollectionVisitor)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_accept_iterable1() throws Exception {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Integer accepted =
				ForEachUtil.accept(list, new AbstractCollectionVisitor<Integer, Integer, RuntimeException>() {
					
					public Integer visit(Integer element) throws RuntimeException {
						finalResult += element;
						return null;
					}
					
					@Override
					protected void init() {
						finalResult = 0;
					}
				});
		assertThat(accepted, is(6));
	}
	
	/**
	 * {@link ForEachUtil#accept(Iterable, org.jiemamy.utils.visitor.ForEachUtil.CollectionVisitor)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_accept_iterable2() throws Exception {
		List<Integer> list = Arrays.asList(1, 2, 30);
		Integer accepted =
				ForEachUtil.accept(list, new AbstractCollectionVisitor<Integer, Integer, RuntimeException>() {
					
					public Integer visit(Integer element) throws RuntimeException {
						if (element > 10) {
							return finalResult;
						}
						finalResult += element;
						return null;
					}
					
					@Override
					protected void init() {
						finalResult = 0;
					}
				});
		assertThat(accepted, is(3));
	}
	
	/**
	 * {@link ForEachUtil#accept(Map, org.jiemamy.utils.visitor.ForEachUtil.MapVisitor)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_accept_map1() throws Exception {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(1, 10);
		map.put(2, 20);
		map.put(3, 30);
		Integer accepted =
				ForEachUtil.accept(map, new AbstractMapVisitor<Integer, Integer, Integer, RuntimeException>() {
					
					public Integer visit(Integer key, Integer value) throws RuntimeException {
						finalResult += key + value;
						return null;
					}
					
					@Override
					protected void init() {
						finalResult = 0;
					}
					
				});
		assertThat(accepted, is(66));
	}
	
	/**
	 * {@link ForEachUtil#accept(Map, org.jiemamy.utils.visitor.ForEachUtil.MapVisitor)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_accept_map2() throws Exception {
		Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
		map.put(1, 10);
		map.put(2, 20);
		map.put(3, 30);
		Integer accepted =
				ForEachUtil.accept(map, new AbstractMapVisitor<Integer, Integer, Integer, RuntimeException>() {
					
					public Integer visit(Integer key, Integer value) throws RuntimeException {
						if (value > 10) {
							return finalResult;
						}
						finalResult += key + value;
						return null;
					}
					
					@Override
					protected void init() {
						finalResult = 0;
					}
					
				});
		assertThat(accepted, is(11));
	}
}
