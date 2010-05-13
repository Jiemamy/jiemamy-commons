/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/13
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
package org.jiemamy.utils.collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

/**
 * {@link CollectionsUtil}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class CollectionsUtilTest {
	
	/**
	 * {@link CollectionsUtil#addOrReplace(Set, Object)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_addOrReplace() throws Exception {
		Set<Element> set = new HashSet<Element>();
		Element e1 = Element.of(1);
		Element e2 = Element.of(1);
		Element e3 = Element.of(10);
		
		// Elementの特性確認
		assertThat(e1, is(not(sameInstance(e2))));
		// but
		assertThat(e1, is(equalTo(e2)));
		
		// Setの特性の確認
		set.add(e1);
		set.add(e2);
		assertThat(set.size(), is(1));
		assertThat(set.iterator().next(), is(sameInstance(e1)));
		
		// setにe2をaddOrReplaceすると、e1がe2に置き換わる
		Element removed = CollectionsUtil.addOrReplace(set, e2);
		assertThat(set.size(), is(1));
		assertThat(set.iterator().next(), is(sameInstance(e2)));
		assertThat(removed, is(sameInstance(e1)));
		
		// setにe3をaddOrReplaceすると、単純にaddされる
		Element removed2 = CollectionsUtil.addOrReplace(set, e3);
		assertThat(set.size(), is(2));
		assertThat(set, hasItem(e3));
		assertThat(removed2, is(nullValue()));
	}
}
