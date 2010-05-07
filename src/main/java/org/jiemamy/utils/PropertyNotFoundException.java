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

/**
 * プロパティが見つからない場合の例外クラス。
 * 
 * @author j5ik2o
 */
@SuppressWarnings("serial")
public class PropertyNotFoundException extends Exception {
	
	private String entityName;
	
	private String propertyName;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param entityName エンティティ名
	 * @param propertyName プロパティ名
	 */
	public PropertyNotFoundException(String entityName, String propertyName) {
		this(entityName, propertyName, null);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param entityName エンティティ名 
	 * @param propertyName プロパティ名
	 * @param cause 原因となった例外
	 */
	public PropertyNotFoundException(String entityName, String propertyName, Throwable cause) {
		super(String.format("%s.%s is not found.", entityName, propertyName), cause);
		this.entityName = entityName;
		this.propertyName = propertyName;
	}
	
	/**
	 * エンティティ名を取得する。
	 * 
	 * @return エンティティ名
	 */
	public String getEntityName() {
		return entityName;
	}
	
	/**
	 * プロパティ名を取得する。
	 * 
	 * @return プロパティ名
	 */
	public String getPropertyName() {
		return propertyName;
	}
	
}
