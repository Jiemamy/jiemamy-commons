/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/04/28
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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * JiemamyErrorクラスのテストクラス、インスタンスを作って内容がきちんと
 * 設定されているか確認する
 * 
 * @author tonouchi
 */
public class JiemamyErrorTest {
	
	/**
	 * コンストラクタのテスト受け取ったメッセージがきちんと想定した文言に
	 * なっているか確認
	 */
	@Test
	public void testConstructors() {
		String expectedMessage =
				"Jiemamy internal error : hogehoge"
						+ System.getProperty("line.separator")
						+ " - This is a Jiemamy bug. Please make a ticket on our issue tracker (http://jira.jiemamy.org).";
		
		JiemamyError error = new JiemamyError("hogehoge");
		
		assertThat(error.getMessage(), is(expectedMessage));
		assertThat(error.getCause(), is(nullValue()));
	}
}
