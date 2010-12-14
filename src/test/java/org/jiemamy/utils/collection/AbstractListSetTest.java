/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/12/09
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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.junit.Test;

/**
 * {@link ListSet}のテストクラスの抽象実装。
 * 
 * @version $Id$
 * @author daisuke
 */
public abstract class AbstractListSetTest {
	
	/** サブクラスのセットアップメソッドで代入する */
	protected ListSet<Element> alhs;
	

	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_add_remove_size() throws Exception {
		assertThat(alhs.size(), is(0));
		assertThat(alhs.add(null), is(true));
		assertThat(alhs.size(), is(1));
		assertThat(alhs.add(null), is(false));
		assertThat(alhs.size(), is(1));
		assertThat(alhs.remove(null), is(true));
		assertThat(alhs.size(), is(0));
		assertThat(alhs.remove(null), is(false));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_get() throws Exception {
		alhs.add(Element.of(1));
		alhs.add(Element.of(3));
		alhs.add(Element.of(5));
		alhs.add(Element.of(2));
		alhs.add(Element.of(4));
		alhs.add(Element.of(3));
		alhs.add(Element.of(4));
		alhs.add(Element.of(6));
		alhs.add(null);
		
		assertThat(alhs.get(0), is(Element.of(1)));
		assertThat(alhs.get(1), is(Element.of(3)));
		assertThat(alhs.get(2), is(Element.of(5)));
		assertThat(alhs.get(3), is(Element.of(2)));
		assertThat(alhs.get(4), is(Element.of(4)));
		assertThat(alhs.get(5), is(Element.of(6)));
		assertThat(alhs.get(6), is(nullValue()));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_iterator() throws Exception {
		alhs.add(Element.of(1));
		alhs.add(Element.of(3));
		alhs.add(Element.of(5));
		alhs.add(Element.of(2));
		alhs.add(Element.of(4));
		alhs.add(Element.of(6));
		
		assertThat(alhs.size(), is(6));
		
		Iterator<Element> iterator = alhs.iterator();
		assertThat(iterator.next(), is(Element.of(1)));
		assertThat(iterator.next(), is(Element.of(3)));
		assertThat(iterator.next(), is(Element.of(5)));
		assertThat(iterator.next(), is(Element.of(2)));
		assertThat(iterator.next(), is(Element.of(4)));
		iterator.remove();
		assertThat(iterator.next(), is(Element.of(6)));
		
		assertThat(alhs.size(), is(5));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test04_listIterator() throws Exception {
		alhs.add(Element.of(1));
		alhs.add(Element.of(3));
		alhs.add(Element.of(5));
		alhs.add(Element.of(2));
		alhs.add(Element.of(4));
		alhs.add(Element.of(6));
		
		assertThat(alhs.size(), is(6));
		
		ListIterator<Element> iterator = alhs.listIterator();
		assertThat(iterator.hasPrevious(), is(false));
		assertThat(iterator.hasNext(), is(true));
		assertThat(iterator.next(), is(Element.of(1)));
		assertThat(iterator.hasPrevious(), is(true));
		assertThat(iterator.hasNext(), is(true));
		assertThat(iterator.next(), is(Element.of(3)));
		iterator.add(Element.of(30));
		assertThat(iterator.next(), is(Element.of(5)));
		assertThat(iterator.next(), is(Element.of(2)));
		iterator.set(Element.of(20));
		assertThat(iterator.next(), is(Element.of(4)));
		iterator.remove();
		assertThat(iterator.next(), is(Element.of(6)));
		assertThat(iterator.hasPrevious(), is(true));
		assertThat(iterator.hasNext(), is(false));
		
		assertThat(alhs.size(), is(6));
		
		assertThat(alhs.get(0), is(Element.of(1)));
		assertThat(alhs.get(1), is(Element.of(3)));
		assertThat(alhs.get(2), is(Element.of(30)));
		assertThat(alhs.get(3), is(Element.of(5)));
		assertThat(alhs.get(4), is(Element.of(20)));
		assertThat(alhs.get(5), is(Element.of(6)));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test05_subList() throws Exception {
		alhs.add(Element.of(1));
		alhs.add(Element.of(3));
		alhs.add(Element.of(5));
		alhs.add(Element.of(2));
		alhs.add(Element.of(4));
		alhs.add(Element.of(6));
		
		List<Element> subList = alhs.subList(1, 5);
		assertThat(subList.size(), is(4));
		assertThat(subList.get(0), is(Element.of(3)));
		assertThat(subList.get(1), is(Element.of(5)));
		assertThat(subList.get(2), is(Element.of(2)));
		assertThat(subList.get(3), is(Element.of(4)));
		
		subList.set(1, Element.of(50));
		alhs.set(3, Element.of(20));
		
		assertThat(subList.get(0), is(Element.of(3)));
		assertThat(subList.get(1), is(Element.of(50)));
		assertThat(subList.get(2), is(Element.of(20)));
		assertThat(subList.get(3), is(Element.of(4)));
		assertThat(alhs.get(0), is(Element.of(1)));
		assertThat(alhs.get(1), is(Element.of(3)));
		assertThat(alhs.get(2), is(Element.of(50)));
		assertThat(alhs.get(3), is(Element.of(20)));
		assertThat(alhs.get(4), is(Element.of(4)));
		assertThat(alhs.get(5), is(Element.of(6)));
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test06_set_and_replace() throws Exception {
		alhs.add(Element.of(5, "1st"));
		alhs.add(Element.of(5, "2nd"));
		
		assertThat(alhs.get(0).getMark(), is("1st"));
		assertThat(alhs.size(), is(1));
		
		CollectionsUtil.addOrReplace((List<Element>) alhs, Element.of(5, "3rd"));
		
		assertThat(alhs.get(0).getMark(), is("3rd"));
		assertThat(alhs.size(), is(1));
		
		CollectionsUtil.addOrReplace((Set<Element>) alhs, Element.of(5, "4th"));
		
		assertThat(alhs.get(0).getMark(), is("4th"));
		assertThat(alhs.size(), is(1));
		
		try {
			alhs.set(1, Element.of(0));
			fail();
		} catch (IndexOutOfBoundsException e) {
			// success
		}
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test07_set() throws Exception {
		alhs.add(Element.of(1));
		alhs.add(Element.of(3));
		alhs.add(Element.of(5));
		alhs.add(Element.of(2));
		alhs.add(Element.of(4));
		alhs.add(Element.of(6));
		
		alhs.set(0, Element.of(10));
		try {
			alhs.set(4, Element.of(6));
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
		
		assertThat(alhs.get(0), is(Element.of(10)));
		assertThat(alhs.get(1), is(Element.of(3)));
		assertThat(alhs.get(2), is(Element.of(5)));
		assertThat(alhs.get(3), is(Element.of(2)));
		assertThat(alhs.get(4), is(Element.of(4)));
		assertThat(alhs.get(5), is(Element.of(6)));
	}
}
