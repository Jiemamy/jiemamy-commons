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

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import org.apache.commons.lang.Validate;

import org.jiemamy.exception.JiemamyError;

/**
 * ClassPool用のユーティリティクラス。
 * 
 * @author j5ik2o
 */
public class ClassPoolUtil {
	
	/**
	 * ClassPoolのキャッシュ。
	 */
	protected static final Map<ClassLoader, ClassPool> CLASS_POOL_MAP =
			Collections.synchronizedMap(new WeakHashMap<ClassLoader, ClassPool>());
	

	/**
	 * {@link ClassPool}から{@link CtClass}を作成する。
	 * 
	 * @param classPool クラスプール
	 * @param name クラス名
	 * @return CtClass
	 */
	public static CtClass createCtClass(ClassPool classPool, String name) {
		try {
			return createCtClass(classPool, name, Object.class);
		} catch (NotFoundException e) {
			throw new JiemamyError("不到達ポイント");
		}
	}
	
	/**
	 * {@link ClassPool}から{@link CtClass}を作成する。
	 * 
	 * @param classPool クラスプール
	 * @param name クラス名
	 * @param superClass スーパークラス
	 * @return {@link CtClass}
	 * @throws NotFoundException クラスプールにsuperClassが見つからなかった場合
	 */
	public static CtClass createCtClass(ClassPool classPool, String name, Class<?> superClass) throws NotFoundException {
		return createCtClass(classPool, name, toCtClass(classPool, superClass));
	}
	
	/**
	 * {@link ClassPool}に{@link CtClass}を作成します。
	 * 
	 * @param classPool クラスプール
	 * @param name クラス名
	 * @param superClass スーパークラス
	 * @return {@link CtClass}
	 */
	public static CtClass createCtClass(ClassPool classPool, String name, CtClass superClass) {
		return classPool.makeClass(name, superClass);
	}
	
	/**
	 * リソースを破棄します。
	 */
	public static synchronized void dispose() {
		synchronized (ClassPoolUtil.class) {
			CLASS_POOL_MAP.clear();
		}
	}
	
	/**
	 * ClassPoolを返します。
	 * 
	 * @param targetClass ターゲットクラス
	 * @return {@link ClassPool}
	 */
	public static ClassPool getClassPool(Class<?> targetClass) {
		return getClassPool(ClassLoaderUtil.getClassLoader(targetClass));
	}
	
	/**
	 * ClassPoolを返します。
	 * 
	 * @param classLoader クラスローダ
	 * @return {@link ClassPool}
	 */
	public static ClassPool getClassPool(ClassLoader classLoader) {
		Validate.notNull(classLoader);
		ClassPool classPool = CLASS_POOL_MAP.get(classLoader);
		if (classPool == null) {
			if (classLoader == null) {
				return ClassPool.getDefault();
			}
			classPool = new ClassPool();
			classPool.appendClassPath(new LoaderClassPath(classLoader));
			CLASS_POOL_MAP.put(classLoader, classPool);
		}
		return classPool;
	}
	
	/**
	 * CtClassに変換する。
	 * 
	 * @param classPool クラスプール
	 * @param clazz クラス
	 * @return {@link CtClass}
	 * @throws NotFoundException クラスプールにクラスが見つからなかった場合
	 */
	public static CtClass toCtClass(ClassPool classPool, Class<?> clazz) throws NotFoundException {
		Validate.notNull(classPool);
		Validate.notNull(clazz);
		return toCtClass(classPool, ClassUtil.getSimpleClassName(clazz));
	}
	
	/**
	 * CtClassに変換する。
	 * 
	 * @param classPool クラスプール
	 * @param className クラス名
	 * @return {@link CtClass}
	 * @throws NotFoundException クラスプールにクラスが見つからなかった場合
	 */
	public static CtClass toCtClass(ClassPool classPool, String className) throws NotFoundException {
		Validate.notNull(classPool);
		Validate.notNull(className);
		return classPool.get(className);
	}
	
	/**
	 * CtClassの配列に変換する。
	 * 
	 * @param classPool クラスプール
	 * @param classes クラス配列
	 * @return CtClassの配列
	 * @throws NotFoundException クラスプールにクラスが見つからなかった場合
	 */
	public static CtClass[] toCtClassArray(ClassPool classPool, Class<?>[] classes) throws NotFoundException {
		if (classes == null) {
			return new CtClass[0];
		}
		CtClass[] result = new CtClass[classes.length];
		for (int i = 0; i < result.length; ++i) {
			result[i] = toCtClass(classPool, classes[i]);
		}
		return result;
	}
	
	/**
	 * CtClassの配列に変換する。
	 * 
	 * @param classPool クラスプール
	 * @param classNames クラス名配列
	 * @return CtClassの配列
	 * @throws NotFoundException クラスプールにクラスが見つからなかった場合
	 */
	public static CtClass[] toCtClassArray(ClassPool classPool, String[] classNames) throws NotFoundException {
		Validate.notNull(classNames);
		if (classNames == null) {
			return new CtClass[0];
		}
		CtClass[] result = new CtClass[classNames.length];
		for (int i = 0; i < result.length; ++i) {
			result[i] = toCtClass(classPool, classNames[i]);
		}
		return result;
	}
	
	private ClassPoolUtil() {
	}
}
