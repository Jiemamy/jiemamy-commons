/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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

/**
 * {@link ResourceBundle}用のユーティリティクラス。
 * 
 * @author j5ik2o
 * 
 */
public class ResourceBundleUtil {
	
	/**
	 * {@link Map}に変換します。
	 * 
	 * @param bundle バンドル
	 * @return {@link Map}
	 */
	public static final Map<String, String> convertMap(ResourceBundle bundle) {
		Map<String, String> ret = new HashMap<String, String>();
		for (Enumeration<String> e = bundle.getKeys(); e.hasMoreElements();) {
			String key = e.nextElement();
			String value = bundle.getString(key);
			ret.put(key, value);
		}
		return ret;
	}
	
	/**
	 * {@link Map}に変換します。
	 * 
	 * @param name 名前
	 * @param locale ロケール
	 * @return {@link Map}
	 */
	public static final Map<String, String> convertMap(String name, Locale locale) {
		ResourceBundle bundle = getBundle(name, locale);
		return convertMap(bundle);
	}
	
	/**
	 * バンドルを返します。 見つからない場合は、<code>null</code>を返します。
	 * 
	 * @param name 名前
	 * @param locale ロケール
	 * @return {@link ResourceBundle}
	 * @see ResourceBundle#getBundle(String, Locale)
	 */
	public static final ResourceBundle getBundle(String name, Locale locale) {
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
	 * バンドルを返します。 見つからない場合は、<code>null</code>を返します。
	 * 
	 * @param name 名前
	 * @param locale ロケール
	 * @param classLoader クラスローダ
	 * @return {@link ResourceBundle}
	 * @see ResourceBundle#getBundle(String, Locale, ClassLoader)
	 */
	public static final ResourceBundle getBundle(String name, Locale locale, ClassLoader classLoader) {
		if (locale == null) {
			locale = Locale.getDefault();
		}
		try {
			return ResourceBundle.getBundle(name, locale, classLoader);
		} catch (MissingResourceException ignore) {
			return null;
		}
	}
	
	/**
	 * インスタンスを構築します。
	 */
	private ResourceBundleUtil() {
	}
}
