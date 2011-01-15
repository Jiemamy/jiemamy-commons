/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2009/03/04
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

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.Validate;

/**
 * {@link EssentialStack}のユーティリティクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class EssentialStacks {
	
	/**
	 * 左右の要素を下から調べ、初めて異なる要素が現れる所までの部分スタックを返す。
	 * 
	 * <p>ABCDEFG と ABCXYZ の場合は ABC を返し、ABCDEFG と ABXDXXG の場合は AB を返す。</p>
	 * 
	 * @param <E> 要素の型
	 * @param left 左辺スタック
	 * @param right 右辺スタック
	 * @return 部分スタック
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static <E>EssentialStack<E> intersection(EssentialStack<? extends E> left, EssentialStack<? extends E> right) {
		Validate.notNull(left);
		Validate.notNull(right);
		EssentialStack<E> result = new ArrayEssentialStack<E>(/*Math.min(left.size(), right.size())*/);
		
		int i = 0;
		for (E leftElement : left) {
			if (i >= right.size()) {
				break;
			}
			E rightElement = right.get(i);
			if (ObjectUtils.equals(rightElement, leftElement)) {
				result.push(leftElement);
			} else {
				// 違いが生じた時点で終了
				break;
			}
			i++;
		}
		
		return result;
	}
	
	/**
	 * 左の要素から、右の要素を取り除いた部分スタックを返す。
	 * 
	 * @param <E> 要素の型
	 * @param left 左辺スタック
	 * @param right 右辺スタック
	 * @return 部分スタック
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static <E>EssentialStack<E> minus(EssentialStack<? extends E> left, EssentialStack<? extends E> right) {
		Validate.notNull(left);
		Validate.notNull(right);
		EssentialStack<E> result = new ArrayEssentialStack<E>(left);
		for (E er : right) {
			for (E el : left) {
				if (el == er) {
					result.remove(el);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * {@link ArrayEssentialStack}の新しいインスタンスを作成する。
	 * 
	 * @param <E> {@link ArrayEssentialStack}の要素型
	 * @return {@link ArrayEssentialStack}の新しいインスタンス
	 * @see ArrayEssentialStack#ArrayEssentialStack()
	 */
	public static <E>ArrayEssentialStack<E> newArrayEssentialStack() {
		return new ArrayEssentialStack<E>();
	}
	
	private EssentialStacks() {
	}
}
