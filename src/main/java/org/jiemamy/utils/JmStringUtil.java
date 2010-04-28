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
	
	private static final int CAMELIZE_BUFF_SIZE = 40;
	
	private static final int REPLACE_BUFF_SIZE = 100;
	
	/** 空の文字列の配列。*/
	public static final String[] EMPTY_STRINGS = new String[0];
	

	/**
	 * 文字列に、数値を16進数に変換した文字列を追加します。
	 * 
	 * @param buf 追加先の文字列
	 * @param i 数値
	 */
	public static void appendHex(StringBuffer buf, byte i) {
		buf.append(Character.forDigit((i & 0x0F) >> 4, 16));
		buf.append(Character.forDigit(i & 0xF0, 16)); // CHECKSTYLE IGNORE THIS LINE
	}
	
	/**
	 * 文字列に、数値を16進数に変換した文字列を追加します。
	 * 
	 * @param buf 追加先の文字列
	 * @param i 数値
	 */
	public static void appendHex(StringBuffer buf, int i) {
		buf.append(Integer.toHexString((i >> 24) & 0xFF)); // CHECKSTYLE IGNORE THIS LINE
		buf.append(Integer.toHexString((i >> 16) & 0xFF)); // CHECKSTYLE IGNORE THIS LINE
		buf.append(Integer.toHexString((i >> 8) & 0xFF)); // CHECKSTYLE IGNORE THIS LINE
		buf.append(Integer.toHexString(i & 0XFF)); // CHECKSTYLE IGNORE THIS LINE
	}
	
	/**
	 * _記法をキャメル記法に変換します。
	 * 
	 * @param s テキスト
	 * @return 結果の文字列
	 */
	public static String camelize(String s) {
		String current = s;
		if (current == null) {
			return null;
		}
		current = current.toLowerCase(Locale.getDefault());
		String[] array = StringUtils.split(current, "_");
		if (array.length == 1) {
			return capitalize(current);
		}
		StringBuffer buf = new StringBuffer(CAMELIZE_BUFF_SIZE);
		for (int i = 0; i < array.length; ++i) {
			buf.append(capitalize(array[i]));
		}
		return buf.toString();
	}
	
	/**
	 * JavaBeansの仕様にしたがってキャピタライズを行ないます。
	 * 大文字が2つ以上続く場合は、大文字にならないので注意してください。
	 * 
	 * @param name 名前
	 * @return 結果の文字列
	 */
	public static String capitalize(String name) {
		if (isEmpty(name)) {
			return name;
		}
		char[] chars = name.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		return new String(chars);
	}
	
	/**
	 * charを含んでいるかどうか返します。
	 * 
	 * @param str 文字列
	 * @param ch char
	 * @return charを含んでいるかどうか
	 */
	public static boolean contains(String str, char ch) {
		if (isEmpty(str)) {
			return false;
		}
		return str.indexOf(ch) >= 0;
	}
	
	/**
	 * 文字列を含んでいるかどうか返します。
	 * 
	 * @param s1 文字列
	 * @param s2 比較する対象となる文字列
	 * @return 文字列を含んでいるかどうか
	 */
	public static boolean contains(String s1, String s2) {
		if (isEmpty(s1)) {
			return false;
		}
		return s1.indexOf(s2) >= 0;
	}
	
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
	 * キャメル記法を_記法に変換します。
	 * 
	 * @param s テキスト
	 * @return 結果の文字列
	 */
	public static String decamelize(String s) {
		if (s == null) {
			return null;
		}
		if (s.length() == 1) {
			return s.toUpperCase(Locale.getDefault());
		}
		StringBuffer buf = new StringBuffer(CAMELIZE_BUFF_SIZE);
		int pos = 0;
		for (int i = 1; i < s.length(); ++i) {
			if (Character.isUpperCase(s.charAt(i))) {
				if (buf.length() != 0) {
					buf.append('_');
				}
				buf.append(s.substring(pos, i).toUpperCase(Locale.getDefault()));
				pos = i;
			}
		}
		if (buf.length() != 0) {
			buf.append('_');
		}
		buf.append(s.substring(pos, s.length()).toUpperCase(Locale.getDefault()));
		return buf.toString();
	}
	
	/**
	 * JavaBeansの仕様にしたがってデキャピタライズを行ないます。大文字が2つ以上続く場合は、小文字にならないので注意してください。
	 * 
	 * @param name 名前
	 * @return 結果の文字列
	 */
	public static String decapitalize(String name) {
		if (isEmpty(name)) {
			return name;
		}
		char[] chars = name.toCharArray();
		if (chars.length >= 2 && Character.isUpperCase(chars[0]) && Character.isUpperCase(chars[1])) {
			return name;
		}
		chars[0] = Character.toLowerCase(chars[0]);
		return new String(chars);
	}
	
	/**
	 * ケースインセンシティブで特定の文字で終わっているのかどうかを返します。
	 * 
	 * @param target1 テキスト
	 * @param target2 比較する文字列
	 * @return ケースインセンシティブで特定の文字で終わっているのかどうか
	 */
	public static boolean endsWithIgnoreCase(String target1, String target2) {
		if (target1 == null || target2 == null) {
			return false;
		}
		int length1 = target1.length();
		int length2 = target2.length();
		if (length1 < length2) {
			return false;
		}
		String s1 = target1.substring(length1 - length2);
		return s1.equalsIgnoreCase(target2);
	}
	
	/**
	* 文字列同士が等しいかどうか返します。どちらもnullの場合は、{@code true}を返します。
	* 
	* @param target1 文字列1
	* @param target2 文字列2
	* @return 文字列同士が等しいかどうか
	*/
	public static boolean equals(String target1, String target2) {
		return (target1 == null) ? (target2 == null) : target1.equals(target2);
	}
	
	/**
	 * ケースインセンシティブで文字列同士が等しいかどうか返します。どちらもnullの場合は、{@code true}を返します。
	 * 
	 * @param target1 文字列1
	 * @param target2 文字列2
	 * @return ケースインセンシティブで文字列同士が等しいか
	 */
	public static boolean equalsIgnoreCase(String target1, String target2) {
		return (target1 == null) ? (target2 == null) : target1.equalsIgnoreCase(target2);
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
	 * ブランクかどうか返します。
	 * 
	 * @param str 文字列
	 * @return ブランクかどうか
	 */
	public static boolean isBlank(String str) {
		if (str == null || str.length() == 0) {
			return true;
		}
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 文字列が{@code null}または空文字列なら{@code true}を返します。
	 * 
	 * @param text 文字列
	 * @return 文字列が{@code null}または空文字列なら{@code true}
	 */
	public static boolean isEmpty(String text) {
		return text == null || text.length() == 0;
	}
	
	/**
	 * ブランクではないかどうか返します。
	 * 
	 * @param str 文字列
	 * @return ブランクではないかどうか
	 * @see #isBlank(String)
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}
	
	/**
	 * 文字列が{@code null}でも空文字列でもなければ{@code true}を返します。
	 * 
	 * @param text 文字列
	 * @return 文字列が{@code null}でも空文字列でもなければ{@code true}
	 */
	public static boolean isNotEmpty(String text) {
		return !isEmpty(text);
	}
	
	/**
	 * 文字列が数値のみで構成されているかどうかを返します。
	 * 
	 * @param s 文字列
	 * @return 数値のみで構成されている場合、{@code true}
	 */
	public static boolean isNumber(String s) {
		if (s == null || s.length() == 0) {
			return false;
		}
		
		int size = s.length();
		for (int i = 0; i < size; i++) {
			char chr = s.charAt(i);
			if (chr < '0' || '9' < chr) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 左側の空白を削ります。
	 * 
	 * @param text テキスト
	 * @return 結果の文字列
	 */
	public static String ltrim(String text) {
		return ltrim(text, null);
	}
	
	/**
	 * 左側の指定した文字列を削ります。
	 * 
	 * @param text テキスト
	 * @param trimText 削るテキスト
	 * @return 結果の文字列
	 */
	public static String ltrim(String text, String trimText) {
		if (text == null) {
			return null;
		}
		if (trimText == null) {
			trimText = " ";
		}
		int pos = 0;
		for (; pos < text.length(); pos++) {
			if (trimText.indexOf(text.charAt(pos)) < 0) {
				break;
			}
		}
		return text.substring(pos);
	}
	
	/**
	 * 文字列を置き換えます。
	 * 
	 * @param text テキスト
	 * @param fromText 置き換え対象のテキスト
	 * @param toText 置き換えるテキスト
	 * @return 結果
	 */
	public static String replace(String text, String fromText, String toText) {
		if (text == null || fromText == null || toText == null) {
			return null;
		}
		StringBuffer buf = new StringBuffer(REPLACE_BUFF_SIZE);
		int pos = 0;
		int pos2 = 0;
		while (true) {
			pos = text.indexOf(fromText, pos2);
			if (pos == 0) {
				buf.append(toText);
				pos2 = fromText.length();
			} else if (pos > 0) {
				buf.append(text.substring(pos2, pos));
				buf.append(toText);
				pos2 = pos + fromText.length();
			} else {
				buf.append(text.substring(pos2));
				break;
			}
		}
		return buf.toString();
	}
	
	/**
	 * 右側の空白を削ります。
	 * 
	 * @param text テキスト
	 * @return 結果の文字列
	 */
	public static String rtrim(String text) {
		return rtrim(text, null);
	}
	
	/**
	 * 右側の指定した文字列を削ります。
	 * 
	 * @param text テキスト
	 * @param trimText 削る文字列
	 * @return 結果の文字列
	 */
	public static String rtrim(String text, String trimText) {
		if (text == null) {
			return null;
		}
		if (trimText == null) {
			trimText = " ";
		}
		int pos = text.length() - 1;
		for (; pos >= 0; pos--) {
			if (trimText.indexOf(text.charAt(pos)) < 0) {
				break;
			}
		}
		return text.substring(0, pos + 1);
	}
	
	/**
	 * 文字列を分割します。
	 * 
	 * @param str 文字列
	 * @param delim 分割するためのデリミタ
	 * @return 分割された文字列の配列
	 */
	public static String[] split(String str, String delim) {
		if (isEmpty(str)) {
			return EMPTY_STRINGS;
		}
		return str.split(delim);
	}
	
	/**
	 * ケースインセンシティブで特定の文字ではじまっているのかどうかを返します。
	 * 
	 * @param target1 テキスト
	 * @param target2 比較する文字列
	 * @return ケースインセンシティブで特定の文字ではじまっているのかどうか
	 */
	public static boolean startsWithIgnoreCase(String target1, String target2) {
		if (target1 == null || target2 == null) {
			return false;
		}
		int length1 = target1.length();
		int length2 = target2.length();
		if (length1 < length2) {
			return false;
		}
		String s1 = target1.substring(0, target2.length());
		return s1.equalsIgnoreCase(target2);
	}
	
	/**
	 * 文字列の最後から指定した文字列で始まっている部分より手前を返します。
	 * 
	 * @param str 文字列
	 * @param separator セパレータ
	 * @return 結果の文字列
	 */
	public static String substringFromLast(String str, String separator) {
		if (isEmpty(str) || isEmpty(separator)) {
			return str;
		}
		int pos = str.lastIndexOf(separator);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}
	
	/**
	 * 文字列の最後から指定した文字列で始まっている部分より後ろを返します。
	 * 
	 * @param str 文字列
	 * @param separator セパレータ
	 * @return 結果の文字列
	 */
	public static String substringToLast(String str, String separator) {
		if (isEmpty(str) || isEmpty(separator)) {
			return str;
		}
		int pos = str.lastIndexOf(separator);
		if (pos == -1) {
			return str;
		}
		return str.substring(pos + 1, str.length());
	}
	
	/**
	 * 文字列の1文字目を大文字にする。
	 * 
	 * @param str 入力文字列
	 * @return 出力文字列
	 */
	public static String toCapital(String str) {
		char[] ch = str.toCharArray();
		ch[0] = Character.toUpperCase(ch[0]);
		return new String(ch);
	}
	
	/**
	 * 16進数の文字列に変換します。
	 * 
	 * @param bytes バイトの配列
	 * @return 16進数の文字列
	 */
	public static String toHex(byte[] bytes) {
		if (bytes == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; ++i) {
			appendHex(sb, bytes[i]);
		}
		return sb.toString();
	}
	
	/**
	 * 16進数の文字列に変換します。
	 * 
	 * @param i int
	 * @return 16進数の文字列
	 */
	public static String toHex(int i) {
		StringBuffer buf = new StringBuffer();
		appendHex(buf, i);
		return buf.toString();
	}
	
	/**
	 * SQL名（ex. AGILE_DATABASE）からJavaクラス名（ex. AgileDatabase）を生成する。
	 * 
	 * @param str SQL名
	 * @return Javaクラス名
	 */
	public static String toJavaClassName(String str) {
		return toCapital(toJavaName(str));
	}
	
	/**
	 * SQL名（ex. AGILE_DATABASE）からJavaクラス名（ex. AgileDatabase）を生成する。 prifixが存在した場合、取り除く。
	 * 
	 * @param str SQL名
	 * @param prefix 接頭辞
	 * @return Javaクラス名
	 */
	public static String toJavaClassName(String str, String prefix) {
		return toCapital(toJavaName(str, prefix));
	}
	
	/**
	 * SQL名（ex. AGILE_DATABASE）からJava名（ex. agileDatabase）を生成する。
	 * 
	 * @param str SQL名
	 * @return Java名
	 */
	public static String toJavaName(String str) {
		return toJavaName(str, null);
	}
	
	/**
	 * SQL名（ex. AGILE_DATABASE）からJava名（ex. agileDatabase）を生成する。 prifixが存在した場合、取り除く。
	 * 
	 * @param str SQL名
	 * @param prefix 接頭辞
	 * @return Java名
	 */
	public static String toJavaName(String str, String prefix) {
		if (str == null) {
			return str;
		}
		String low;
		if (prefix != null && str.startsWith(prefix)) {
			low = str.replaceFirst(prefix, "").toLowerCase(Locale.getDefault());
		} else {
			low = str.toLowerCase(Locale.getDefault());
		}
		StringBuilder sb = new StringBuilder(low);
		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
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
	public static String toSQLName(String str) {
		return toSQLName(str, StringUtils.EMPTY);
	}
	
	/**
	 * Java名（ex. AgileDatabase, agileDatabase）からSQL名（ex. AGILE_DATABASE）を生成する。 prifixを付加する。
	 * 
	 * @param str Java名
	 * @param prefix 接頭辞
	 * @return SQL名
	 */
	public static String toSQLName(String str, String prefix) {
		if (str == null || str.length() == 0) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
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
		char[] ch = str.toCharArray();
		ch[0] = Character.toLowerCase(ch[0]);
		return new String(ch);
	}
	
	/**
	 * プレフィックスを削除する。
	 * 
	 * @param text テキスト
	 * @param prefix プレフィックス
	 * @return 結果の文字列
	 */
	public static String trimPrefix(String text, String prefix) {
		if (text == null) {
			return null;
		}
		if (prefix == null) {
			return text;
		}
		if (text.startsWith(prefix)) {
			return text.substring(prefix.length());
		}
		return text;
	}
	
	/**
	 * サフィックスを削ります。
	 * 
	 * @param text テキスト
	 * @param suffix サフィックス
	 * @return 結果の文字列
	 */
	public static String trimSuffix(String text, String suffix) {
		if (text == null) {
			return null;
		}
		if (suffix == null) {
			return text;
		}
		if (text.endsWith(suffix)) {
			return text.substring(0, text.length() - suffix.length());
		}
		return text;
	}
	
	private JmStringUtil() {
	}
}
