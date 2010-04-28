/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/01/28
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

import org.apache.commons.lang.Validate;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * {@link NodeList}の{@link Iterable}ラッパー。
 * 
 * @author daisuke
 */
public class IterableNodeList implements Iterable<Node> {
	
	private final NodeList nodeList;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param nodeList ノードリスト
	 */
	public IterableNodeList(NodeList nodeList) {
		Validate.notNull(nodeList);
		this.nodeList = nodeList;
	}
	
	public Iterator<Node> iterator() {
		return new NodeListIterator();
	}
	

	/**
	 * {@link NodeList}の{@link Iterator}ラッパー。
	 * 
	 * @author daisuke
	 */
	public class NodeListIterator implements Iterator<Node> {
		
		private int index;
		

		public boolean hasNext() {
			return nodeList.getLength() > index;
		}
		
		public Node next() {
			return nodeList.item(index++);
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
