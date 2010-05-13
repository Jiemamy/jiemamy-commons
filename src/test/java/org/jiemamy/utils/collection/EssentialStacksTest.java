/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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

import org.jiemamy.utils.collection.ArrayEssentialStack;
import org.jiemamy.utils.collection.EssentialStack;
import org.jiemamy.utils.collection.EssentialStacks;

/**
 * {@link EssentialStacks}のテストクラス。
 * 
 * @author daisuke
 */
public class EssentialStacksTest {
	
	private ArrayEssentialStack<Element> stackA;
	
	private ArrayEssentialStack<Element> stackB;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		stackA = new ArrayEssentialStack<Element>();
		stackB = new ArrayEssentialStack<Element>();
		
		Element zero = new Element(0);
		Element one = new Element(1);
		Element two = new Element(2);
		Element three = new Element(3);
		Element four = new Element(4);
		
		stackA.push(zero);
		stackA.push(one);
		stackA.push(two);
		stackA.push(three);
		
		stackB.push(zero);
		stackB.push(one);
		stackB.push(three);
		stackB.push(four);
	}
	
	/**
	 * {@link EssentialStacks#intersection(EssentialStack, EssentialStack)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_intersection() throws Exception {
		EssentialStack<Element> intersection = EssentialStacks.intersection(stackA, stackB);
		assertThat(intersection.size(), is(2));
		assertThat(intersection.pop(), is(Element.of(1)));
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
	

	private static class Element {
		
		static Element of(int num) {
			return new Element(num);
		}
		

		final int num;
		

		Element(int num) {
			this.num = num;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Element other = (Element) obj;
			if (num != other.num) {
				return false;
			}
			return true;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + num;
			return result;
		}
		
		@Override
		public String toString() {
			return String.valueOf(num);
		}
	}
}
