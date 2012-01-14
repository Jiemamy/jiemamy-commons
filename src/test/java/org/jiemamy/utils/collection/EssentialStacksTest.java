/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/03/05
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

import org.junit.Before;
import org.junit.Test;

/**
 * {@link EssentialStacks}のテストクラス。
 * 
 * @author daisuke
 */
public class EssentialStacksTest {
	
	private ArrayEssentialStack<Element> stackA;
	
	private ArrayEssentialStack<Element> stackB;
	
	private ArrayEssentialStack<Element> stackC;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		stackA = new ArrayEssentialStack<Element>();
		stackB = new ArrayEssentialStack<Element>();
		stackC = new ArrayEssentialStack<Element>();
		
		Element zero = Element.of(0);
		Element one = Element.of(1);
		Element two = Element.of(2);
		Element three = Element.of(3);
		Element four = Element.of(4);
		
		stackA.push(zero);
		stackA.push(one);
		stackA.push(two);
		stackA.push(three);
		
		stackB.push(zero);
		stackB.push(one);
		stackB.push(three);
		stackB.push(four);
		
		stackC.push(zero);
	}
	
	/**
	 * {@link EssentialStacks#intersection(EssentialStack, EssentialStack)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_intersection1() throws Exception {
		EssentialStack<Element> intersection = EssentialStacks.intersection(stackA, stackB);
		assertThat(intersection.size(), is(2));
		assertThat(intersection.pop(), is(Element.of(1)));
		assertThat(intersection.pop(), is(Element.of(0)));
	}
	
	/**
	 * {@link EssentialStacks#intersection(EssentialStack, EssentialStack)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_intersection2() throws Exception {
		EssentialStack<Element> intersection = EssentialStacks.intersection(stackB, stackC);
		assertThat(intersection.size(), is(1));
		assertThat(intersection.pop(), is(Element.of(0)));
	}
	
	/**
	 * {@link EssentialStacks#minus(EssentialStack, EssentialStack)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_minus() throws Exception {
		EssentialStack<Element> minus = EssentialStacks.minus(stackA, stackB);
		assertThat(minus.size(), is(1));
		assertThat(minus.pop(), is(Element.of(2)));
	}
}
