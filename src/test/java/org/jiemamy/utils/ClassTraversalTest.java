/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.jar.JarFile;

import org.junit.Test;

import org.jiemamy.utils.ClassTraversal.ClassHandler;

/**
 * {@link ClassTraversal}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class ClassTraversalTest {
	
	/**
	 * {@link ClassTraversal#forEach(JarFile, ClassHandler)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_forEach_jarFile() throws Exception {
		ClassHandler handler = mock(ClassHandler.class);
		ClassTraversal.forEach(new JarFile("src/test/resources/lib/postgresql-8.3-603.jdbc3.jar"), handler);
		
		verify(handler, times(181)).processClass(startsWith("org.postgresql"), anyString());
	}
	
}
