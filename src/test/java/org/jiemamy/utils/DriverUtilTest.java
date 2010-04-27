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
package org.jiemamy.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.net.URL;
import java.sql.Driver;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link DriverUtil}のテストクラス。
 * 
 * @author daisuke
 */
public class DriverUtilTest {
	
	private URL[] urls;
	

	/**
	 * {@link #urls}を生成する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		urls = new URL[] {
			DriverUtilTest.class.getResource("/postgresql-8.3-603.jdbc3.jar"),
		};
	}
	
	/**
	 * {@link #urls}を破棄する。
	 */
	@After
	public void tearDown() {
		urls = null;
	}
	
	/**
	 * PostgreSQL用JARに対してgetDriverInstanceをすると_Driverのインスタンスが取得できる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_PostgreSQL用JARに対してgetDriverInstanceをすると_Driverのインスタンスが取得できる() throws Exception {
		Driver driver = DriverUtil.getDriverInstance(urls, "org.postgresql.Driver");
		
		assertThat(driver, is(notNullValue()));
		assertThat(driver.getClass().getName(), is("org.postgresql.Driver"));
	}
	
	/**
	 * PostgreSQL用JARに対してgetDriverClassesをすると_Driverクラスが1つ取得できる。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_PostgreSQL用JARに対してgetDriverClassesをすると_Driverクラスが1つ取得できる() throws Exception {
		List<Class<? extends Driver>> driverClasses = DriverUtil.getDriverClasses(urls);
		
		assertThat(driverClasses.size(), is(1));
		assertThat(driverClasses.get(0).getName(), is("org.postgresql.Driver"));
	}
}
