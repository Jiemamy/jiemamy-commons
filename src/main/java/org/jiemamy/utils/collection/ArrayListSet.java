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

import java.util.AbstractList;
import java.util.ArrayList;

import com.google.common.collect.Lists;

/**
 * {@link ArrayList}を用いた {@link ListSet}の実装クラス。
 * 
 * @param <E> 要素の型
 * @version $Id$
 * @author daisuke
 */
public class ArrayListSet<E> extends AbstractList<E> implements ListSet<E> {
	
	private final ArrayList<E> list = Lists.newArrayList();
	

	@Override
	public boolean add(E element) {
		boolean result = list.contains(element) == false;
		super.add(element);
		return result;
	}
	
	@Override
	public void add(int index, E element) {
		if (list.contains(element) == false) {
			list.add(index, element);
		}
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
		ArrayListSet<?> other = (ArrayListSet<?>) obj;
		if (list == null) {
			if (other.list != null) {
				return false;
			}
		} else if (!list.equals(other.list)) {
			return false;
		}
		return true;
	}
	
	@Override
	public E get(int index) {
		return list.get(index);
	}
	
	@Override
	public int hashCode() {
		return list.hashCode();
	}
	
	@Override
	public E remove(int index) {
		return list.remove(index);
	}
	
	@Override
	public E set(int index, E element) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
		}
		E prev = list.get(index);
		list.remove(index);
		if (list.contains(element) == false) {
			list.add(index, element);
			return prev;
		} else {
			list.add(index, prev);
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	public int size() {
		return list.size();
	}
	
}
