/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2008/07/26
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

import java.sql.ResultSet;

import org.jiemamy.utils.ForEachUtil;

/**
 * {@link ResultSet}に対するビジターの抽象クラス。
 * 
 * @param <R> 戻り値の型
 * @param <X> visitメソッドが投げる可能性のある例外
 * @author daisuke
 */
public abstract class AbstractResultSetVisitor<R, X extends Exception> implements
		ForEachUtil.ResultSetVisitor<ResultSet, R, X> {
	
	/** ループが終了した後、forEachが返すべき戻り値 */
	protected R finalResult;
	

	/**
	 * インスタンスを生成する。
	 */
	public AbstractResultSetVisitor() {
		init();
	}
	
	public R getFinalResult() {
		return finalResult;
	}
	
	@Override
	public String toString() {
		return finalResult.toString();
	}
	
	/**
	 * 最終戻り値の初期化を行う。
	 */
	protected void init() {
		finalResult = null;
	}
}
