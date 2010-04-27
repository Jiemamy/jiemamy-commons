/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2008/12/01
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
package org.jiemamy;

/**
 * Jiemamyの実装バグが原因であることによるエラー。
 * 
 * <ul>
 *   <li>http://d.hatena.ne.jp/daisuke-m/20081201/1228095493</li>
 *   <li>http://d.hatena.ne.jp/daisuke-m/20081202/1228221927</li>
 * </ul>
 * 
 * @since 0.2
 * @author daisuke
 */
@SuppressWarnings("serial")
public class JiemamyError extends Error {
	
	// TODO tonocchi
	private static final String GUIDE = "Please report a bug to http://jira.jiemamy.org/ :";
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param message 例外メッセージ（エラーが発生した状況、考えられる原因など、なるべく詳細に記載すること）
	 * @since 0.2
	 */
	public JiemamyError(String message) {
		super(GUIDE + message);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param message 例外メッセージ（エラーが発生した状況、考えられる原因など、なるべく詳細に記載すること）
	 * @param cause 起因例外
	 * @since 0.2
	 */
	public JiemamyError(String message, Throwable cause) {
		super(GUIDE + message, cause);
	}
	
}
