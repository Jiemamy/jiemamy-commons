/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/18
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
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link LinkedEssentialQueue}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class LinkedEssentialQueueTest {
	
	private LinkedEssentialQueue<Element> queue;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		queue = new LinkedEssentialQueue<Element>();
	}
	
	/**
	 * テストの情報を破棄する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@After
	public void tearDown() throws Exception {
		queue = null;
	}
	
	/**
	 * Test method for {@link org.jiemamy.utils.collection.LinkedEssentialQueue#clear()}.
	 */
	@Test
	public void testClear() {
		queue.enqueue(Element.of(0));
		assertThat(queue.size(), is(1));
		queue.clear();
		assertThat(queue.size(), is(0));
	}
	
	/**
	 * Test method for {@link org.jiemamy.utils.collection.LinkedEssentialQueue#dequeue()}.
	 */
	@Test
	public void testDequeue() {
		queue.enqueue(Element.of(100));
		assertThat(queue.dequeue(), is(Element.of(100)));
		
		try {
			queue.dequeue();
			fail();
		} catch (NoSuchElementException e) {
			// success
		}
	}
	
	/**
	 * Test method for {@link org.jiemamy.utils.collection.LinkedEssentialQueue#enqueue(java.lang.Object)}.
	 */
	@Test
	public void testEnqueueE() {
		assertThat(queue.size(), is(0));
		queue.enqueue(Element.of(0));
		assertThat(queue.size(), is(1));
	}
	
	/**
	 * Test method for {@link org.jiemamy.utils.collection.LinkedEssentialQueue#enqueue(org.jiemamy.utils.collection.EssentialQueue)}.
	 */
	@Test
	public void testEnqueueEssentialQueueOfE() {
		EssentialQueue<Element> q2 = new LinkedEssentialQueue<Element>();
		q2.enqueue(Element.of(0));
		q2.enqueue(Element.of(1));
		q2.enqueue(Element.of(2));
		
		queue.enqueue(Element.of(100));
		queue.enqueue(q2);
		
		assertThat(queue.size(), is(4));
		assertThat(queue.dequeue(), is(Element.of(100)));
		assertThat(queue.dequeue(), is(Element.of(0)));
		assertThat(queue.dequeue(), is(Element.of(1)));
		assertThat(queue.dequeue(), is(Element.of(2)));
	}
	
	/**
	 * Test method for {@link org.jiemamy.utils.collection.LinkedEssentialQueue#isEmpty()}.
	 */
	@Test
	public void testIsEmpty() {
		assertThat(queue.isEmpty(), is(true));
		queue.enqueue(Element.of(0));
		assertThat(queue.isEmpty(), is(false));
		queue.clear();
		assertThat(queue.isEmpty(), is(true));
	}
	
	/**
	 * Test method for {@link org.jiemamy.utils.collection.LinkedEssentialQueue#iterator()}.
	 */
	@Test
	public void testIterator() {
		queue.enqueue(Element.of(0));
		queue.enqueue(Element.of(1));
		queue.enqueue(Element.of(2));
		Iterator<Element> iterator = queue.iterator();
		
		assertThat(iterator.hasNext(), is(true));
		assertThat(iterator.next(), is(Element.of(0)));
		assertThat(iterator.hasNext(), is(true));
		assertThat(iterator.next(), is(Element.of(1)));
		assertThat(iterator.hasNext(), is(true));
		assertThat(iterator.next(), is(Element.of(2)));
		assertThat(iterator.hasNext(), is(false));
		
		try {
			iterator.next();
			fail();
		} catch (NoSuchElementException e) {
			// success
		}
	}
	
	/**
	 * Test method for {@link org.jiemamy.utils.collection.LinkedEssentialQueue#peek()}.
	 */
	@Test
	public void testPeek() {
		queue.enqueue(Element.of(0));
		queue.enqueue(Element.of(1));
		queue.enqueue(Element.of(2));
		assertThat(queue.peek(), is(Element.of(0)));
		assertThat(queue.peek(), is(Element.of(0)));
		assertThat(queue.peek(), is(Element.of(0)));
		queue.dequeue();
		assertThat(queue.peek(), is(Element.of(1)));
		assertThat(queue.peek(), is(Element.of(1)));
		assertThat(queue.peek(), is(Element.of(1)));
		
		queue.clear();
		try {
			queue.peek();
			fail();
		} catch (NoSuchElementException e) {
			// success
		}
	}
	
	/**
	 * Test method for {@link org.jiemamy.utils.collection.LinkedEssentialQueue#size()}.
	 */
	@Test
	public void testSize() {
		assertThat(queue.size(), is(0));
		queue.enqueue(Element.of(0));
		assertThat(queue.size(), is(1));
		queue.enqueue(Element.of(1));
		assertThat(queue.size(), is(2));
		queue.enqueue(Element.of(2));
		assertThat(queue.size(), is(3));
		queue.dequeue();
		assertThat(queue.size(), is(2));
		queue.dequeue();
		assertThat(queue.size(), is(1));
		queue.dequeue();
		assertThat(queue.size(), is(0));
	}
	
	/**
	 * Test method for {@link org.jiemamy.utils.collection.LinkedEssentialQueue#toString()}.
	 */
	@Test
	public void testToString() {
		queue.enqueue(Element.of(0));
		queue.enqueue(Element.of(1));
		queue.enqueue(Element.of(2));
		assertThat(queue.toString(), is("[0, 1, 2]"));
	}
}
