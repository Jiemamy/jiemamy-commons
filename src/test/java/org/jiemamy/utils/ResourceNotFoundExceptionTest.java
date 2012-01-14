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
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * {@link ResourceNotFoundException}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class ResourceNotFoundExceptionTest {
	
	/**
	 * {@link ResourceNotFoundException#getPath()}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test() throws Exception {
		ResourceNotFoundException e = new ResourceNotFoundException("foo");
		assertThat(e.getMessage(), is("foo"));
		assertThat(e.getPath(), is("foo"));
	}
	
}
