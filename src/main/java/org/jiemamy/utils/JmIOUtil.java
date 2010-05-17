/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2008/07/26
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;

import org.jiemamy.utils.sql.metadata.TypeSafeDatabaseMetaData.TypeSafeResultSet;

/**
 * 入出力ユーティリティクラス。
 * 
 * <p>{@link IOUtils}に対する追加分と考えるとよい。</p>
 * 
 * @author daisuke
 */
public final class JmIOUtil {
	
	/** バッファサイズ */
	private static final int BUFF_SIZE = 8192;
	

	/**
	 * 無条件にリソースを閉じる。
	 * 
	 * <p>{@link Connection#close()} と等価であるが、例外を無視する。
	 * 主に finally 句内で使われることを想定している。</p>
	 * 
	 * @param connection 閉じる対象。nullでも、既にclose済みであっても構わない
	 */
	public static void closeQuietly(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException ignore) {
				// ignore
			}
		}
	}
	
	/**
	 * 無条件にリソースを閉じる。
	 * 
	 * <p>{@link ResultSet#close()} と等価であるが、例外を無視する。
	 * 主に finally 句内で使われることを想定している。</p>
	 * 
	 * @param resultSet 閉じる対象。nullでも、既にclose済みであっても構わない
	 */
	public static void closeQuietly(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException ignore) {
				// ignore
			}
		}
	}
	
	/**
	 * 無条件にリソースを閉じる。
	 * 
	 * <p>{@link Statement#close()} と等価であるが、例外を無視する。
	 * 主に finally 句内で使われることを想定している。</p>
	 * 
	 * @param statement 閉じる対象。nullでも、既にclose済みであっても構わない
	 */
	public static void closeQuietly(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException ignore) {
				// ignore
			}
		}
	}
	
	/**
	 * 無条件にリソースを閉じる。
	 * 
	 * <p>{@link TypeSafeResultSet#close()} と等価であるが、例外を無視する。
	 * 主に finally 句内で使われることを想定している。</p>
	 * 
	 * @param columnsResult 閉じる対象。nullでも、既にclose済みであっても構わない
	 */
	public static void closeQuietly(TypeSafeResultSet<?> columnsResult) {
		if (columnsResult != null) {
			try {
				columnsResult.close();
			} catch (SQLException e) {
				// ignore
			}
		}
	}
	
	/**
	 * {@link InputStream}の内容を{@link OutputStream}にコピーする。
	 * 
	 * @param is {@link InputStream}
	 * @param os {@link OutputStream}
	 * @throws IOException 入出力が失敗した場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static void copy(InputStream is, OutputStream os) throws IOException {
		Validate.notNull(is);
		Validate.notNull(os);
		byte[] buf = new byte[BUFF_SIZE];
		int n = 0;
		while ((n = is.read(buf, 0, buf.length)) != -1) {
			os.write(buf, 0, n);
		}
	}
	
	/**
	 * {@link OutputStream}をフラッシュする。
	 * 
	 * <p>{@link OutputStream#flush()} と等価であるが、{@link IOException}を無視する。</p>
	 * 
	 * @param out {@link OutputStream}
	 */
	public static void flushQuietly(OutputStream out) {
		if (out != null) {
			try {
				out.flush();
			} catch (IOException e) {
				// ignore
			}
		}
	}
	
	/**
	 * {@link InputStream}からbyteの配列を取得する。
	 * 
	 * @param is {@link InputStream}
	 * @return byteの配列
	 * @throws IOException 入出力が失敗した場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static byte[] getBytes(InputStream is) throws IOException {
		Validate.notNull(is);
		byte[] buf = new byte[BUFF_SIZE];
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int n = 0;
			while ((n = is.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, n);
			}
			byte[] bytes = baos.toByteArray();
			return bytes;
		} finally {
			IOUtils.closeQuietly(is);
		}
	}
	
	private JmIOUtil() {
	}
}
