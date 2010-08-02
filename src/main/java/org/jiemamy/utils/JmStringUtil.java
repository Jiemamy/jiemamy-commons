/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
import org.apache.commons.lang.Validate;

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
	 * 文字列に、数値を16進数に変換した文字列を追加する。
	 * 
	 * @param buf 追加先の文字列
	 * @param i 数値
	 * @throws IllegalArgumentException 引数{@code buf}に{@code null}を与えた場合
	 */
	public static void appendHex(StringBuilder buf, byte i) {
		Validate.notNull(buf);
		buf.append(Character.forDigit((i >> 4) & 0x0F, 16));
		buf.append(Character.forDigit(i & 0x0F, 16)); // CHECKSTYLE IGNORE THIS LINE
	}
	
	/**
	 * 文字列に、数値を16進数に変換した文字列を追加する。
	 * 
	 * @param buf 追加先の文字列
	 * @param i 数値
	 * @throws IllegalArgumentException 引数{@code buf}に{@code null}を与えた場合
	 */
	public static void appendHex(StringBuilder buf, int i) {
		Validate.notNull(buf);
		for (int shift = 24; shift >= 0; shift -= 8) { // CHECKSTYLE IGNORE THIS LINE
			String str = Integer.toHexString((i >> shift) & 0xFF); // CHECKSTYLE IGNORE THIS LINE
			if (str.length() == 1) {
				buf.append('0');
			}
			buf.append(str);
		}
	}
	
	/**
	 * _記法をキャメル記法に変換する。
	 * 
	 * @param s テキスト
	 * @return 結果の文字列。{@code s}が{@code null}だった場合は{@code null}
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
		StringBuilder buf = new StringBuilder(CAMELIZE_BUFF_SIZE);
		for (int i = 0; i < array.length; ++i) {
			buf.append(capitalize(array[i]));
		}
		return buf.toString();
	}
	
	/**
	 * JavaBeansの仕様にしたがってキャピタライズを行う。
	 * 
	 * <p>大文字が2つ以上続く場合は、大文字にならないので注意。</p>
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
	 * キャメル記法を_記法に変換する。
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
		StringBuilder buf = new StringBuilder(CAMELIZE_BUFF_SIZE);
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
	 * JavaBeansの仕様にしたがってデキャピタライズを行う。
	 * 
	 * <p>大文字が2つ以上続く場合は、小文字にならないので注意。</p>
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
	 * ケースインセンシティブで特定の文字で終わっているのかどうかを返す。
	 * 
	 * @param target テキスト
	 * @param suffix 比較する文字列
	 * @return ケースインセンシティブで特定の文字で終わっている場合は{@code true}、そうでない場合は{@code false}
	 */
	public static boolean endsWithIgnoreCase(String target, String suffix) {
		if (target == null || suffix == null) {
			return false;
		}
		int targetLength = target.length();
		int suffixLength = suffix.length();
		if (targetLength < suffixLength) {
			return false;
		}
		String s1 = target.substring(targetLength - suffixLength);
		return s1.equalsIgnoreCase(suffix);
	}
	
	/**
	* 文字列同士が等しいかどうか返します。どちらもnullの場合は、{@code true}を返す。
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
	 * <p>マッチする要素が複数あった場合は、よりインデックスの小さいものを返す。</p>
	 * 
	 * @param array 調査対象配列
	 * @param stringToFind 探す文字列
	 * @param startIndex 調査開始インデックス
	 * @return インデックス番号。見つからなかった場合は{@code -1}
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
	 * ブランクかどうか返す。
	 * 
	 * @param str 文字列
	 * @return ブランクの場合は{@code true}、そうでない場合は{@code false}
	 */
	public static boolean isBlank(String str) {
		if (str == null || str.length() == 0) {
			return true;
		}
		for (int i = 0; i < str.length(); i++) {
			if (Character.isWhitespace(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 文字列が{@code null}または空文字列なら{@code true}を返す。
	 * 
	 * @param text 文字列
	 * @return 文字列が{@code null}または空文字列なら{@code true}
	 */
	public static boolean isEmpty(String text) {
		return text == null || text.length() == 0;
	}
	
	/**
	* JavaClassNameであるか判定する。
	* 
	* @param name 判定する文字列
	* @return {@code JavaClassName}である場合{@code true}
	*/
	public static boolean isJavaClassName(String name) {
		if (isEmpty(name)) {
			return false;
		}
		return name.matches("([A-Z]+[a-z]*)+");
	}
	
	/**
	 * JavaNameであるか判定する。
	 * 
	 * @param name 判定する文字列
	 * @return {@code JavaName}である場合{@code true}
	 */
	public static boolean isJavaName(String name) {
		if (isEmpty(name)) {
			return false;
		}
		return name.matches("[a-z]+([A-Z]+[a-z]*)*");
	}
	
	/**
	 * ブランクではないかどうか返す。
	 * 
	 * @param str 文字列
	 * @return ブランクではないかどうか
	 * @see #isBlank(String)
	 */
	public static boolean isNotBlank(String str) {
		return isBlank(str) == false;
	}
	
	/**
	 * 文字列が{@code null}でも空文字列でもなければ{@code true}を返す。
	 * 
	 * @param text 文字列
	 * @return 文字列が{@code null}でも空文字列でもなければ{@code true}
	 */
	public static boolean isNotEmpty(String text) {
		return isEmpty(text) == false;
	}
	
	/**
	 * 文字列が数値のみで構成されているかどうかを返す。
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
	 * SQL Nameであるか判定する。
	 * <p>
	 * 全ての文字が大文字またはアンダースコアで構成された文字のことをSQL Nameと定義する。</p>
	 * <pre>
	 * JmStringUtil.isSqlName(null)      = false
	 * JmStringUtil.isSqlName("")        = false
	 * JmStringUtil.isSqlName("A")       = true
	 * JmStringUtil.isSqlName("a")       = false
	 * JmStringUtil.isSqlName("AAA")     = true
	 * JmStringUtil.isSqlName("aaa")     = false
	 * JmStringUtil.isSqlName("AAA_BBB") = true
	 * JmStringUtil.isSqlName("aaa_bbb") = false
	 * JmStringUtil.isSqlName("AAA_bbb") = false
	 * JmStringUtil.isSqlName("AAAbbb")  = false
	 * JmStringUtil.isSqlName("AAA-")    = false
	 * </pre>
	 * 
	 * @param name 判定する文字列
	 * @return {@code SQL Name}である場合{@code true}
	 */
	public static boolean isSqlName(String name) {
		if (isEmpty(name)) {
			return false;
		}
		return name.matches("[A-Z_]+");
	}
	
	/**
	 * 左側の空白を削る。
	 * 
	 * @param text テキスト
	 * @return 結果の文字列
	 */
	public static String ltrim(String text) {
		return ltrim(text, null);
	}
	
	/**
	 * 左側の指定した文字列を削る。
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
	 * 文字列{@code text}中に含まれる{@code fromText}を全て{@code toText}に置き換える。
	 * 
	 * @param text テキスト
	 * @param fromText 置き換え対象のテキスト
	 * @param toText 置き換えるテキスト
	 * @return 結果
	 */
	public static String replace(String text, String fromText, String toText) {
		if (text == null || fromText == null || toText == null) {
			return text;
		}
		StringBuilder buf = new StringBuilder(REPLACE_BUFF_SIZE);
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
	 * 右側の空白を削る。
	 * 
	 * @param text テキスト
	 * @return 結果の文字列
	 */
	public static String rtrim(String text) {
		return rtrim(text, null);
	}
	
	/**
	 * 右側の指定した文字列を削る。
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
	 * 文字列を分割する。
	 * 
	 * @param str 文字列
	 * @param delim 分割するためのデリミタ
	 * @return 分割された文字列の配列
	 */
	public static String[] split(String str, String delim) {
		if (isEmpty(str)) {
			return EMPTY_STRINGS;
		}
		if (isEmpty(delim)) {
			return new String[] {
				str
			};
		}
		return str.split(delim);
	}
	
	/**
	 * ケースインセンシティブで特定の文字ではじまっているのかどうかを返す。
	 * 
	 * @param target テキスト
	 * @param prefix 比較する文字列
	 * @return ケースインセンシティブで特定の文字ではじまっているのかどうか
	 */
	public static boolean startsWithIgnoreCase(String target, String prefix) {
		if (target == null || prefix == null) {
			return false;
		}
		int targetLength = target.length();
		int prefixLength = prefix.length();
		if (targetLength < prefixLength) {
			return false;
		}
		String s1 = target.substring(0, prefix.length());
		return s1.equalsIgnoreCase(prefix);
	}
	
	/**
	 * 文字列の最後から指定した文字列で始まっている部分より手前を返す。
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
	 * 文字列の最後から指定した文字列で始まっている部分より後ろを返す。
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
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static String toCapital(String str) {
		Validate.notNull(str);
		char[] ch = str.toCharArray();
		ch[0] = Character.toUpperCase(ch[0]);
		return new String(ch);
	}
	
	/**
	 * 16進数の文字列に変換する。
	 * 
	 * @param bytes バイトの配列
	 * @return 16進数の文字列
	 */
	public static String toHex(byte[] bytes) {
		if (bytes == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (int i = 0; i < bytes.length; ++i) {
			appendHex(sb, bytes[i]);
		}
		return sb.toString();
	}
	
	/**
	 * 16進数の文字列に変換する。
	 * 
	 * @param i int
	 * @return 16進数の文字列
	 */
	public static String toHex(int i) {
		StringBuilder buf = new StringBuilder();
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
		if (isEmpty(str)) {
			return str;
		}
		if (isSqlName(str)) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			if (Character.isUpperCase(c) && i != 0) {
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
	 * <p>{@code text}が{@code prefix}から始まらない場合は何もしない。</p>
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
	 * <p>{@code text}が{@code suffix}で終わらない場合は何もしない。</p>
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
