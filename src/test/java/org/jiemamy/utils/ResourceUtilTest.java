/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2010/05/24
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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;

/**
 * {@link ResourceUtil}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class ResourceUtilTest {
	
	/**
	 * {@link ResourceUtil#getContextClassLoader()}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getContextClassLoader() throws Exception {
		ClassLoader cl1 = Thread.currentThread().getContextClassLoader();
		ClassLoader cl2 = mock(ClassLoader.class);
		assertThat(ResourceUtil.getContextClassLoader(), is(notNullValue()));
		assertThat(ResourceUtil.getContextClassLoader(), is(cl1));
		assertThat(ResourceUtil.getContextClassLoader(), is(not(cl2)));
		
		Thread.currentThread().setContextClassLoader(cl2);
		
		assertThat(ResourceUtil.getContextClassLoader(), is(notNullValue()));
		assertThat(ResourceUtil.getContextClassLoader(), is(cl2));
		assertThat(ResourceUtil.getContextClassLoader(), is(not(cl1)));
		
		Thread.currentThread().setContextClassLoader(cl1);
	}
	
	/**
	 * {@link ResourceUtil#getResourcePath(Class)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_getResourcePath() throws Exception {
		assertThat(ResourceUtil.getResourcePath(String.class), is("java/lang/String.class"));
		assertThat(ResourceUtil.getResourcePath(ResourceUtilTest.class), is("org/jiemamy/utils/ResourceUtilTest.class"));
	}
}
