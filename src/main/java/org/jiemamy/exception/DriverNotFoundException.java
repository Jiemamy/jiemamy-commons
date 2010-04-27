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
package org.jiemamy.exception;

import clover.org.apache.commons.lang.Validate;

/**
 * JDBCドライバクラスが、JARファイル内から見つからなかった時にスローされる例外。
 * 
 * @since 0.2
 * @author daisuke
 */
@SuppressWarnings("serial")
public class DriverNotFoundException extends ClassNotFoundException {
	
	/** 見つからなかったドライバクラス名 */
	private String className;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param className 見つからなかったドライバクラス名
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.2
	 */
	public DriverNotFoundException(String className) {
		Validate.notNull(className);
		this.className = className;
	}
	
	/**
	 * 見つからなかったドライバクラス名を取得する。
	 * 
	 * @return ドライバクラス名
	 * @since 0.2
	 */
	public String getClassName() {
		return className;
	}
	
}
