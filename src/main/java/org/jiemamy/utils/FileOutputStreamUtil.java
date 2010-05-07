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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * {@link FileOutputStream}用のユーティリティクラス。
 * 
 * @author j5ik2o
 * 
 */
public class FileOutputStreamUtil {
	
	/**
	 * {@link FileOutputStream}を作成する。
	 * 
	 * @param file ファイル
	 * @return {@link FileOutputStream}
	 * @throws FileNotFoundException ファイルは存在するが、普通のファイルではなくディレクトリである場合、
	 * ファイルは存在せず作成もできない場合、またはなんらかの理由で開くことができない場合
	 * @see FileOutputStream#FileOutputStream(File)
	 */
	public static FileOutputStream create(File file) throws FileNotFoundException {
		return new FileOutputStream(file);
	}
	
	private FileOutputStreamUtil() {
	}
}
