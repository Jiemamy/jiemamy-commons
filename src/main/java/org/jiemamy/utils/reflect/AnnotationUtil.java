/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2009/02/04
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

import java.lang.annotation.Annotation;

import org.apache.commons.lang.Validate;

/**
 * アノテーションを処理するユーティリティクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class AnnotationUtil {
	
	/**
	 * target自身の型、及びそのインターフェイスのいずれかが持つアノテーションを取得する。
	 * 
	 * <p>targetの基底クラス及び、targetのインターフェイスの基底インターフェイスは対象外。</p>
	 * 
	 * @param <T> アノテーションの型
	 * @param target 調査対象オブジェクト
	 * @param annotationClass アノテーションの型
	 * @return 取得したアノテーション. もし見つからなかった場合は{@code null}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static <T extends Annotation>T getTypeAnnotation(Object target, Class<T> annotationClass) {
		Validate.notNull(target);
		Validate.notNull(annotationClass);
		
		Annotation classAnnotation = target.getClass().getAnnotation(annotationClass);
		if (classAnnotation != null) {
			return annotationClass.cast(classAnnotation);
		}
		for (Class<?> interf : target.getClass().getInterfaces()) {
			Annotation interfaceAnnotation = interf.getAnnotation(annotationClass);
			if (interfaceAnnotation != null) {
				return annotationClass.cast(interfaceAnnotation);
			}
		}
		return null;
	}
	
	private AnnotationUtil() {
	}
}
