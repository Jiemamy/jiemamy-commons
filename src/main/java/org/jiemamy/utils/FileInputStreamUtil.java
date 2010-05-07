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
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * {@link FileInputStream}用のユーティリティクラス。
 * 
 * @author j5ik2o
 */
public class FileInputStreamUtil {
	
	/**
	 * {@link FileInputStream}を作成する。
	 * 
	 * @param file ファイル
	 * @return {@link FileInputStream}
	 * @throws FileNotFoundException ファイルが存在しないか、
	 * 普通のファイルではなくディレクトリであるか、またはなんらかの理由で開くことができない場合
	 * @see FileInputStream#FileInputStream(File)
	 */
	public static FileInputStream create(File file) throws FileNotFoundException {
		return new FileInputStream(file);
	}
	
	private FileInputStreamUtil() {
	}
}
