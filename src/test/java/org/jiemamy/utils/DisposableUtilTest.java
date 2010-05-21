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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import org.junit.Test;
import org.mockito.InOrder;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class DisposableUtilTest {
	
	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testname() throws Exception {
		Disposable d1 = mock(Disposable.class);
		doNothing().when(d1).dispose();
		
		Disposable d2 = mock(Disposable.class);
		doNothing().when(d2).dispose();
		
		Disposable d3 = mock(Disposable.class);
		doNothing().when(d3).dispose();
		
		Disposable d4 = mock(Disposable.class);
		doNothing().when(d4).dispose();
		
		DisposableUtil.add(d1);
		DisposableUtil.add(d2);
		DisposableUtil.add(d3);
		
		DisposableUtil.remove(d2);
		
		DisposableUtil.dispose();
		
		InOrder order = inOrder(d4, d3, d2, d1);
		
		order.verify(d4, never()).dispose();
		order.verify(d3, times(1)).dispose();
		order.verify(d2, never()).dispose();
		order.verify(d1, times(1)).dispose();
	}
}
