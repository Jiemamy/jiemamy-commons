/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2008/08/23
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
package org.jiemamy.utils.swap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.util.HashMap;

import org.junit.Test;

/**
 * {@link Swapper}のテストクラス。
 * 
 * @author Keisuke.K
 */
public class SwapperTest {
	
	/**
	 * スワップ処理を行い、その後get()して双方が同一のオブジェクトかどうかをチェックする。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_基本的なスワップ処理() throws Exception {
		HashMap<String, String> testMap = new HashMap<String, String>();
		testMap.put("foo", "bar");
		testMap.put("fizz", "buzz");
		
		// スワップ
		SwapObject<HashMap<String, String>> swapObj = new SwapObject<HashMap<String, String>>(testMap);
		
		// 取得
		HashMap<String, String> getMap = swapObj.get();
		
		// チェック
		assertEquals(testMap, getMap);
	}
	
	/**
	 * 2個のオブジェクトに対してスワップ処理を行い、get()した結果が正しくなるかどうかをチェックする。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_2個のオブジェクトをスワップ処理() throws Exception {
		String str1 = "foobar";
		String str2 = "fizzbuzz";
		
		// スワップ
		SwapObject<String> swapObj1 = new SwapObject<String>(str1);
		SwapObject<String> swapObj2 = new SwapObject<String>(str2);
		
		// 取得
		String getStr1 = swapObj1.get();
		String getStr2 = swapObj2.get();
		
		// チェック
		assertEquals(str1, getStr1);
		assertEquals(str2, getStr2);
	}
	
	/**
	 * 参照が切れ、GC後に不要となったスワップ情報が切り捨てられ、スワップファイルの長さが短くなるかどうかをチェックする。<br>
	 * [SER-5] クリーニング直後のスワップで NPE
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_参照が切れた場合のスワップファイルの長さ調整() throws Exception {
		String str1 = "foobar";
		String str2 = "fizzbuzz";
		
		// スワップ
		@SuppressWarnings("unused")
		SwapObject<String> swapObj1 = new SwapObject<String>(str1);
		@SuppressWarnings("unused")
		SwapObject<String> swapObj2 = new SwapObject<String>(str2);
		
		long currentSize = Swapper.INSTANCE.channel.size();
		
		// スワップファイルの最後に記録されているであろうswapObj2の参照を切ってGC。
		swapObj2 = null;
		System.gc();
		
		// ReferenceQueue に enqueue されるための待ち
		// see. http://d.hatena.ne.jp/Ewigkeit/20080823/1219463052
		Thread.sleep(1000L);
		
		long newSize = Swapper.INSTANCE.channel.size();
		
		// チェック
		assertTrue(currentSize > newSize);
		
		// [SER-5] 新しくオブジェクトをスワップさせ、NPE が発生しないことを確認。
		try {
			@SuppressWarnings("unused")
			SwapObject<String> swapObj3 = new SwapObject<String>("John Doe");
		} catch (NullPointerException e) {
			fail("[SER-5] throws NPE");
		}
	}
	
	/**
	 * 1つのSwapObjectに対して複数回get()を行い、取得できたRealObject同士は参照が同一であるべき。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test04_複数回取得したRealObjectの参照同一性() throws Exception {
		HashMap<String, String> testMap = new HashMap<String, String>();
		testMap.put("foo", "bar");
		testMap.put("fizz", "buzz");
		
		// スワップ
		SwapObject<HashMap<String, String>> swapObj = new SwapObject<HashMap<String, String>>(testMap);
		
		// 取得
		HashMap<String, String> map1 = swapObj.get();
		HashMap<String, String> map2 = swapObj.get();
		
		// チェック
		assertSame(map1, map2);
	}
	
	/**
	 * スワップ後に取得したRealObjectに対して更新を行い、再度スワップ処理を行う流れのテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test05_スワップ後取得したRealObjectの更新をして再度スワップ処理() throws Exception {
		HashMap<String, String> testMap = new HashMap<String, String>();
		testMap.put("foo", "bar");
		testMap.put("fizz", "buzz");
		
		// スワップ
		SwapObject<HashMap<String, String>> swapObj = new SwapObject<HashMap<String, String>>(testMap);
		
		// 内容の変更
		testMap.put("John", "Doe");
		swapObj.update(testMap);
		
		// 取得
		HashMap<String, String> getMap = swapObj.get();
		
		assertEquals(testMap, getMap);
	}
	
}
