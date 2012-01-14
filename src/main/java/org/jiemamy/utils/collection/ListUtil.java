/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/02/10
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

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;

/**
 * {@link List}を扱うユーティリティクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class ListUtil {
	
	/**
	 * リストの指定したindexの要素を、次の要素と入れ替える。
	 * 
	 * @param list 対象リスト
	 * @param index インデックス
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws IllegalArgumentException indexが負の数の場合
	 * @throws IndexOutOfBoundsException if either index or index + 1 is out of range ((index < 0 || index + 1 >= list.size())).
	 */
	public static void moveDown(List<?> list, int index) {
		Validate.notNull(list);
		Validate.isTrue(index >= 0);
		Collections.swap(list, index, index + 1);
	}
	
	/**
	 * リストの指定したindexの要素を、前の要素と入れ替える。
	 * 
	 * @param list 対象リスト
	 * @param index インデックス
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws IndexOutOfBoundsException if either index or index - 1 is out of range ((index - 1 < 0 || index >= list.size())).
	 */
	public static void moveUp(List<?> list, int index) {
		Validate.notNull(list);
		Collections.swap(list, index, index - 1);
	}
	
	/**
	 * リストの順序を反転させる。
	 * 
	 * @param list 対象リスト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static void reverse(List<?> list) {
		Validate.notNull(list);
		int center = list.size() / 2;
		int end = list.size() - 1;
		
		for (int i = 0; i < center; i++) {
			Collections.swap(list, i, end - i);
		}
	}
	
	private ListUtil() {
	}
	
}
