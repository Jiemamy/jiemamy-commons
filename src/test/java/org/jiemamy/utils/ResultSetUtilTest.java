/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/13
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
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import org.junit.Test;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class ResultSetUtilTest {
	
	private static final String COL = "COL";
	
	boolean called;
	

	/**
	 * TODO for daisuke
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testname() throws Exception {
		assertThat(ResultSetUtil.getValue(BigInteger.class, new Mock(), COL, BigInteger.TEN), is(BigInteger.TEN));
		
		test(Boolean.class, false, true, new Mock() {
			
			@Override
			public boolean getBoolean(String columnName) {
				assertThat(columnName, is(COL));
				called = true;
				return true;
			}
			
		});
		
		test(String.class, "foo", "bar", new Mock() {
			
			@Override
			public String getString(String columnName) {
				assertThat(columnName, is(COL));
				called = true;
				return "bar";
			}
			
		});
		
		test(Byte.class, Byte.MIN_VALUE, Byte.MAX_VALUE, new Mock() {
			
			@Override
			public byte getByte(String columnName) {
				assertThat(columnName, is(COL));
				called = true;
				return Byte.MAX_VALUE;
			}
			
		});
		
	}
	
	private <T>void test(Class<T> clazz, T defaultValue, T actual, ResultSet rs) {
		called = false;
		assertThat(ResultSetUtil.getValue(clazz, rs, COL, defaultValue), is(actual));
		assertThat(called, is(true));
	}
	

	private static class Mock implements ResultSet {
		
		public boolean absolute(int row) {
			throw new AssertionError();
		}
		
		public void afterLast() {
			throw new AssertionError();
		}
		
		public void beforeFirst() {
			throw new AssertionError();
		}
		
		public void cancelRowUpdates() {
			throw new AssertionError();
		}
		
		public void clearWarnings() {
			throw new AssertionError();
		}
		
		public void close() {
			throw new AssertionError();
		}
		
		public void deleteRow() {
			throw new AssertionError();
		}
		
		public int findColumn(String columnName) {
			throw new AssertionError();
		}
		
		public boolean first() {
			throw new AssertionError();
		}
		
		public Array getArray(int i) {
			throw new AssertionError();
		}
		
		public Array getArray(String colName) {
			throw new AssertionError();
		}
		
		public InputStream getAsciiStream(int columnIndex) {
			throw new AssertionError();
		}
		
		public InputStream getAsciiStream(String columnName) {
			throw new AssertionError();
		}
		
		public BigDecimal getBigDecimal(int columnIndex) {
			throw new AssertionError();
		}
		
		public BigDecimal getBigDecimal(int columnIndex, int scale) {
			throw new AssertionError();
		}
		
		public BigDecimal getBigDecimal(String columnName) {
			throw new AssertionError();
		}
		
		public BigDecimal getBigDecimal(String columnName, int scale) {
			throw new AssertionError();
		}
		
		public InputStream getBinaryStream(int columnIndex) {
			throw new AssertionError();
		}
		
		public InputStream getBinaryStream(String columnName) {
			throw new AssertionError();
		}
		
		public Blob getBlob(int i) {
			throw new AssertionError();
		}
		
		public Blob getBlob(String colName) {
			throw new AssertionError();
		}
		
		public boolean getBoolean(int columnIndex) {
			throw new AssertionError();
		}
		
		public boolean getBoolean(String columnName) {
			throw new AssertionError();
		}
		
		public byte getByte(int columnIndex) {
			throw new AssertionError();
		}
		
		public byte getByte(String columnName) {
			throw new AssertionError();
		}
		
		public byte[] getBytes(int columnIndex) {
			throw new AssertionError();
		}
		
		public byte[] getBytes(String columnName) {
			throw new AssertionError();
		}
		
		public Reader getCharacterStream(int columnIndex) {
			throw new AssertionError();
		}
		
		public Reader getCharacterStream(String columnName) {
			throw new AssertionError();
		}
		
		public Clob getClob(int i) {
			throw new AssertionError();
		}
		
		public Clob getClob(String colName) {
			throw new AssertionError();
		}
		
		public int getConcurrency() {
			throw new AssertionError();
		}
		
		public String getCursorName() {
			throw new AssertionError();
		}
		
		public Date getDate(int columnIndex) {
			throw new AssertionError();
		}
		
		public Date getDate(int columnIndex, Calendar cal) {
			throw new AssertionError();
		}
		
		public Date getDate(String columnName) {
			throw new AssertionError();
		}
		
		public Date getDate(String columnName, Calendar cal) {
			throw new AssertionError();
		}
		
		public double getDouble(int columnIndex) {
			throw new AssertionError();
		}
		
		public double getDouble(String columnName) {
			throw new AssertionError();
		}
		
		public int getFetchDirection() {
			throw new AssertionError();
		}
		
		public int getFetchSize() {
			throw new AssertionError();
		}
		
		public float getFloat(int columnIndex) {
			throw new AssertionError();
		}
		
		public float getFloat(String columnName) {
			throw new AssertionError();
		}
		
		public int getInt(int columnIndex) {
			throw new AssertionError();
		}
		
		public int getInt(String columnName) {
			throw new AssertionError();
		}
		
		public long getLong(int columnIndex) {
			throw new AssertionError();
		}
		
		public long getLong(String columnName) {
			throw new AssertionError();
		}
		
		public ResultSetMetaData getMetaData() {
			throw new AssertionError();
		}
		
		public Object getObject(int columnIndex) {
			throw new AssertionError();
		}
		
		public Object getObject(int i, Map<String, Class<?>> map) {
			throw new AssertionError();
		}
		
		public Object getObject(String columnName) {
			throw new AssertionError();
		}
		
		public Object getObject(String colName, Map<String, Class<?>> map) {
			throw new AssertionError();
		}
		
		public Ref getRef(int i) {
			throw new AssertionError();
		}
		
		public Ref getRef(String colName) {
			throw new AssertionError();
		}
		
		public int getRow() {
			throw new AssertionError();
		}
		
		public short getShort(int columnIndex) {
			throw new AssertionError();
		}
		
		public short getShort(String columnName) {
			throw new AssertionError();
		}
		
		public Statement getStatement() {
			throw new AssertionError();
		}
		
		public String getString(int columnIndex) {
			throw new AssertionError();
		}
		
		public String getString(String columnName) {
			throw new AssertionError();
		}
		
		public Time getTime(int columnIndex) {
			throw new AssertionError();
		}
		
		public Time getTime(int columnIndex, Calendar cal) {
			throw new AssertionError();
		}
		
		public Time getTime(String columnName) {
			throw new AssertionError();
		}
		
		public Time getTime(String columnName, Calendar cal) {
			throw new AssertionError();
		}
		
		public Timestamp getTimestamp(int columnIndex) {
			throw new AssertionError();
		}
		
		public Timestamp getTimestamp(int columnIndex, Calendar cal) {
			throw new AssertionError();
		}
		
		public Timestamp getTimestamp(String columnName) {
			throw new AssertionError();
		}
		
		public Timestamp getTimestamp(String columnName, Calendar cal) {
			throw new AssertionError();
		}
		
		public int getType() {
			throw new AssertionError();
		}
		
		public InputStream getUnicodeStream(int columnIndex) {
			throw new AssertionError();
		}
		
		public InputStream getUnicodeStream(String columnName) {
			throw new AssertionError();
		}
		
		public URL getURL(int columnIndex) {
			throw new AssertionError();
		}
		
		public URL getURL(String columnName) {
			throw new AssertionError();
		}
		
		public SQLWarning getWarnings() {
			throw new AssertionError();
		}
		
		public void insertRow() {
			throw new AssertionError();
		}
		
		public boolean isAfterLast() {
			throw new AssertionError();
		}
		
		public boolean isBeforeFirst() {
			throw new AssertionError();
		}
		
		public boolean isFirst() {
			throw new AssertionError();
		}
		
		public boolean isLast() {
			throw new AssertionError();
		}
		
		public boolean last() {
			throw new AssertionError();
		}
		
		public void moveToCurrentRow() {
			throw new AssertionError();
		}
		
		public void moveToInsertRow() {
			throw new AssertionError();
		}
		
		public boolean next() {
			throw new AssertionError();
		}
		
		public boolean previous() {
			throw new AssertionError();
		}
		
		public void refreshRow() {
			throw new AssertionError();
		}
		
		public boolean relative(int rows) {
			throw new AssertionError();
		}
		
		public boolean rowDeleted() {
			throw new AssertionError();
		}
		
		public boolean rowInserted() {
			throw new AssertionError();
		}
		
		public boolean rowUpdated() {
			throw new AssertionError();
		}
		
		public void setFetchDirection(int direction) {
			throw new AssertionError();
		}
		
		public void setFetchSize(int rows) {
			throw new AssertionError();
		}
		
		public void updateArray(int columnIndex, Array x) {
			throw new AssertionError();
		}
		
		public void updateArray(String columnName, Array x) {
			throw new AssertionError();
		}
		
		public void updateAsciiStream(int columnIndex, InputStream x, int length) {
			throw new AssertionError();
		}
		
		public void updateAsciiStream(String columnName, InputStream x, int length) {
			throw new AssertionError();
		}
		
		public void updateBigDecimal(int columnIndex, BigDecimal x) {
			throw new AssertionError();
		}
		
		public void updateBigDecimal(String columnName, BigDecimal x) {
			throw new AssertionError();
		}
		
		public void updateBinaryStream(int columnIndex, InputStream x, int length) {
			throw new AssertionError();
		}
		
		public void updateBinaryStream(String columnName, InputStream x, int length) {
			throw new AssertionError();
		}
		
		public void updateBlob(int columnIndex, Blob x) {
			throw new AssertionError();
		}
		
		public void updateBlob(String columnName, Blob x) {
			throw new AssertionError();
		}
		
		public void updateBoolean(int columnIndex, boolean x) {
			throw new AssertionError();
		}
		
		public void updateBoolean(String columnName, boolean x) {
			throw new AssertionError();
		}
		
		public void updateByte(int columnIndex, byte x) {
			throw new AssertionError();
		}
		
		public void updateByte(String columnName, byte x) {
			throw new AssertionError();
		}
		
		public void updateBytes(int columnIndex, byte[] x) {
			throw new AssertionError();
		}
		
		public void updateBytes(String columnName, byte[] x) {
			throw new AssertionError();
		}
		
		public void updateCharacterStream(int columnIndex, Reader x, int length) {
			throw new AssertionError();
		}
		
		public void updateCharacterStream(String columnName, Reader reader, int length) {
			throw new AssertionError();
		}
		
		public void updateClob(int columnIndex, Clob x) {
			throw new AssertionError();
		}
		
		public void updateClob(String columnName, Clob x) {
			throw new AssertionError();
		}
		
		public void updateDate(int columnIndex, Date x) {
			throw new AssertionError();
		}
		
		public void updateDate(String columnName, Date x) {
			throw new AssertionError();
		}
		
		public void updateDouble(int columnIndex, double x) {
			throw new AssertionError();
		}
		
		public void updateDouble(String columnName, double x) {
			throw new AssertionError();
		}
		
		public void updateFloat(int columnIndex, float x) {
			throw new AssertionError();
		}
		
		public void updateFloat(String columnName, float x) {
			throw new AssertionError();
		}
		
		public void updateInt(int columnIndex, int x) {
			throw new AssertionError();
		}
		
		public void updateInt(String columnName, int x) {
			throw new AssertionError();
		}
		
		public void updateLong(int columnIndex, long x) {
			throw new AssertionError();
		}
		
		public void updateLong(String columnName, long x) {
			throw new AssertionError();
		}
		
		public void updateNull(int columnIndex) {
			throw new AssertionError();
		}
		
		public void updateNull(String columnName) {
			throw new AssertionError();
		}
		
		public void updateObject(int columnIndex, Object x) {
			throw new AssertionError();
		}
		
		public void updateObject(int columnIndex, Object x, int scale) {
			throw new AssertionError();
		}
		
		public void updateObject(String columnName, Object x) {
			throw new AssertionError();
		}
		
		public void updateObject(String columnName, Object x, int scale) {
			throw new AssertionError();
		}
		
		public void updateRef(int columnIndex, Ref x) {
			throw new AssertionError();
		}
		
		public void updateRef(String columnName, Ref x) {
			throw new AssertionError();
		}
		
		public void updateRow() {
			throw new AssertionError();
		}
		
		public void updateShort(int columnIndex, short x) {
			throw new AssertionError();
		}
		
		public void updateShort(String columnName, short x) {
			throw new AssertionError();
		}
		
		public void updateString(int columnIndex, String x) {
			throw new AssertionError();
		}
		
		public void updateString(String columnName, String x) {
			throw new AssertionError();
		}
		
		public void updateTime(int columnIndex, Time x) {
			throw new AssertionError();
		}
		
		public void updateTime(String columnName, Time x) {
			throw new AssertionError();
		}
		
		public void updateTimestamp(int columnIndex, Timestamp x) {
			throw new AssertionError();
		}
		
		public void updateTimestamp(String columnName, Timestamp x) {
			throw new AssertionError();
		}
		
		public boolean wasNull() {
			throw new AssertionError();
		}
		
	}
	
}
