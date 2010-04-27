/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/04/27
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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.Test;

/**
 * {@link AnnotationUtil}のテストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class AnnotationUtilTest {
	
	@SampleAnnotation1("test1")
	static interface A {
		
	}
	
	static interface B extends A {
		
	}
	
	@SampleAnnotation2("test2")
	static class C implements A {
		
	}
	
	static class D implements A {
		
	}
	
	static class E implements B {
		
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@interface SampleAnnotation1 {
		
		String value();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@interface SampleAnnotation2 {
		
		String value();
	}
	

	/**
	 * {@link AnnotationUtil#getTypeAnnotation(Object, Class)}のテスト。
	 * 
	 * @throws Exception 例外が発生した場合
	 * @since 0.3
	 */
	@Test
	public void tes01_getTypeAnnotation() throws Exception {
		SampleAnnotation1 c1 = AnnotationUtil.getTypeAnnotation(new C(), SampleAnnotation1.class);
		assertThat(c1.value(), is("test1"));
		SampleAnnotation2 c2 = AnnotationUtil.getTypeAnnotation(new C(), SampleAnnotation2.class);
		assertThat(c2.value(), is("test2"));
		SampleAnnotation1 d1 = AnnotationUtil.getTypeAnnotation(new D(), SampleAnnotation1.class);
		assertThat(d1.value(), is("test1"));
		SampleAnnotation2 d2 = AnnotationUtil.getTypeAnnotation(new D(), SampleAnnotation2.class);
		assertThat(d2, is(nullValue()));
	}
}
