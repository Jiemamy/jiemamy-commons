/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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

import org.apache.commons.lang.SystemUtils;

/**
 * 恐らくJiemamyの実装バグが原因であることによるエラー。
 * 
 * <ul>
 *   <li><a href="http://d.hatena.ne.jp/daisuke-m/20081201/1228095493">参考1</a></li>
 *   <li><a href="http://d.hatena.ne.jp/daisuke-m/20081202/1228221927">参考2</a></li>
 * </ul>
 * 
 * <p>このエラーが発生した場合、<a href="http://jira.jiemamy.org">Jiemamy Projectの課題管理システム</a>にご報告ください。</p>
 * 
 * @author daisuke
 */
@SuppressWarnings("serial")
public class JiemamyError extends Error {
	
	/**
	 * メッセージがJiemamy内部エラーであることを示す接頭句
	 */
	private static final String GUIDE_PREFIX = "Jiemamy internal error : ";
	
	/**
	 * JiemamyのバグなのでJIRAにチケットを切ってくださいという接尾句
	 */
	private static final String GUIDE_SUFFIX = SystemUtils.LINE_SEPARATOR
			+ " - This is a Jiemamy bug. Please make a ticket on our issue tracker (http://jira.jiemamy.org).";
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param message 例外メッセージ（エラーが発生した状況、考えられる原因など、なるべく詳細に記載すること）
	 */
	public JiemamyError(String message) {
		this(message, null);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param message 例外メッセージ（エラーが発生した状況、考えられる原因など、なるべく詳細に記載すること）
	 * @param cause 起因例外
	 */
	public JiemamyError(String message, Throwable cause) {
		super(GUIDE_PREFIX + message + GUIDE_SUFFIX, cause);
	}
	
}
