/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import java.beans.Introspector;
import java.util.LinkedList;

/**
 * リソースを破棄するためのユーティリティクラス。
 * 
 * @author j5ik2o
 */
public class DisposableUtil {
	
	/** 登録済みの{@link Disposable} */
	private static final LinkedList<Disposable> DISPOSABLES = new LinkedList<Disposable>();
	

	/**
	 * 破棄可能なリソースを登録する。
	 * 
	 * @param disposable 破棄可能なリソース
	 */
	public static synchronized void add(Disposable disposable) {
		DISPOSABLES.add(disposable);
	}
	
	/**
	 * 登録済みのリソースを全て破棄する。
	 */
	public static synchronized void dispose() {
		while (DISPOSABLES.isEmpty() == false) {
			Disposable disposable = DISPOSABLES.removeLast();
			try {
				disposable.dispose();
			} catch (Throwable t) {
				t.printStackTrace(); // must not use Logger.
			}
		}
		DISPOSABLES.clear();
		Introspector.flushCaches();
	}
	
	/**
	 * 破棄可能なリソースの登録を解除する。
	 * 
	 * @param disposable 破棄可能なリソース
	 */
	public static synchronized void remove(Disposable disposable) {
		DISPOSABLES.remove(disposable);
	}
	
	private DisposableUtil() {
	}
}
