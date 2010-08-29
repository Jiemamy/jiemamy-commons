/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2009/02/19
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

import java.util.Calendar;

import org.apache.commons.lang.Validate;

/**
 * 日付時間系ユーティリティ。
 * 
 * <p>cf. http://d.hatena.ne.jp/higayasuo/20090219/1235020303</p>
 * 
 * @version $Id$
 * @author daisuke
 */
public final class DateUtil {
	
	/** Unix の「紀元年」 */
	static final int UNIX_EPOCH = 1970;
	

	/**
	 * {@link java.util.Date}を{@link java.sql.Date}に変換する。
	 * 
	 * @param date {@link java.util.Date}
	 * @return　{@link java.sql.Date}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static java.sql.Date toSqlDate(java.util.Date date) {
		Validate.notNull(date);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new java.sql.Date(cal.getTimeInMillis());
	}
	
	/**
	 * {@link java.util.Date}を{@link java.sql.Time}に変換する。
	 * 
	 * @param date {@link java.util.Date}
	 * @return {@link java.sql.Time}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static java.sql.Time toSqlTime(java.util.Date date) {
		Validate.notNull(date);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.YEAR, UNIX_EPOCH);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DATE, 1);
		return new java.sql.Time(cal.getTimeInMillis());
	}
	
	private DateUtil() {
	}
}
