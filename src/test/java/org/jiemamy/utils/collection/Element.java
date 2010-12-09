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
package org.jiemamy.utils.collection;

import java.util.Collection;

/**
 * {@link Collection}のテスト用要素クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
class Element {
	
	public static Element of(int value) {
		return of(value, "");
	}
	
	public static Element of(int value, String mark) {
		return new Element(value, mark);
	}
	

	final int value;
	
	final String mark;
	

	Element(int value, String mark) {
		this.value = value;
		this.mark = mark;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Element other = (Element) obj;
		if (value != other.value) {
			return false;
		}
		return true;
	}
	
	public String getMark() {
		return mark;
	}
	
	public int getNum() {
		return value;
	}
	
	@Override
	public int hashCode() {
		return value;
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
