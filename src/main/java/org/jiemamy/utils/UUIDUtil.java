/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2008/12/16
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

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.utils.collection.CollectionsUtil;

/**
 * {@link UUID}用ユーティリティクラス。
 * 
 * @author daisuke
 */
public final class UUIDUtil {
	
	static Map<String, UUID> cache = CollectionsUtil.newHashMap();
	
	private static Logger logger = LoggerFactory.getLogger(UUIDUtil.class);
	

//	/**
//	 * テスト用に使用するUUIDをランダムで発行するための便利メソッド。
//	 * 
//	 * @param args 引数. 意味無し
//	 */
//	public static void main(String[] args) {
//		for (int i = 0; i < 5; i++) {
//			System.out.println(UUID.randomUUID());
//		}
//	}
	
	/**
	 * 文字列からUUIDを生成する。
	 * 
	 * <p>与えられた文字列表現がUUIDとして不適切であり、生成に失敗した場合は、ランダム生成を行う。</p>
	 * 
	 * <p>不適切な文字列表現からUUIDを新規生成した場合は、nameとUUIDのマッピングをキャッシュし、
	 * 後に同じnameによるUUID取得が行われた場合は、前回と同じUUIDを返す。</p>
	 * 
	 * @param name UUIDの文字列表現
	 * @return 生成したUUID
	 */
	public static UUID valueOfOrRandom(String name) {
		UUID result;
		if (name == null) {
			result = getUUID(null);
			logger.info(LogMarker.DETAIL, "Symbolic ID null is mapped to UUID '{}'", result);
		} else {
			try {
				result = UUID.fromString(name);
			} catch (IllegalArgumentException e) {
				result = getUUID(name);
				logger.info(LogMarker.DETAIL, "Symbolic ID '{}' is mapped to UUID '{}'", name, result);
			}
		}
		return result;
	}
	
	private static UUID getUUID(String name) {
		if (cache.get(name) == null) {
			cache.put(name, UUID.randomUUID());
		}
		return cache.get(name);
	}
	
	private UUIDUtil() {
	}
}
