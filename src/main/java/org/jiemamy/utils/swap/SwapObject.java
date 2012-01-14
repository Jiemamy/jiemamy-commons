/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2008/08/20
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

import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * ある RealObject をスワップの対象とし、管理するクラス。
 * 
 * <p>
 * スワップされたオブジェクトは{@link SwapObject#get() }で取得できる。<br>
 * 内部では RealObject への弱参照を保持し、デシリアライズ要求を受けた際、<br>
 * RealObject へ到達が可能と判断された場合は{@link Swapper }へデシリアライズ<br>
 * 要求を委譲せず、到達できた RealObject を返す。
 * </p>
 * 
 * <p>
 * また、スワップしている RealObject の内容が変わった場合、必ず{@link SwapObject#update(Serializable) }を呼ぶ必要がある。
 * </p>
 * 
 * @param <T> スワップの対象なるオブジェクトのクラス
 * @version $Id$
 * @author Keisuke.K
 */
public class SwapObject<T extends Serializable> {
	
	/** スワップファイル内でのスワップ済み RealObject の位置 */
	long position;
	
	/** スワップファイル内でのスワップ済み RealObject のバイト長 */
	int length;
	
	/** スワップする RealObject への参照 */
	Reference<T> ref;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * <p>{@link Swapper}へシリアライズ要求を行う前に、RealObjectへの弱参照を保持する。</p>
	 * 
	 * @param obj スワップの対象とするオブジェクト
	 * @throws SwapException シリアライズに失敗した場合
	 */
	public SwapObject(T obj) throws SwapException {
		ref = new WeakReference<T>(obj);
		Swapper.INSTANCE.serialize(this, obj);
	}
	
	/**
	 * スワップ済みのオブジェクトを取得する。
	 * 
	 * <p>RealObject への弱参照が到達可能な場合、到達した RealObject を返す。
	 * 到達できない場合は{@link Swapper }へ RealObject のデシリアライズ要求を委譲する。</p>
	 * 
	 * @return スワップ済みのオブジェクト
	 * @throws SwapException デシリアライズに失敗した場合
	 */
	public synchronized T get() throws SwapException {
		T obj = ref.get();
		
		if (obj == null) {
			obj = Swapper.INSTANCE.deserialize(this);
			ref = new WeakReference<T>(obj);
		}
		
		return obj;
	}
	
	/**
	 * スワップする RealObject を引数の内容で更新する。
	 * 
	 * <p>スワップしている RealObject の更新は{@link Swapper }が検知することができないため、
	 * RealObject が更新され、そのオブジェクトをスワップ情報にも更新しなければならない場合は
	 * 必ずこのメソッドを呼ぶ必要がある。</p>
	 * 
	 * @param obj 更新する RealObject
	 * @throws SwapException シリアライズに失敗した場合
	 */
	public synchronized void update(T obj) throws SwapException {
		ref = new WeakReference<T>(obj);
		Swapper.INSTANCE.reserialize(this, obj);
	}
	
}
