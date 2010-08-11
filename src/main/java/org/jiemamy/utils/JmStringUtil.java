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
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;

/**
 * {@link String}関係のユーティリティクラス。
 * 
 * <p>主に、文字列からの変換、文字列への変換、文字列の判定ユーティリティを含む。</p>
 * 
 * <h3>当クラス内で使用する用語の定義</h3>
 * <dl>
 *  <dt>Javaクラス名</dt>
 *  <dd>Javaのクラスを表す名前。複合語の先頭を、大文字で書き始めるアッパーキャメルケースで表される。
 *      Javaクラス名には大文字小文字の英数字を使用できる。ex.{@code AgileDatabase}</dd>
 *  <dt>Java名</dt>
 *  <dd>Javaのメソッド名や変数名を表す名前。複合語の先頭を小文字で書き始めるローワーキャメルケースで表される。
 *      Javaクラス名には大文字小文字の英数字を使用できる。ex.{@code agileDatabase}</dd>
 *  <dt>SQL名</dt>
 *  <dd>Databaseにおけるテーブルやカラム名を表す名前。アンダースコア(_)で単語を連結するスネークケースで表される。
 *      SQL名には大文字英数字、アンダースコアが使用できる。ex.{@code AGILE_DATABASE}</dd>
 * </dl>
 * 
 * @author daisuke
 * @author wencheng
 * @author yamkazu
 */
public final class JmStringUtil {
	
	private static final int REPLACE_BUFF_SIZE = 100;
	
	/** 空の文字列の配列。*/
	public static final String[] EMPTY_STRINGS = new String[0];
	
	/** Javaクラス名の正規表現。 */
	private static final Pattern JAVA_CLASS_NAME_PATTERN = Pattern.compile("^[A-Z][a-zA-Z0-9]*$");
	
	/** Javaクラス名の除外となるクラス名の正規表現。 */
	private static final Pattern JAVA_CLASS_NAME_IGNORE_PATTERN = Pattern.compile("^[A-Z]+$");
	
	/** Java名の正規表現。 */
	private static final Pattern JAVA_NAME_PATTERN = Pattern.compile("^[a-z][a-zA-Z0-9]*$");
	
	/** SQL名の正規表現。 */
	private static final Pattern SQL_NAME_PATTERN = Pattern.compile("^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$");
	

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
	 * SQL名にプレフィックスを追加した文字列を返す。
	 * 
	 * <p>{@code prefix}が{@code _}で終了していない場合は{@code prefix}に{@code _}を付与して処理を行う。</p>
	 * 
	 * @param sqlName 追加元のSQL名
	 * @param prefix 追加するプレフィックス
	 * @return プレフィックス付きのSQL名。{@code sqlName}または{@code prefix}が空文字列の場合は{@code sqlName}
	 */
	public static String appendSqlPrefix(String sqlName, String prefix) {
		if (isEmpty(sqlName) || isEmpty(prefix)) {
			return sqlName;
		}
		return prefix.replaceFirst("[^_]$", "$0_").toUpperCase(Locale.getDefault()) + sqlName;
	}
	
	/**
	 * 文字列の1文字目を大文字にする。
	 * 
	 * @param str 入力文字列
	 * @return 出力文字列。もし{@code str}が空文字の場合は単にその文字列。
	 */
	public static String capitalize(String str) {
		if (isEmpty(str)) {
			return str;
		}
		char[] ch = str.toCharArray();
		ch[0] = Character.toUpperCase(ch[0]);
		return new String(ch);
	}
	
	/**
	 * JavaBeansの仕様にしたがってキャピタライズを行う。
	 * 
	 * <p>{@link #decapitalizeAsJavaBeans(String)}では大文字が2つ以上続く場合は、
	 * 小文字にならない動作をするが、{@link #capitalizeAsJavaBeans(String)}では
	 * そのような考慮はなく単に1文字目を大文字する。</p>
	 * 
	 * @param name 名前
	 * @return 結果の文字列。もし{@code name}が空文字の場合は単にその文字列。
	 */
	public static String capitalizeAsJavaBeans(String name) {
		// 対称性を維持するために残しておく
		return capitalize(name);
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
	 * 文字列の1文字目を小文字にする。
	 * 
	 * @param str 入力文字列
	 * @return 出力文字列。もし{@code str}が空文字の場合は単にその文字列。
	 */
	public static String decapitalize(String str) {
		if (isEmpty(str)) {
			return str;
		}
		char[] ch = str.toCharArray();
		ch[0] = Character.toLowerCase(ch[0]);
		return new String(ch);
	}
	
	/**
	 * JavaBeansの仕様にしたがってデキャピタライズを行う。
	 * 
	 * <p>大文字が2つ以上続く場合は、小文字にならないので注意。</p>
	 * 
	 * @param name 名前
	 * @return 結果の文字列。もし{@code name}が空文字の場合は単にその文字列。
	 */
	public static String decapitalizeAsJavaBeans(String name) {
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
	* 文字列同士が等しいかどうか返します。どちらも{@code null}の場合は、{@code true}を返す。
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
		if (isEmpty(str)) {
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
	 * Javaクラス名であるか判定する。
	 * 
	 * <p>判定は文字列が{@code ^[A-Z][a-zA-Z0-9]*$}に適合しているかで処理する。</p>
	 * 
	 * <p>ただし例外として全ての文字列が大文字だった場合は{@code false}を返す。</p>
	 * 
	 * @param name 判定する文字列
	 * @return Javaクラス名である場合{@code true}。{@code name}が空文字の場合は{@code false}
	 */
	public static boolean isJavaClassName(String name) {
		if (isEmpty(name)) {
			return false;
		}
		// THINK 誰か正規表現を使わずに高速化してください
		return JAVA_CLASS_NAME_PATTERN.matcher(name).matches()
				&& JAVA_CLASS_NAME_IGNORE_PATTERN.matcher(name).matches() == false;
	}
	
	/**
	 * Java名であるか判定する。
	 * 
	 * <p>判定は文字列が{@code ^[a-z][a-zA-Z0-9]*$}に適合しているかで処理する。</p>
	 * 
	 * @param name 判定する文字列
	 * @return Java名である場合{@code true}。{@code name}が空文字の場合は{@code false}
	 */
	public static boolean isJavaName(String name) {
		if (isEmpty(name)) {
			return false;
		}
		// THINK 誰か正規表現を使わずに高速化してください
		return JAVA_NAME_PATTERN.matcher(name).matches();
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
	 * SQL名であるか判定する。
	 * 
	 * <p>判定は文字列が{@code ^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$}に適合しているかで処理する。</p>
	 * 
	 * @param name 判定する文字列
	 * @return SQL名である場合{@code true}。{@code name}が空文字の場合は{@code false}
	 */
	public static boolean isSqlName(String name) {
		if (isEmpty(name)) {
			return false;
		}
		// THINK 誰か正規表現を使わずに高速化してください
		return SQL_NAME_PATTERN.matcher(name).matches();
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
	 * SQL名からプレフィックスを削除する。
	 * 
	 * <p>{@code prefix}が{@code _}で終了していない場合は{@code prefix}に{@code _}を付与して処理を行う。</p>
	 * 
	 * @param sqlName 削除元のSQL名
	 * @param prefix 削除するプレフィックス
	 * @return プレフィックスを削除したSQL名。{@code sqlName}または{@code prefix}が空文字の時は{@code sqlName}
	 */
	public static String removeSqlPrefix(String sqlName, String prefix) {
		if (isEmpty(sqlName) || isEmpty(prefix)) {
			return sqlName;
		}
		return sqlName.replaceFirst(prefix + "_?", "");
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
	 * SQL名、またはJava名からJavaクラス名を生成する。
	 * 
	 * <p>文字列が、SQL名、Javaクラス名、Java名でもない場合は{@link String#toUpperCase(Locale)}を
	 * 掛けることによって、SQL名化して上でそれを処理する。</p>
	 * 
	 * <p>Javaクラス名を処理する場合は、単にその文字列を返す。ただし例外として全ての文字列が大文字
	 * の場合は、その文字列はSQL名として処理する。例えば、{@code FOO}を受け取った場合は、SQL名として
	 * 処理し{@code Foo}が生成される。</p>
	 * 
	 * @param str SQL名またはJava名
	 * @return Javaクラス名。もし{@code str}が空文字の場合は単にその文字列。
	 */
	public static String toJavaClassName(String str) {
		if (isEmpty(str)) {
			return str;
		}
		return capitalize(toJavaName(str));
	}
	
	/**
	 * SQL名、またはJavaクラス名からJava名を生成する。
	 * 
	 * <p>文字列が、SQL名、Javaクラス名、Java名でもない場合は{@link String#toUpperCase(Locale)}を
	 * 掛けることによって、SQL名化した上でそれを処理する。</p>
	 * 
	 * <p>Java名を処理する場合は、単にその文字列を返す。</p>
	 * 
	 * @param str SQL名またはJavaクラス名
	 * @return Java名。もし{@code str}が空文字の場合は単にその文字列。
	 */
	public static String toJavaName(String str) {
		if (isEmpty(str)) {
			return str;
		}
		if (isJavaName(str)) {
			return str;
		}
		if (isJavaClassName(str)) {
			return decapitalizeAsJavaBeans(str);
		}
		String upper;
		if (isSqlName(str) == false) {
			upper = str.toUpperCase(Locale.getDefault());
		} else {
			upper = str;
		}
		
		StringBuilder sb = new StringBuilder(upper.toLowerCase(Locale.getDefault()));
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
	 * Javaクラス名、Java名からSLQ名を生成する。
	 * 
	 * <p>SQL名を処理する場合は、単にその文字列を返す。</p>
	 * 
	 * @param str Javaクラス名またはJava名
	 * @return SQL名。もし{@code str}が空文字の場合は単にその文字列。
	 */
	public static String toSqlName(String str) {
		if (isEmpty(str)) {
			return str;
		}
		if (isSqlName(str)) {
			return str;
		}
		if ((isJavaClassName(str) || isJavaName(str)) == false) {
			return str.toUpperCase(Locale.getDefault());
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
		return sb.toString();
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
