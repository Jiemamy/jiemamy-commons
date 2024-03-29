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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * {@link EssentialQueue}の{@link LinkedList}を用いた実装。
 * 
 * @param <E> 要素の型
 * @version $Id$
 * @author daisuke
 */
public class LinkedEssentialQueue<E> implements EssentialQueue<E> {
	
	LinkedList<E> linkedList = new LinkedList<E>();
	

	public void clear() {
		linkedList.clear();
	}
	
	public E dequeue() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return linkedList.poll();
	}
	
	public void enqueue(E element) {
		linkedList.offer(element);
	}
	
	public void enqueue(EssentialQueue<E> queue) {
		while (queue.isEmpty() == false) {
			E element = queue.dequeue();
			enqueue(element);
		}
	}
	
	public boolean isEmpty() {
		return linkedList.isEmpty();
	}
	
	public Iterator<E> iterator() {
		return linkedList.iterator();
	}
	
	public E peek() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return linkedList.peek();
	}
	
	public int size() {
		return linkedList.size();
	}
	
	@Override
	public String toString() {
		return linkedList.toString();
	}
	
}
