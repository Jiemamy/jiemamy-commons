/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2008/06/24
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
package org.jiemamy.utils.visitor;

import org.jiemamy.utils.sql.TypeSafeResultSet;

/**
 * {@link TypeSafeResultSet}に対するビジターの抽象クラス。
 * 
 * @author daisuke
 * @param <T> Collectionが保持する型
 * @param <R> forEachが返すべき戻り値の型
 * @param <X> スローする可能性のある例外
 */
public abstract class AbstractTypeSafeResultSetVisitor<T, R, X extends Exception> implements
		ForEachUtil.TypeSafeResultSetVisitor<T, R, X> {
	
	/** ループが終了した後、forEachが返すべき戻り値 */
	protected R finalResult;
	

	/**
	 * インスタンスを生成する。
	 */
	public AbstractTypeSafeResultSetVisitor() {
		init();
	}
	
	public R getFinalResult() {
		return finalResult;
	}
	
	@Override
	public String toString() {
		return finalResult == null ? "null" : finalResult.toString();
	}
	
	/**
	 * 最終戻り値の初期化を行う。
	 */
	protected void init() {
		finalResult = null;
	}
	
}
