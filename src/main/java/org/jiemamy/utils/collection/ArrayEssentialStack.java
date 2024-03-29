/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2008/12/16
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

import java.util.Collection;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Stack;

import org.apache.commons.lang.Validate;

/**
 * {@link EssentialStack}の既存スタック実装を用いた実装。
 * 
 * @param <E> 要素の型
 * @version $Id$
 * @author daisuke
 */
public class ArrayEssentialStack<E> implements EssentialStack<E> {
	
	/** 10% room for growth */
	@SuppressWarnings("unused")
	private static final double GROWTH_FACTOR = 1.1;
	
	private final Stack<E> stack;
	

	/**
	 * インスタンスを生成する。
	 */
	public ArrayEssentialStack() {
		stack = new Stack<E>();
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param collection 初期要素を保持したコレクション
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public ArrayEssentialStack(Collection<? extends E> collection) {
		Validate.notNull(collection);
//		int size = collection.size();
//		int capacity = (int) Math.min(size * GROWTH_FACTOR, Integer.MAX_VALUE);
		
//		stack = new Stack<E>(capacity);
		stack = new Stack<E>();
		for (E element : collection) {
			stack.push(element);
		}
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param source 初期要素を保持したスタック
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public ArrayEssentialStack(EssentialStack<? extends E> source) {
		Validate.notNull(source);
//		stack = new ArrayStack<E>(source.size());
		stack = new Stack<E>();
		for (E element : source) {
			stack.push(element);
		}
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param initialSize 初期サイズ
	 * @throws IllegalArgumentException if the specified initial size is negative
	 * @deprecated 初期サイズは指定できない。現在、引数は無視される。
	 */
	@Deprecated
	public ArrayEssentialStack(int initialSize) {
		Validate.isTrue(initialSize >= 0);
		stack = new Stack<E>();
	}
	
	public void clear() {
		stack.clear();
	}
	
	public E get(int n) {
		return stack.get(n);
	}
	
	public void insert(int index, E element) {
		stack.add(index, element);
	}
	
	public boolean isEmpty() {
		return stack.isEmpty();
	}
	
	public Iterator<E> iterator() {
		return stack.iterator();
	}
	
	public E peek() {
		return stack.peek();
	}
	
	public E peek(int n) {
		try {
			return stack.get(stack.size() - n - 1);
		} catch (EmptyStackException e) {
			throw new IndexOutOfBoundsException("Index: " + n + ", Size: " + size());
		}
	}
	
	public E pop() {
		return stack.pop();
	}
	
	public void push(E element) {
		stack.push(element);
	}
	
	public boolean remove(E element) {
		return stack.remove(element);
	}
	
	public E remove(int n) {
		return stack.remove(n);
	}
	
	public EssentialStack<E> reverse() {
		EssentialStack<E> result = new ArrayEssentialStack<E>();
		for (E element : stack) {
			result.insert(0, element);
		}
		return result;
	}
	
	public int size() {
		return stack.size();
	}
	
	@Override
	public String toString() {
		return stack.toString();
	}
}
