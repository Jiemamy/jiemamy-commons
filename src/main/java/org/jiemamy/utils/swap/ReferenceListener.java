/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2008/11/16
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
package org.jiemamy.utils.swap;

import java.lang.ref.Reference;

/**
 * 参照リスナ。
 * 
 * @author Keisuke.K
 */
interface ReferenceListener {
	
	/**
	 * 参照の到達可能性が変更されたことを通知する。
	 * 
	 * @param <T> 参照先のクラス型
	 * @param ref 到達可能性が変更された参照
	 */
	<T>void modifiedReference(Reference<T> ref);
	
}
