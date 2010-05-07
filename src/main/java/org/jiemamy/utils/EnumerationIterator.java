/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy.utils;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * {@link Enumeration}を {@link Iterator}にするためのアダブタ。
 * 
 * @param <E> 要素
 * @author j5ik2o
 */
public class EnumerationIterator<E> implements Iterator<E> {
	
	private Enumeration<E> enumeration = null;
	

	/**
	 * {@link EnumerationIterator}を作成します。
	 * 
	 * @param e {@link Enumeration}
	 */
	public EnumerationIterator(Enumeration<E> e) {
		if (e == null) {
			throw new NullPointerException("Enumeration");
		}
		this.enumeration = e;
	}
	
	public boolean hasNext() {
		return enumeration.hasMoreElements();
	}
	
	public E next() {
		return enumeration.nextElement();
	}
	
	public void remove() {
		throw new UnsupportedOperationException("remove");
	}
	
}
