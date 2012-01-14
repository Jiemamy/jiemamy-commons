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

import java.util.EmptyStackException;

/**
 * 本質的なスタックのインターフェイス。
 * 
 * <p>{@link java.util.Stack}は実装型である上に、不適切な継承を行っている為、
 * Jiemamy Projectでは、こちらのインターフェイスを優先的に使用する。</p>
 * 
 * @param <E> 要素の型
 * @version $Id$
 * @author daisuke
 */
public interface EssentialStack<E> extends Iterable<E> {
	
	/**
	 * スタックを初期化する。
	 */
	void clear();
	
	/**
	 * スタックの下（早くpushした方）からn番目の要素を取得する。スタックから削除は行わない。
	 * 
	 * @param n インデックス
	 * @return 要素
	 * @throws IndexOutOfBoundsException if index is out of range {@code (index &lt; 0 || index &gt;= size())}.
	 */
	E get(int n);
	
	/**
	 * スタックの下（早くpushした方）からn番目に要素を挿入する。
	 * 
	 * @param n インデックス
	 * @param element 挿入する要素
	 * @throws IndexOutOfBoundsException if index is out of range {@code (index &lt; 0 || index &gt; size())}.
	 */
	void insert(int n, E element);
	
	/**
	 * 取り出す要素があるかどうか調べる。
	 * 
	 * @return スタックが空の場合は{@code true}、そうでない場合は{@code false}
	 */
	boolean isEmpty();
	
	/**
	 * 次の{@link #pop()}でスタックから取り出される要素を取得する。スタックから削除は行わない。
	 * 
	 * <p>{@code peek(0)}と等価である。</p>
	 * 
	 * @return 要素
	 * @throws EmptyStackException スタックが空の場合
	 */
	E peek();
	
	/**
	 * n番目に{@link #pop()}でスタックから取り出される要素を取得する。スタックから削除は行わない。
	 * 
	 * @param n インデックス
	 * @return 要素
	 * @throws IndexOutOfBoundsException 取り出す要素がない場合
	 */
	E peek(int n);
	
	/**
	 * スタックから要素を1つ取り出す。取り出した要素はスタックから削除される。
	 * 
	 * @return 取り出した要素
	 * @throws EmptyStackException スタックが空の場合
	 */
	E pop();
	
	/**
	 * スタックに要素を追加する。
	 * 
	 * @param element 追加する要素
	 */
	void push(E element);
	
	/**
	 * スタックの下（早くpushした方）から順に検索し、初めて見つかった要素を削除する。
	 * 
	 * @param element 削除する要素
	 * @return 削除が行われた場合は{@code true}、そうでない場合は{@code false}
	 */
	boolean remove(E element);
	
	/**
	 * スタックの下（早くpushした方）からn番目の要素を削除する。
	 * 
	 * @param n インデックス
	 * @return 削除された要素
	 * @throws IndexOutOfBoundsException if index is out of range {@code (index &lt; 0 || index &gt;= size())}.
	 */
	E remove(int n);
	
	/**
	 * 逆順のスタックを返す。
	 * 
	 * @return 逆順のスタック
	 */
	EssentialStack<E> reverse();
	
	/**
	 * スタックから取り出せる要素の数を取得する。
	 * 
	 * @return 要素の数
	 */
	int size();
}
