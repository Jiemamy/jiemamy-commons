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

import org.apache.commons.lang.Validate;

/**
 * リソースが見つからなかったときにスローされる例外クラス。
 * 
 * @version $Id$
 * @author j5ik2o
 */
@SuppressWarnings("serial")
public class ResourceNotFoundException extends Exception {
	
	private String path;
	

	/**
	 * {@link ResourceNotFoundException}を作成する。
	 * 
	 * @param path パス
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public ResourceNotFoundException(String path) {
		super(path);
		Validate.notNull(path);
		this.path = path;
	}
	
	/**
	 * パスを返す。
	 * 
	 * @return パス
	 */
	public String getPath() {
		return path;
	}
}
