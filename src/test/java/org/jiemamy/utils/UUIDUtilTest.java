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
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

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
	public void test01_ValueOfOrRandom() {
		// UUID化できるStringを与えた場合 → fromString生成
		UUID uuid1 = UUIDUtil.valueOfOrRandom("ffffffff-ffff-ffff-ffff-ffffffffffff");
		assertThat(uuid1, is(UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff")));
		
		// UUID化できないStringを与えた場合 → randomUUID生成
		UUID uuid2 = UUIDUtil.valueOfOrRandom("foo");
		assertThat(uuid2.toString(), is(not("foo")));
	}
	
	/**
	 * clearすると、キャッシュが開放され、別のIDが割り当てられること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_clear() throws Exception {
		UUID foo = UUIDUtil.valueOfOrRandom("foo");
		UUID bar = UUIDUtil.valueOfOrRandom("bar");
		UUID baz = UUIDUtil.valueOfOrRandom("baz");
		UUID nul = UUIDUtil.valueOfOrRandom(null);
		
		// 同じname（nullを含む）には同じUUIDが対応し続けること
		assertThat(UUIDUtil.valueOfOrRandom("foo"), is(equalTo(foo)));
		assertThat(UUIDUtil.valueOfOrRandom("bar"), is(equalTo(bar)));
		assertThat(UUIDUtil.valueOfOrRandom("baz"), is(equalTo(baz)));
		assertThat(UUIDUtil.valueOfOrRandom(null), is(equalTo(nul)));
		
		UUIDUtil.clear();
		
		// clearすると、キャッシュが開放され、別のIDが割り当てられること
		assertThat(UUIDUtil.valueOfOrRandom("foo"), is(not(equalTo(foo))));
		assertThat(UUIDUtil.valueOfOrRandom("bar"), is(not(equalTo(bar))));
		assertThat(UUIDUtil.valueOfOrRandom("baz"), is(not(equalTo(baz))));
		assertThat(UUIDUtil.valueOfOrRandom(null), is(not(equalTo(nul))));
	}
	
	/**
	 * {@link UUIDUtil#toShortString(UUID)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_toShortString() throws Exception {
		assertThat(UUIDUtil.toShortString(UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff")), is("ffffffff"));
		assertThat(UUIDUtil.toShortString(UUID.fromString("fffffff0-ffff-ffff-ffff-ffffffffffff")), is("fffffff0"));
		assertThat(UUIDUtil.toShortString(null), is("null"));
	}
}
