/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2008/12/17
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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.UUID;

import org.junit.Test;

/**
 * {@link UUIDUtil}のテストクラス。
 * 
 * @author daisuke
 */
public class UUIDUtilTest {
	
	/**
	 * Test method for {@link org.jiemamy.utils.UUIDUtil#valueOfOrRandom(java.lang.String)}.
	 */
	@Test
	public void testValueOfOrRandom() {
		// UUID化できるStringを与えた場合 → fromString生成
		UUID uuid1 = UUIDUtil.valueOfOrRandom("ffffffff-ffff-ffff-ffff-ffffffffffff");
		assertThat(uuid1, is(UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff")));
		
		// UUID化できないStringを与えた場合 → randomUUID生成
		UUID uuid2 = UUIDUtil.valueOfOrRandom("foo");
		assertThat(uuid2.toString(), is(not("foo")));
		
		// 同じname（nullを含む）には同じUUIDが対応し続けること
		for (String name : Arrays.asList("foo", "bar", "baz", null)) {
			assertThat(UUIDUtil.valueOfOrRandom(name), is(notNullValue()));
			assertThat(UUIDUtil.valueOfOrRandom(name), is(UUIDUtil.valueOfOrRandom(name)));
		}
	}
	
}
