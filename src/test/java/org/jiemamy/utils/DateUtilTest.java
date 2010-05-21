/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/21
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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class DateUtilTest {
	
	/**
	 * {@link DateUtil#toSqlDate(java.util.Date)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	@SuppressWarnings("deprecation")
	public void test_toSqlDate() throws Exception {
		java.util.Date date = new java.util.Date();
		java.sql.Date sqlDate = DateUtil.toSqlDate(date);
		assertThat(sqlDate.getYear(), is(date.getYear()));
		assertThat(sqlDate.getMonth(), is(date.getMonth()));
		assertThat(sqlDate.getDay(), is(date.getDay()));
		
	}
	
	/**
	 * {@link DateUtil#toSqlDate(java.util.Date)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	@SuppressWarnings("deprecation")
	public void testname() throws Exception {
		java.util.Date date = new java.util.Date();
		java.sql.Time sqlTime = DateUtil.toSqlTime(date);
		assertThat(sqlTime.getHours(), is(date.getHours()));
		assertThat(sqlTime.getMinutes(), is(date.getMinutes()));
		assertThat(sqlTime.getSeconds(), is(date.getSeconds()));
	}
}
