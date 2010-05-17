/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2009/03/04
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
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link ArrayEssentialStack}のテストクラス。
 * 
 * @author daisuke
 */
public class ArrayEssentialStackTest {
	
	private ArrayEssentialStack<Element> stack;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		stack = new ArrayEssentialStack<Element>();
	}
	
	/**
	 * テストの情報を破棄する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@After
	public void tearDown() throws Exception {
		stack = null;
	}
	
	/**
	 * {@link Element}の基本的なテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test00_qeuals() throws Exception {
		assertThat(Element.of(0).hashCode(), is(Element.of(0).hashCode()));
		assertThat(Element.of(0).equals(null), is(false));
		assertThat(Element.of(0).equals(new Object()), is(false));
	}
	
	/**
	 * 基本スタック機能のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_基本スタック機能のテスト() throws Exception {
		stack.push(Element.of(0));
		stack.push(Element.of(1));
		stack.push(Element.of(2));
		
		assertThat(stack.pop(), is(Element.of(2)));
		assertThat(stack.pop(), is(Element.of(1)));
		assertThat(stack.pop(), is(Element.of(0)));
		
		stack.push(Element.of(3));
		stack.push(Element.of(4));
		stack.push(Element.of(5));
		
		assertThat(stack.get(0), is(Element.of(3)));
		assertThat(stack.get(1), is(Element.of(4)));
		assertThat(stack.get(2), is(Element.of(5)));
		
		assertThat(stack.toString(), is("[3, 4, 5]"));
	}
	
	/**
	 * {@link EssentialStack#iterator()}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_iteratorのテスト() throws Exception {
		stack.push(new Element(0));
		stack.push(new Element(1));
		stack.push(new Element(2));
		
		int i = 0;
		for (Element e : stack) {
			assertThat(e.num, is(i++));
		}
	}
	
	/**
	 * {@link EssentialStack#reverse()}のテスト
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_reverseのテスト() throws Exception {
		stack.push(new Element(0));
		stack.push(new Element(1));
		stack.push(new Element(2));
		
		EssentialStack<Element> reverse = stack.reverse();
		
		int i = stack.size() - 1;
		for (Element e : reverse) {
			assertThat(e.num, is(i--));
		}
	}
	
	/**
	 * {@link EssentialStack#clear()}のテスト
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test04_clearのテスト() throws Exception {
		stack.push(new Element(0));
		stack.push(new Element(1));
		stack.push(new Element(2));
		
		assertThat(stack.size(), is(3));
		stack.clear();
		assertThat(stack.size(), is(0));
	}
	
	/**
	 * {@link EssentialStack#insert(int, Object)}のテスト
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test05_insertのテスト() throws Exception {
		stack.push(new Element(0));
		stack.push(new Element(2));
		stack.insert(1, new Element(1));
		
		assertThat(stack.pop().num, is(2));
		assertThat(stack.pop().num, is(1));
		assertThat(stack.pop().num, is(0));
	}
	
	/**
	 * {@link EssentialStack#isEmpty()}のテスト
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test06_isEmptyのテスト() throws Exception {
		assertThat(stack.size(), is(0));
		assertThat(stack.isEmpty(), is(true));
		
		try {
			stack.pop();
			fail();
		} catch (EmptyStackException e) {
			// success
		}
		
		stack.push(new Element(0));
		
		assertThat(stack.size(), is(1));
		assertThat(stack.isEmpty(), is(false));
	}
	
	/**
	 * {@link EssentialStack#peek()}, {@link EssentialStack#peek(int)}のテスト
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test07_peekのテスト() throws Exception {
		stack.push(new Element(0));
		stack.push(new Element(1));
		stack.push(new Element(2));
		
		assertThat(stack.peek().num, is(2));
		assertThat(stack.peek(2).num, is(0));
		assertThat(stack.peek(1).num, is(1));
		assertThat(stack.peek(0).num, is(2));
		
		try {
			stack.peek(100);
			fail();
		} catch (IndexOutOfBoundsException e) {
			// sucess
		}
		
	}
	
	/**
	 * {@link EssentialStack#remove(int)}, {@link EssentialStack#remove(Object)}のテスト
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test08_removeのテスト() throws Exception {
		Element three = new Element(3);
		stack.push(new Element(0));
		stack.push(new Element(1));
		stack.push(new Element(2));
		stack.push(three);
		stack.push(new Element(4));
		
		stack.remove(1);
		stack.remove(three);
		
		assertThat(stack.pop().num, is(4));
		assertThat(stack.pop().num, is(2));
		assertThat(stack.pop().num, is(0));
	}
	
	/**
	 * コンストラクタにコレクションを与えたスタックのテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test10_コンストラクタにコレクションを与えたスタックのテスト() throws Exception {
		List<Element> list = new ArrayList<Element>();
		list.add(new Element(0));
		list.add(new Element(1));
		list.add(new Element(2));
		stack = new ArrayEssentialStack<Element>(list);
		
		assertThat(stack.pop().num, is(2));
		assertThat(stack.pop().num, is(1));
		assertThat(stack.pop().num, is(0));
	}
	
	/**
	 * コンストラクタにスタックを与えたスタックのテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test11_コンストラクタにスタックを与えたスタックのテスト() throws Exception {
		EssentialStack<Element> source = new ArrayEssentialStack<Element>();
		source.push(new Element(0));
		source.push(new Element(1));
		source.push(new Element(2));
		stack = new ArrayEssentialStack<Element>(source); // 同要素スタックのコピーを作る
		
		assertThat(stack.size(), is(3));
		assertThat(source.size(), is(3));
		
		assertThat(stack.pop().num, is(2));
		assertThat(stack.pop().num, is(1));
		assertThat(stack.pop().num, is(0));
		
		assertThat(stack.size(), is(0));
		assertThat(source.size(), is(3)); // sourceに影響しない
	}
	
	/**
	 * 各メソッドに{@code null}を与えた場合のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	@SuppressWarnings("unused")
	public void test12_nullテスト() throws Exception {
		try {
			new ArrayEssentialStack<Element>((Collection<Element>) null);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
		try {
			new ArrayEssentialStack<Element>((EssentialStack<Element>) null);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
		try {
			new ArrayEssentialStack<Element>(-1);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			stack.get(100);
			fail();
		} catch (IndexOutOfBoundsException e) {
			//success
		}
		try {
			stack.insert(100, Element.of(100));
			fail();
		} catch (IndexOutOfBoundsException e) {
			//success
		}
		try {
			stack.peek(100);
			fail();
		} catch (IndexOutOfBoundsException e) {
			//success
		}
		try {
			stack.remove(100);
			fail();
		} catch (IndexOutOfBoundsException e) {
			//success
		}
		try {
			stack.peek();
			fail();
		} catch (EmptyStackException e) {
			//success
		}
		try {
			stack.pop();
			fail();
		} catch (EmptyStackException e) {
			//success
		}
		
		stack.push(Element.of(1));
		
		try {
			stack.get(100);
			fail();
		} catch (IndexOutOfBoundsException e) {
			//success
		}
		try {
			stack.insert(100, Element.of(100));
			fail();
		} catch (IndexOutOfBoundsException e) {
			//success
		}
		try {
			stack.peek(100);
			fail();
		} catch (IndexOutOfBoundsException e) {
			//success
		}
		try {
			stack.remove(100);
			fail();
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}
}
