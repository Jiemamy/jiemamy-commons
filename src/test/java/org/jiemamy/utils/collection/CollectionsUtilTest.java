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
package org.jiemamy.utils.collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Delayed;

import org.apache.commons.collections15.map.HashedMap;
import org.apache.commons.collections15.set.UnmodifiableSet;
import org.junit.Test;

/**
 * {@link CollectionsUtil}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class CollectionsUtilTest {
	
	/**
	 * {@link CollectionsUtil#addOrReplace(Set, Object)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_addOrReplace() throws Exception {
		Set<Element> set = new HashSet<Element>();
		Element e1 = Element.of(1);
		Element e2 = Element.of(1);
		Element e3 = Element.of(10);
		
		// Elementの特性確認
		assertThat(e1, is(not(sameInstance(e2))));
		// but
		assertThat(e1, is(equalTo(e2)));
		
		// Setの特性の確認
		set.add(e1);
		set.add(e2);
		assertThat(set.size(), is(1));
		assertThat(set.iterator().next(), is(sameInstance(e1)));
		
		// setにe2をaddOrReplaceすると、e1がe2に置き換わる
		Element removed = CollectionsUtil.addOrReplace(set, e2);
		assertThat(set.size(), is(1));
		assertThat(set.iterator().next(), is(sameInstance(e2)));
		assertThat(removed, is(sameInstance(e1)));
		
		// setにe3をaddOrReplaceすると、単純にaddされる
		Element removed2 = CollectionsUtil.addOrReplace(set, e3);
		assertThat(set.size(), is(2));
		assertThat(set, hasItem(e3));
		assertThat(removed2, is(nullValue()));
	}
	
	/**
	 * {@link CollectionsUtil#addOrReplace(Set, Object)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_addOrReplace_failed1() throws Exception {
		Set<Element> set = new HashSet<Element>();
		Element e1 = Element.of(1);
		Element e2 = Element.of(2);
		Element e3 = Element.of(2);
		
		set.add(e1);
		set.add(e2);
		
		Set<Element> mock = spy(set);
		when(mock.remove(any(Element.class))).thenReturn(false);
		
		try {
			CollectionsUtil.addOrReplace(mock, e3);
			fail();
		} catch (CollectionModificationException e) {
			assertThat(e.getMessage(), is("cannot remove"));
			// success
		}
	}
	
	/**
	 * {@link CollectionsUtil#addOrReplace(Set, Object)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_addOrReplace_failed2() throws Exception {
		Set<Element> set = new HashSet<Element>();
		Element e1 = Element.of(1);
		Element e2 = Element.of(2);
		Element e3 = Element.of(2);
		
		set.add(e1);
		set.add(e2);
		
		Set<Element> mock = spy(set);
		when(mock.add(any(Element.class))).thenReturn(false);
		
		try {
			CollectionsUtil.addOrReplace(mock, e3);
			fail();
		} catch (CollectionModificationException e) {
			assertThat(e.getMessage(), is("cannot add"));
			// success
		}
	}
	
	/**
	 * {@link CollectionsUtil#addOrReplace(Set, Object)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_addOrReplace_failed3() throws Exception {
		Set<Element> set = new HashSet<Element>();
		Element e1 = Element.of(1);
		Element e2 = Element.of(2);
		Element e3 = Element.of(2);
		
		set.add(e1);
		set.add(e2);
		
		Set<Element> mock = spy(set);
		when(mock.add(any(Element.class))).thenThrow(new UnsupportedOperationException());
		
		try {
			CollectionsUtil.addOrReplace(mock, e3);
			fail();
		} catch (CollectionModificationException e) {
			assertThat(e.getMessage(), is("cannot add"));
			// success
		}
	}
	
	/**
	 * {@link CollectionsUtil#addOrReplace(Set, Object)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_addOrReplace_failed4() throws Exception {
		Set<Element> set = new HashSet<Element>();
		Element e1 = Element.of(1);
		Element e2 = Element.of(2);
		Element e3 = Element.of(2);
		
		set.add(e1);
		set.add(e2);
		
		Set<Element> unmod = UnmodifiableSet.decorate(set);
		try {
			CollectionsUtil.addOrReplace(unmod, e3);
			fail();
		} catch (CollectionModificationException e) {
			assertThat(e.getMessage(), is("cannot remove"));
		}
	}
	
	/**
	 * {@link CollectionsUtil#newArrayList()}系のテスト
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void test_new() throws Exception {
		ArrayList<Integer> noEmptyList = new ArrayList<Integer>();
		noEmptyList.add(1);
		noEmptyList.add(2);
		noEmptyList.add(3);
		
		assertThat(CollectionsUtil.newArrayBlockingQueue(1), is(notNullValue()));
		assertThat(CollectionsUtil.newArrayBlockingQueue(1, true), is(notNullValue()));
		assertThat(CollectionsUtil.newArrayBlockingQueue(1, true, new ArrayList<Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newArrayList(), is(notNullValue()));
		assertThat(CollectionsUtil.newArrayList(1), is(notNullValue()));
		assertThat(CollectionsUtil.newArrayList(new ArrayList<Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newConcurrentHashMap(), is(notNullValue()));
		assertThat(CollectionsUtil.newConcurrentHashMap(1), is(notNullValue()));
		assertThat(CollectionsUtil.newConcurrentHashMap(1, 0.5f, 1), is(notNullValue()));
		assertThat(CollectionsUtil.newConcurrentHashMap(new HashedMap<Void, Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newConcurrentLinkedQueue(), is(notNullValue()));
		assertThat(CollectionsUtil.newConcurrentLinkedQueue(new ArrayList<Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newCopyOnWriteArrayList(), is(notNullValue()));
		assertThat(CollectionsUtil.newCopyOnWriteArrayList(new ArrayList<Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newCopyOnWriteArrayList(new Integer[0]), is(notNullValue()));
		assertThat(CollectionsUtil.newCopyOnWriteArraySet(), is(notNullValue()));
		assertThat(CollectionsUtil.newCopyOnWriteArraySet(new ArrayList<Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newDelayQueue(), is(notNullValue()));
		assertThat(CollectionsUtil.newDelayQueue(new ArrayList<Delayed>()), is(notNullValue()));
		assertThat(CollectionsUtil.newHashMap(), is(notNullValue()));
		assertThat(CollectionsUtil.newHashMap(1), is(notNullValue()));
		assertThat(CollectionsUtil.newHashMap(new HashMap<Void, Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newHashMap(1, 0.5f), is(notNullValue()));
		assertThat(CollectionsUtil.newHashSet(), is(notNullValue()));
		assertThat(CollectionsUtil.newHashSet(new ArrayList<Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newHashSet(1), is(notNullValue()));
		assertThat(CollectionsUtil.newHashSet(1, 0.5f), is(notNullValue()));
		assertThat(CollectionsUtil.newHashtable(), is(notNullValue()));
		assertThat(CollectionsUtil.newHashtable(1), is(notNullValue()));
		assertThat(CollectionsUtil.newHashtable(new HashedMap<Void, Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newHashtable(1, 0.5f), is(notNullValue()));
		assertThat(CollectionsUtil.newIdentityHashMap(), is(notNullValue()));
		assertThat(CollectionsUtil.newIdentityHashMap(1), is(notNullValue()));
		assertThat(CollectionsUtil.newIdentityHashMap(new HashMap<Void, Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newLinkedBlockingQueue(), is(notNullValue()));
		assertThat(CollectionsUtil.newLinkedBlockingQueue(new ArrayList<Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newLinkedBlockingQueue(1), is(notNullValue()));
		assertThat(CollectionsUtil.newLinkedHashMap(), is(notNullValue()));
		assertThat(CollectionsUtil.newLinkedHashMap(1), is(notNullValue()));
		assertThat(CollectionsUtil.newLinkedHashMap(new HashMap<Void, Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newLinkedHashMap(1, 0.5f), is(notNullValue()));
		assertThat(CollectionsUtil.newLinkedHashSet(), is(notNullValue()));
		assertThat(CollectionsUtil.newLinkedHashSet(new ArrayList<Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newLinkedHashSet(1), is(notNullValue()));
		assertThat(CollectionsUtil.newLinkedHashSet(1, 0.5f), is(notNullValue()));
		assertThat(CollectionsUtil.newLinkedList(), is(notNullValue()));
		assertThat(CollectionsUtil.newLinkedList(new ArrayList<Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newPriorityBlockingQueue(), is(notNullValue()));
		assertThat(CollectionsUtil.newPriorityBlockingQueue(new ArrayList<Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newPriorityBlockingQueue(1), is(notNullValue()));
		assertThat(CollectionsUtil.newPriorityBlockingQueue(1, mock(Comparator.class)), is(notNullValue()));
		assertThat(CollectionsUtil.newPriorityQueue(), is(notNullValue()));
		assertThat(CollectionsUtil.newPriorityQueue(new ArrayList<Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newPriorityQueue(new PriorityQueue<Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newPriorityQueue(new TreeSet<Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newPriorityQueue(1), is(notNullValue()));
		assertThat(CollectionsUtil.newPriorityQueue(1, mock(Comparator.class)), is(notNullValue()));
		assertThat(CollectionsUtil.newStack(), is(notNullValue()));
		assertThat(CollectionsUtil.newTreeMap(), is(notNullValue()));
		assertThat(CollectionsUtil.newTreeMap(mock(Comparator.class)), is(notNullValue()));
		assertThat(CollectionsUtil.newTreeMap(new HashMap<Void, Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newTreeMap(new TreeMap<Void, Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newTreeSet(), is(notNullValue()));
		assertThat(CollectionsUtil.newTreeSet(new ArrayList<Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newTreeSet(mock(Comparator.class)), is(notNullValue()));
		assertThat(CollectionsUtil.newTreeSet(new TreeSet<Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newVector(), is(notNullValue()));
		assertThat(CollectionsUtil.newVector(noEmptyList), is(notNullValue()));
		assertThat(CollectionsUtil.newVector(1), is(notNullValue()));
		assertThat(CollectionsUtil.newVector(1, 1), is(notNullValue()));
		assertThat(CollectionsUtil.newWeakHashMap(), is(notNullValue()));
		assertThat(CollectionsUtil.newWeakHashMap(1), is(notNullValue()));
		assertThat(CollectionsUtil.newWeakHashMap(new HashMap<Void, Void>()), is(notNullValue()));
		assertThat(CollectionsUtil.newWeakHashMap(1, 0.5f), is(notNullValue()));
	}
}
