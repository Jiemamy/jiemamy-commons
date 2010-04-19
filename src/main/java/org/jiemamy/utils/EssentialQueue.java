/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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
package org.jiemamy.utils;

import java.util.NoSuchElementException;

/**
 * 本質的なキューのインターフェイス。
 * 
 * <p>{@link java.util.Queue}は不適切な継承を行っている為、
 * Jiemamy Projectでは、こちらのインターフェイスを優先的に使用する。</p>
 * 
 * @param <E> 要素の型
 * @author daisuke
 */
public interface EssentialQueue<E> extends Iterable<E> {
	
	/**
	 * キューを初期化する。
	 */
	void clear();
	
	/**
	 * キューから要素を1つ取り出す。
	 * 
	 * @return 取り出した要素
	 * @throws NoSuchElementException 取り出す要素が無い場合
	 */
	E dequeue();
	
	/**
	 * キューに要素を追加する。
	 * 
	 * @param element 追加する要素
	 */
	void enqueue(E element);
	
	/**
	 * 与えられたキューの内容を、このキューに全て追加する。
	 * 
	 * @param queue 読み込み元のキュー
	 */
	void enqueue(EssentialQueue<E> queue);
	
	/**
	 * 取り出す要素があるかどうか調べる。
	 * 
	 * @return キューが空の場合は{@code true}、そうでない場合は{@code false}
	 */
	boolean isEmpty();
	
	/**
	 * 次にキューから取り出される要素を取得する。キューから削除は行わない。
	 * 
	 * @return 次にキューから取り出される要素
	 * @throws NoSuchElementException 取り出す要素が無い場合
	 */
	E peek();
	
	/**
	 * キューから取り出せる要素の数を取得する。
	 * 
	 * @return 要素の数
	 */
	int size();
}
