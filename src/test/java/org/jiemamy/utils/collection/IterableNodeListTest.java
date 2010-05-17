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
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class IterableNodeListTest {
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_creation() throws Exception {
		try {
			new IterableNodeList(null);
			fail();
		} catch (IllegalArgumentException e) {
			// success;
		}
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02() throws Exception {
		final List<Node> n = Arrays.asList(mock(Node.class), mock(Node.class), mock(Node.class));
		NodeList nodeList = mock(NodeList.class);
		when(nodeList.getLength()).thenReturn(3);
		when(nodeList.item(anyInt())).thenAnswer(new Answer<Node>() {
			
			public Node answer(InvocationOnMock invocation) throws Throwable {
				Integer i = (Integer) invocation.getArguments()[0];
				try {
					return n.get(i);
				} catch (IndexOutOfBoundsException e) {
					return null;
				}
			}
		});
		
		Iterable<Node> nodes = new IterableNodeList(nodeList);
		
		int index = 0;
		for (Node node : nodes) {
			assertThat(node, is(n.get(index++)));
		}
		
		Iterator<Node> iterator = nodes.iterator();
		
		assertThat(iterator.hasNext(), is(true));
		assertThat(iterator.next(), is(n.get(0)));
		assertThat(iterator.hasNext(), is(true));
		assertThat(iterator.next(), is(n.get(1)));
		assertThat(iterator.hasNext(), is(true));
		assertThat(iterator.next(), is(n.get(2)));
		assertThat(iterator.hasNext(), is(false));
		
		try {
			iterator.next();
			fail();
		} catch (NoSuchElementException e) {
			// success
		}
	}
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_remove() throws Exception {
		NodeList nodeList = mock(NodeList.class);
		when(nodeList.getLength()).thenReturn(0);
		when(nodeList.item(anyInt())).thenThrow(new NoSuchElementException());
		
		Iterable<Node> nodes = new IterableNodeList(nodeList);
		
		try {
			nodes.iterator().remove();
			fail();
		} catch (UnsupportedOperationException e) {
			// success
		}
	}
}
