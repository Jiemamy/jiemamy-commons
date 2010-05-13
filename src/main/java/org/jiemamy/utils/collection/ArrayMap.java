/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on Apr 8, 2009
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * リストのような属性を持つマップクラス。
 * 
 * @param <K> キーの型
 * @param <V> 値の型
 * @author j5ik2o
 */
@SuppressWarnings("serial")
public class ArrayMap<K, V> extends HashMap<K, V> {
	
	private List<V> valueList = CollectionsUtil.newArrayList();
	

	/**
	 * インスタンスを生成する。
	 */
	public ArrayMap() {
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param capacity キャパシティ
	 */
	public ArrayMap(int capacity) {
		super(capacity);
		valueList = CollectionsUtil.newArrayList(capacity);
	}
	
	/**
	 * @see List#contains(Object)
	 * 
	 * @param o List#contains(Object)の引数
	 * @return List#contains(Object)の戻り値
	 */
	public boolean contains(Object o) {
		return valueList.contains(o);
	}
	
	/**
	 * @see List#containsAll(Collection)
	 * 
	 * @param c List#containsAll(Collection)の引数
	 * @return List#containsAll(Collection)の戻り値
	 */
	public boolean containsAll(Collection<?> c) {
		return valueList.containsAll(c);
	}
	
	/**
	 * @see List#get(int)
	 * 
	 * @param index List#get(int)の引数
	 * @return List#get(int)の戻り値
	 */
	public V get(int index) {
		return valueList.get(index);
	}
	
	/**
	 * @see List#indexOf(Object)
	 * 
	 * @param o {@link List#indexOf(Object)}の引数
	 * @return　{@link List#indexOf(Object)}の戻り値
	 */
	public int indexOf(Object o) {
		return valueList.indexOf(o);
	}
	
	/**
	 * @see List#iterator()
	 * 
	 * @return {@link List#iterator()}の戻り値
	 */
	public Iterator<V> iterator() {
		return valueList.iterator();
	}
	
	/**
	 * @see List#lastIndexOf(Object)
	 * 
	 * @param o {@link List#lastIndexOf(Object)}の引数
	 * @return {@link List#lastIndexOf(Object)}の戻り値
	 */
	public int lastIndexOf(Object o) {
		return valueList.lastIndexOf(o);
	}
	
	/**
	 * @see List#listIterator()
	 * 
	 * @return {@link List#listIterator()}の戻り値
	 */
	public ListIterator<V> listIterator() {
		return valueList.listIterator();
	}
	
	/**
	 * @see List#listIterator(int)
	 * 
	 * @param index {@link List#listIterator(int)}の引数
	 * @return {@link List#listIterator(int)}の戻り値
	 */
	public ListIterator<V> listIterator(int index) {
		return valueList.listIterator(index);
	}
	
	@Override
	public V put(K key, V value) {
		V result = super.put(key, value);
		valueList.add(value);
		return result;
	}
	
	@Override
	public V remove(Object key) {
		V result = super.remove(key);
		if (result != null) {
			valueList.remove(result);
		}
		return result;
	}
	
	/**
	 * @see List#subList(int, int)
	 * 
	 * @param fromIndex {@link List#subList(int, int)}の引数
	 * @param toIndex {@link List#subList(int, int)}の引数
	 * @return {@link List#subList(int, int)}の戻り値
	 */
	public List<V> subList(int fromIndex, int toIndex) {
		return valueList.subList(fromIndex, toIndex);
	}
	
	/**
	 * @see List#toArray()
	 * 
	 * @param <T> {@link List#toArray()}の戻り値の型
	 * @return {@link List#toArray()}の戻り値
	 */
	@SuppressWarnings("unchecked")
	public <T>T[] toArray() {
		return (T[]) valueList.toArray();
	}
	
	/**
	 * @see List#toArray(Object[])
	 * 
	 * @param <T> {@link List#toArray(Object[])}の戻り値の型
	 * @param values {@link List#toArray(Object[])}の引数
	 * @return {@link List#toArray(Object[])}の戻り値
	 */
	public <T>T[] toArray(T[] values) {
		return valueList.toArray(values);
	}
	
}
