/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2009/02/23
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

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.collections15.iterators.EmptyIterator;
import org.apache.commons.lang.Validate;
import org.slf4j.Marker;
import org.slf4j.helpers.BasicMarker;

/**
 * SLF4Jのログマーカーを提供する定数クラス。
 * 
 * <p>cf. <a
 * href="http://d.hatena.ne.jp/taichitaichi/20090220/1235124140">http://d.hatena.ne.jp/taichitaichi/20090220/1235124140</a></p>
 * 
 * @see BasicMarker
 * @author daisuke
 */
public enum LogMarker implements Marker, Iterable<Marker> {
	
	/** Jiemamy用 SLF4Jのルートログマーカー */
	MARKER_ROOT("org.jiemamy"),

	/** 設計判断を伴う処理に関連する項目。アドインやプラグインに関連する部分など */
	DESIGN("org.jiemamy.design"),

	/** ファイルやネットワーク、他の依存ライブラリに対するインターフェースに関連する項目 */
	BOUNDARY("org.jiemamy.boundary"),

	/** ライブラリ内に定義されているオブジェクトのライフサイクルに関連する項目 */
	LIFECYCLE("org.jiemamy.lifecycle"),

	/**
	 * 処理の中で、詳細な情報を出力する為の項目
	 * 
	 * <p>このマーカは、主にライブリやフレームワークの実装者が、バグの調査を目的として使う</p>
	 */
	DETAIL("org.jiemamy" + ".detail");
	
	private static final String OPEN = "[ ";
	
	private static final String CLOSE = " ]";
	
	private static final String SEP = ", ";
	
	static {
		for (Marker m : values()) {
			if (m == MARKER_ROOT) {
				continue;
			}
			MARKER_ROOT.add(m);
		}
	}
	
	private final String name;
	
	private List<Marker> refereceList;
	

	LogMarker(String name) {
		this.name = name;
	}
	
	public void add(Marker reference) {
		Validate.notNull(reference, "A null value cannot be added to a Marker as reference.");
		
		// no point in adding the reference multiple times
		if (this.contains(reference)) {
			return;
			
		} else if (reference.contains(this)) { // avoid recursion
			// a potential reference should not its future "parent" as a reference
			return;
		} else {
			// let's add the reference
			if (refereceList == null) {
				refereceList = new Vector<Marker>();
			}
			refereceList.add(reference);
		}
	}
	
	public boolean contains(Marker other) {
		Validate.notNull(other);
		
		if (equals(other)) {
			return true;
		}
		
		if (hasReferences()) {
			for (int i = 0; i < refereceList.size(); i++) {
				Marker ref = refereceList.get(i);
				if (ref.contains(other)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean contains(String name) {
		Validate.notNull(name);
		
		if (this.name.equals(name)) {
			return true;
		}
		
		if (hasReferences()) {
			for (int i = 0; i < refereceList.size(); i++) {
				Marker ref = refereceList.get(i);
				if (ref.contains(name)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public String getName() {
		return name;
	}
	
	@Deprecated
	public boolean hasChildren() {
		return hasReferences();
	}
	
	public boolean hasReferences() {
		return refereceList != null && refereceList.size() > 0;
	}
	
	public Iterator<Marker> iterator() {
		if (refereceList != null) {
			return refereceList.iterator();
		} else {
			return EmptyIterator.getInstance();
		}
	}
	
	public boolean remove(Marker referenceToRemove) {
		if (refereceList == null) {
			return false;
		}
		
		for (Marker m : this) {
			if (referenceToRemove.equals(m)) {
				refereceList.remove(m);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		if (hasReferences() == false) {
			return getName();
		}
		
		Iterator<Marker> it = iterator();
		Marker reference;
		StringBuffer sb = new StringBuffer(getName());
		sb.append(' ').append(OPEN);
		while (it.hasNext()) {
			reference = it.next();
			sb.append(reference.getName());
			if (it.hasNext()) {
				sb.append(SEP);
			}
		}
		sb.append(CLOSE);
		
		return sb.toString();
	}
	
}
