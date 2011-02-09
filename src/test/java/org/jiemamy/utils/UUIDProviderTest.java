/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/12/17
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
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.junit.Test;

/**
 * {@link UUIDProvider}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class UUIDProviderTest {
	
	/**
	 * {@link UUIDProvider#valueOfOrRandom(String)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_valueOfOrRandom() throws Exception {
		UUIDProvider p1 = new UUIDProvider();
		UUIDProvider p2 = new UUIDProvider();
		
		// UUID化できるStringを与えた場合 → fromString生成
		UUID uuid1 = p1.valueOfOrRandom("ffffffff-ffff-ffff-ffff-ffffffffffff");
		assertThat(uuid1, is(UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff")));
		
		UUID uuid2 = p2.valueOfOrRandom("ffffffff-ffff-ffff-ffff-ffffffffffff");
		assertThat(uuid2, is(UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff")));
		
		// UUID化できないStringを与えた場合 → randomUUID生成
		UUID uuid1s = p1.valueOfOrRandom("foo");
		assertThat(uuid1s.toString(), is(not("foo")));
		
		UUID uuid2s = p2.valueOfOrRandom("foo");
		assertThat(uuid1s.toString(), is(not("foo")));
		
		// 別のプロバイダで生成すると、IDは異なる
		assertThat(uuid2s, is(not(equalTo(uuid1s))));
		
		// しかし同じnameには同じUUIDが対応し続けること
		assertThat(p1.valueOfOrRandom("foo"), is(equalTo(uuid1s)));
		assertThat(p1.valueOfOrRandom("foo"), is(equalTo(uuid1s)));
		assertThat(p2.valueOfOrRandom("foo"), is(equalTo(uuid2s)));
		assertThat(p2.valueOfOrRandom("foo"), is(equalTo(uuid2s)));
	}
}
