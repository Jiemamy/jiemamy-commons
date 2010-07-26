/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.Validate;

/**
 * {@link ResourceBundle}用のユーティリティクラス。
 * 
 * @author j5ik2o
 */
public final class ResourceBundleUtil {
	
	/**
	 * {@link Map}に変換する。
	 * 
	 * @param bundle バンドル
	 * @return {@link Map}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static Map<String, String> convertMap(ResourceBundle bundle) {
		Validate.notNull(bundle);
		Map<String, String> ret = new HashMap<String, String>();
		for (Enumeration<String> e = bundle.getKeys(); e.hasMoreElements();) {
			String key = e.nextElement();
			String value = bundle.getString(key);
			ret.put(key, value);
		}
		return ret;
	}
	
	/**
	 * {@link Map}に変換する。
	 * 
	 * <p>{@code locale}に{@code null}を指定した場合は、{@link Locale#getDefault()}を使用する。</p>
	 * 
	 * @param name 名前
	 * @param locale ロケール
	 * @return {@link Map}
	 * @throws IllegalArgumentException 引数{@code name}に{@code null}を与えた場合
	 */
	public static Map<String, String> convertMap(String name, Locale locale) {
		Validate.notNull(name);
		ResourceBundle bundle = getBundle(name, locale);
		return convertMap(bundle);
	}
	
	/**
	 * バンドルを返す。
	 * 
	 * <p>{@code locale}に{@code null}を指定した場合は、{@link Locale#getDefault()}を使用する。</p>
	 * 
	 * @param name 名前
	 * @param locale ロケール
	 * @return {@link ResourceBundle}. 見つからない場合は、{@code null}
	 * @see ResourceBundle#getBundle(String, Locale)
	 * @throws IllegalArgumentException 引数{@code name}に{@code null}を与えた場合
	 */
	public static ResourceBundle getBundle(String name, Locale locale) {
		Validate.notNull(name);
		if (locale == null) {
			locale = Locale.getDefault();
		}
		try {
			return ResourceBundle.getBundle(name, locale);
		} catch (MissingResourceException ignore) {
			return null;
		}
	}
	
	/**
	 * バンドルを返す。
	 * 
	 * <p>{@code locale}に{@code null}を指定した場合は、{@link Locale#getDefault()}を使用する。</p>
	 * 
	 * @param name 名前
	 * @param locale ロケール
	 * @param classLoader クラスローダ
	 * @return {@link ResourceBundle}
	 * @see ResourceBundle#getBundle(String, Locale, ClassLoader)
	 * @throws IllegalArgumentException 引数{@code name}, {@code classLoader}に{@code null}を与えた場合
	 */
	public static ResourceBundle getBundle(String name, Locale locale, ClassLoader classLoader) {
		Validate.notNull(name);
		Validate.notNull(classLoader);
		if (locale == null) {
			locale = Locale.getDefault();
		}
		try {
			return ResourceBundle.getBundle(name, locale, classLoader);
		} catch (MissingResourceException ignore) {
			return null;
		}
	}
	
	private ResourceBundleUtil() {
	}
}
