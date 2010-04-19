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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.Validate;

import org.jiemamy.exception.JiemamyError;

/**
 * {@link File}を扱うユーティリティ・クラス。
 * 
 * @author j5ik2o
 */
public class FileUtil {
	
	/** バッファサイズ */
	private static final int BUFF_SIZE = 1024;
	

	/**
	 * <code>src</code>の内容を<code>dest</code>にコピーします。
	 * 
	 * @param src コピー元のファイル
	 * @param dest コピー先のファイル
	 * @throws IOException 入出力に失敗した場合
	 */
	public static void copy(File src, File dest) throws IOException {
		Validate.notNull(src);
		Validate.notNull(dest);
		if (dest.canWrite() == false || (dest.exists() && dest.canWrite() == false)) {
			return;
		}
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			in = new BufferedInputStream(FileInputStreamUtil.create(src));
			out = new BufferedOutputStream(FileOutputStreamUtil.create(dest));
			byte[] buf = new byte[BUFF_SIZE];
			int length;
			while (-1 < (length = in.read(buf))) {
				out.write(buf, 0, length);
				out.flush();
			}
		} finally {
			InputStreamUtil.close(in);
			OutputStreamUtil.close(out);
		}
	}
	
	/**
	 * ファイルの内容をバイト配列に読み込む。
	 * 
	 * @param file ファイル
	 * @return ファイルの内容を読み込んだバイト配列
	 * @throws IOException 入出力が失敗した場合
	 */
	public static byte[] getBytes(File file) throws IOException {
		Validate.notNull(file);
		return InputStreamUtil.getBytes(FileInputStreamUtil.create(file));
	}
	
	/**
	 * この抽象パス名の正規の形式を返します。
	 * 
	 * @param file ファイル
	 * @return この抽象パス名と同じファイルまたはディレクトリを示す正規パス名文字列
	 * @throws IOException 入出力が失敗した場合
	 */
	public static String getCanonicalPath(File file) throws IOException {
		Validate.notNull(file);
		return file.getCanonicalPath();
	}
	
	/**
	 * この抽象パス名を<code>file:</code> URLに変換します。
	 * 
	 * @param file ファイル
	 * @return ファイルURLを表すURLオブジェクト
	 * @throws MalformedURLException 無効な書式のURLが発生した場合
	 */
	public static URL toURL(final File file) throws MalformedURLException {
		Validate.notNull(file);
		return file.toURI().toURL();
	}
	
	/**
	 * バイトの配列をファイルに書き出します。
	 * 
	 * @param path ファイルのパス
	 * @param data バイトの配列
	 * @throws IOException 入出力が失敗した場合
	 * @throws NullPointerException pathやdataがnullの場合。
	 */
	public static void write(String path, byte[] data) throws IOException {
		Validate.notNull(path);
		Validate.notNull(data);
		write(path, data, 0, data.length);
	}
	
	/**
	 * バイトの配列をファイルに書き出します。
	 * 
	 * @param path ファイルのパス
	 * @param data バイトの配列
	 * @param offset オフセット
	 * @param length 配列の長さ
	 * @throws IOException 入出力が失敗した場合
	 */
	public static void write(String path, byte[] data, int offset, int length) throws IOException {
		Validate.notNull(path);
		Validate.notNull(data);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(path));
		try {
			out.write(data, offset, length);
		} finally {
			out.close();
		}
	}
	
	private FileUtil() {
		throw new JiemamyError("不到達ポイント");
	}
}
