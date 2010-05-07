/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2009/01/21
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

/**
 * バリデーションを行うユーティリティクラス。
 * 
 * @author daisuke
 */
public final class ValidateUtil {
	
	/**
	 * インジェクション用のsetterのバリデーションメソッド。
	 * 
	 * <p>既に初期化されているフィールドを上書き初期化しようとした際に、例外を投げる。</p>
	 * 
	 * @param targetField 対象フィールドの値
	 * @throws IllegalStateException フィールドが初期化済みであった場合
	 */
	public static void injectionSetter(Object targetField) {
		if (targetField != null) {
			throw new IllegalStateException("duplicate initialize");
		}
	}
	
	private ValidateUtil() {
	}
}
