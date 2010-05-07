/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2008/08/20
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
package org.jiemamy.utils.swap;

/**
 * スワップ例外クラス。
 * 
 * <p>
 * オブジェクトのスワップ処理における想定外の状態が検知された際にスローされる。<br>
 * 基本的には、スワップ処理のためのシリアライズ、またはデシリアライズの際にスローされる{@link java.io.IOException }を原因とするラッパー例外である。
 * </p>
 * 
 * @author Keisuke.K
 */
@SuppressWarnings("serial")
public class SwapException extends Exception {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param message 例外メッセージ
	 */
	public SwapException(String message) {
		super(message);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param message 例外メッセージ
	 * @param cause 起因例外
	 */
	public SwapException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param cause 起因例外
	 */
	public SwapException(Throwable cause) {
		super(cause);
	}
	
}
