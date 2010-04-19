/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on Apr 6, 2009
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.jiemamy.exception.JiemamyError;

/**
 * {@link Constructor}用のユーティリティクラス。
 * 
 * @author j5ik2o
 */
public class ConstructorUtil {
	
	/**
	 * 新しいインスタンスを作成します。
	 * 
	 * @param <T> コンストラクタのクラス
	 * @param constructor コンストラクタ
	 * @param args コンストラクタの引数
	 * @return インスタンス
	 * @throws InstantiationException インスタンスの生成に失敗した場合
	 * @throws IllegalAccessException コンストラクタにアクセスできない場合
	 * @throws InvocationTargetException コンストラクタが例外をスローする場合
	 */
	public static <T>T newInstance(Constructor<T> constructor, Object[] args) throws InstantiationException,
			IllegalAccessException, InvocationTargetException {
		return constructor.newInstance(args);
	}
	
	private ConstructorUtil() {
		throw new JiemamyError("不到達ポイント");
	}
}
