/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2010/08/03
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
package org.jiemamy.utils.reflect;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * {@link ModifierUtil}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class ModifierUtilTest {
	
	/**
	 * {@link ModifierUtil#isAbstract(Class)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_isAbstractClass() throws Exception {
		assertThat(ModifierUtil.isAbstract(AbstractClass.class), is(true));
		assertThat(ModifierUtil.isAbstract(Integer.class), is(false));
	}
	
	/**
	 * {@link ModifierUtil#isAbstract(java.lang.reflect.Method)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_isAbstractMethod() throws Exception {
		assertThat(ModifierUtil.isAbstract(AbstractClass.class.getDeclaredMethod("absMethod")), is(true));
		assertThat(ModifierUtil.isAbstract(AbstractClass.class.getDeclaredMethod("concMethod")), is(false));
		assertThat(ModifierUtil.isAbstract(AbstractClass.class.getDeclaredMethod("finalMethod")), is(false));
	}
	
	/**
	 * {@link ModifierUtil#isFinal(java.lang.reflect.Member)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_isFinal() throws Exception {
		assertThat(ModifierUtil.isFinal(AbstractClass.class.getDeclaredField("publicField")), is(false));
		assertThat(ModifierUtil.isFinal(AbstractClass.class.getDeclaredField("protectedField")), is(false));
		assertThat(ModifierUtil.isFinal(AbstractClass.class.getDeclaredField("finalField")), is(true));
		assertThat(ModifierUtil.isFinal(AbstractClass.class.getDeclaredMethod("absMethod")), is(false));
		assertThat(ModifierUtil.isFinal(AbstractClass.class.getDeclaredMethod("concMethod")), is(false));
		assertThat(ModifierUtil.isFinal(AbstractClass.class.getDeclaredMethod("finalMethod")), is(true));
	}
	
	/**
	 * {@link ModifierUtil#isInstanceMember(java.lang.reflect.Member)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_isInstanceMember() throws Exception {
		assertThat(ModifierUtil.isInstanceMember(AbstractClass.class.getDeclaredField("publicField")), is(true));
		assertThat(ModifierUtil.isInstanceMember(AbstractClass.class.getDeclaredField("protectedField")), is(true));
		assertThat(ModifierUtil.isInstanceMember(AbstractClass.class.getDeclaredField("finalField")), is(false));
		assertThat(ModifierUtil.isInstanceMember(AbstractClass.class.getDeclaredMethod("absMethod")), is(true));
		assertThat(ModifierUtil.isInstanceMember(AbstractClass.class.getDeclaredMethod("concMethod")), is(false));
		assertThat(ModifierUtil.isInstanceMember(AbstractClass.class.getDeclaredMethod("finalMethod")), is(true));
	}
	
	/**
	 * {@link ModifierUtil#isPublic(java.lang.reflect.Member)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_isPublic() throws Exception {
		assertThat(ModifierUtil.isPublic(AbstractClass.class.getDeclaredField("publicField")), is(true));
		assertThat(ModifierUtil.isPublic(AbstractClass.class.getDeclaredField("protectedField")), is(false));
		assertThat(ModifierUtil.isPublic(AbstractClass.class.getDeclaredField("finalField")), is(false));
		assertThat(ModifierUtil.isPublic(AbstractClass.class.getDeclaredMethod("absMethod")), is(true));
		assertThat(ModifierUtil.isPublic(AbstractClass.class.getDeclaredMethod("concMethod")), is(false));
		assertThat(ModifierUtil.isPublic(AbstractClass.class.getDeclaredMethod("finalMethod")), is(false));
	}
	
	/**
	 * {@link ModifierUtil#isPublic(java.lang.reflect.Member)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_isPublics() throws Exception {
		
	}
	
	/**
	 * {@link ModifierUtil#isPublicStaticFinal(java.lang.reflect.Member)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_isPublicStaticFinal() throws Exception {
		assertThat(ModifierUtil.isPublicStaticFinal(AbstractClass.class.getDeclaredField("publicField")), is(false));
		assertThat(ModifierUtil.isPublicStaticFinal(AbstractClass.class.getDeclaredField("protectedField")), is(false));
		assertThat(ModifierUtil.isPublicStaticFinal(AbstractClass.class.getDeclaredField("finalField")), is(false));
		assertThat(ModifierUtil.isPublicStaticFinal(AbstractClass.class.getDeclaredField("CONST")), is(true));
		assertThat(ModifierUtil.isPublicStaticFinal(AbstractClass.class.getDeclaredMethod("psfMethod")), is(true));
		assertThat(ModifierUtil.isPublicStaticFinal(AbstractClass.class.getDeclaredMethod("absMethod")), is(false));
		assertThat(ModifierUtil.isPublicStaticFinal(AbstractClass.class.getDeclaredMethod("concMethod")), is(false));
		assertThat(ModifierUtil.isPublicStaticFinal(AbstractClass.class.getDeclaredMethod("finalMethod")), is(false));
	}
	
	/**
	 * {@link ModifierUtil#isTransient(java.lang.reflect.Field)}
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test_isTransient() throws Exception {
		assertThat(ModifierUtil.isTransient(AbstractClass.class.getDeclaredField("publicField")), is(true));
		assertThat(ModifierUtil.isTransient(AbstractClass.class.getDeclaredField("protectedField")), is(false));
		assertThat(ModifierUtil.isTransient(AbstractClass.class.getDeclaredField("finalField")), is(false));
	}
	

	@SuppressWarnings("unused")
	private static abstract class AbstractClass {
		
		public transient String publicField;
		
		protected String protectedField;
		
		static final String finalField = "";
		
		public static final String CONST = "";
		

		public static final void psfMethod() {
			// empty
		}
		
		static void concMethod() {
			// empty
		}
		
		public abstract void absMethod();
		
		protected final void finalMethod() {
			// empty
		}
	}
}
