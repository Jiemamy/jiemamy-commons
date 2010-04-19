/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2008/06/09
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

import java.util.Locale;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Stringユーティリティ。
 * 
 * @author daisuke
 * @author wencheng
 */
public final class JmStringUtil {
	
	/**
	 * 大文字小文字を無視した条件下で、配列中に指定した文字列を含むかどうかを調べる。
	 * 
	 * @param array 調査対象配列
	 * @param stringToFind 探す文字列
	 * @return 含む場合は{@code true}、そうでない場合は{@code false}
	 */
	public static boolean containsIgnoreCase(String[] array, String stringToFind) {
		return indexOfIgnoreCase(array, stringToFind) != ArrayUtils.INDEX_NOT_FOUND;
	}
	
	/**
	 * 大文字小文字を無視した条件下で、配列中に指定した文字列のインデックスを調べる。
	 * 
	 * @param array 調査対象配列
	 * @param stringToFind 探す文字列
	 * @return インデックス番号
	 */
	public static int indexOfIgnoreCase(String[] array, String stringToFind) {
		return indexOfIgnoreCase(array, stringToFind, 0);
	}
	
	/**
	 * 大文字小文字を無視した条件下で、配列中に指定した文字列のインデックスを調べる。
	 * 
	 * @param array 調査対象配列
	 * @param stringToFind 探す文字列
	 * @param startIndex 調査開始インデックス
	 * @return インデックス番号
	 */
	public static int indexOfIgnoreCase(String[] array, String stringToFind, int startIndex) {
		if (array == null) {
			return ArrayUtils.INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		if (stringToFind == null) {
			for (int i = startIndex; i < array.length; i++) {
				if (array[i] == null) {
					return i;
				}
			}
		} else {
			for (int i = startIndex; i < array.length; i++) {
				if (stringToFind.equalsIgnoreCase(array[i])) {
					return i;
				}
			}
		}
		return ArrayUtils.INDEX_NOT_FOUND;
	}
	
	/**
	 * 文字列の1文字目を大文字にする。
	 * 
	 * @param str 入力文字列
	 * @return 出力文字列
	 */
	public static String toCapital(final String str) {
		final char[] ch = str.toCharArray();
		ch[0] = Character.toUpperCase(ch[0]);
		return new String(ch);
	}
	
	/**
	 * SQL名（ex. AGILE_DATABASE）からJavaクラス名（ex. AgileDatabase）を生成する。
	 * 
	 * @param str SQL名
	 * @return Javaクラス名
	 */
	public static String toJavaClassName(final String str) {
		return toCapital(toJavaName(str));
	}
	
	/**
	 * SQL名（ex. AGILE_DATABASE）からJavaクラス名（ex. AgileDatabase）を生成する。 prifixが存在した場合、取り除く。
	 * 
	 * @param str SQL名
	 * @param prefix 接頭辞
	 * @return Javaクラス名
	 */
	public static String toJavaClassName(final String str, final String prefix) {
		return toCapital(toJavaName(str, prefix));
	}
	
	/**
	 * SQL名（ex. AGILE_DATABASE）からJava名（ex. agileDatabase）を生成する。
	 * 
	 * @param str SQL名
	 * @return Java名
	 */
	public static String toJavaName(final String str) {
		return toJavaName(str, null);
	}
	
	/**
	 * SQL名（ex. AGILE_DATABASE）からJava名（ex. agileDatabase）を生成する。 prifixが存在した場合、取り除く。
	 * 
	 * @param str SQL名
	 * @param prefix 接頭辞
	 * @return Java名
	 */
	public static String toJavaName(final String str, final String prefix) {
		if (str == null) {
			return str;
		}
		String low;
		if (prefix != null && str.startsWith(prefix)) {
			low = str.replaceFirst(prefix, "").toLowerCase(Locale.getDefault());
		} else {
			low = str.toLowerCase(Locale.getDefault());
		}
		final StringBuilder sb = new StringBuilder(low);
		for (int i = 0; i < sb.length(); i++) {
			final char c = sb.charAt(i);
			if (c == '_') {
				sb.deleteCharAt(i);
				sb.setCharAt(i, Character.toUpperCase(sb.charAt(i)));
			}
		}
		return sb.toString();
	}
	
	/**
	 * Java名（ex. AgileDatabase, agileDatabase）からSQL名（ex. AGILE_DATABASE）を生成する。
	 * 
	 * @param str Java名
	 * @return SQL名
	 */
	public static String toSQLName(final String str) {
		return toSQLName(str, StringUtils.EMPTY);
	}
	
	/**
	 * Java名（ex. AgileDatabase, agileDatabase）からSQL名（ex. AGILE_DATABASE）を生成する。 prifixを付加する。
	 * 
	 * @param str Java名
	 * @param prefix 接頭辞
	 * @return SQL名
	 */
	public static String toSQLName(final String str, final String prefix) {
		if (str == null || str.length() == 0) {
			return str;
		}
		final StringBuilder sb = new StringBuilder(str);
		for (int i = 0; i < sb.length(); i++) {
			final char c = sb.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.insert(i++, "_");
			} else {
				sb.setCharAt(i, Character.toUpperCase(c));
			}
		}
		if (prefix != null && prefix.length() > 0) {
			sb.insert(0, prefix.toUpperCase(Locale.getDefault()));
		}
		return sb.toString();
	}
	
	/**
	 * 文字列の1文字目を小文字にする。
	 * 
	 * @param str 入力文字列
	 * @return 出力文字列
	 */
	public static String toUnCapital(String str) {
		final char[] ch = str.toCharArray();
		ch[0] = Character.toLowerCase(ch[0]);
		return new String(ch);
	}
	
	private JmStringUtil() {
	}
}
