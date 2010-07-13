/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2008/06/25
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
package org.jiemamy.utils.visitor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.Validate;

import org.jiemamy.utils.sql.TypeSafeResultSet;

/**
 * {@link Collection}や{@link Map}等、複数の要素を持つObjectに対して、全ての要素に処理を行うためのユーティリティ。
 * 
 * @author daisuke
 */
public final class ForEachUtil {
	
	/**
	 * {@link Collection}を処理するビジターアクセプタメソッド。
	 * 
	 * @param <T> Collectionが持つオブジェクトの型
	 * @param <R> 戻り値の型
	 * @param <X> visitメソッドが投げる可能性のある例外
	 * @param target 処理対象コレクション
	 * @param visitor ビジター
	 * @return accept結果
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static <T, R, X extends Exception>R accept(Iterable<T> target, CollectionVisitor<T, R, X> visitor) throws X {
		Validate.notNull(target);
		Validate.notNull(visitor);
		
		for (T element : target) {
			R result = visitor.visit(element);
			if (result != null) {
				return result;
			}
		}
		
		return visitor.getFinalResult();
	}
	
	/**
	 * {@link Map}を処理するビジターアクセプタメソッド。
	 * 
	 * @param <K> Mapのキーの型
	 * @param <V> Mapの値の型
	 * @param <R> 戻り値の型
	 * @param <X> visitメソッドが投げる可能性のある例外
	 * @param targetMap 処理対象コレクション
	 * @param visitor ビジター
	 * @return accept結果
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static <K, V, R, X extends Exception>R accept(Map<K, V> targetMap, MapVisitor<K, V, R, X> visitor) throws X {
		Validate.notNull(targetMap);
		Validate.notNull(visitor);
		
		for (Map.Entry<K, V> entry : targetMap.entrySet()) {
			R result = visitor.visit(entry.getKey(), entry.getValue());
			if (result != null) {
				return result;
			}
		}
		return visitor.getFinalResult();
	}
	
	/**
	 * {@link ResultSet}を処理するビジターアクセプタメソッド。
	 * 
	 * @param <R> 戻り値の型
	 * @param <X> visitメソッドが投げる可能性のある例外
	 * @param target 処理対象ResultSet
	 * @param visitor ビジター
	 * @return accept結果
	 * @throws SQLException SQLの実行に失敗した場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static <R, X extends Exception>R accept(ResultSet target, ResultSetVisitor<ResultSet, R, X> visitor)
			throws SQLException, X { // CHECKSTYLE IGNORE THIS LINE
		Validate.notNull(target);
		Validate.notNull(visitor);
		
		while (target.next()) {
			visitor.visit(target);
		}
		return visitor.getFinalResult();
	}
	
	/**
	 * 配列を処理するビジターアクセプタメソッド。
	 * 
	 * @param <T> 配列が持つオブジェクトの型
	 * @param <R> 戻り値の型
	 * @param <X> visitメソッドが投げる可能性のある例外
	 * @param target 処理対象コレクション
	 * @param visitor ビジター
	 * @return accept結果
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static <T, R, X extends Exception>R accept(T[] target, CollectionVisitor<T, R, X> visitor) throws X {
		Validate.notNull(target);
		Validate.notNull(visitor);
		
		for (T element : target) {
			R result = visitor.visit(element);
			if (result != null) {
				return result;
			}
		}
		
		return visitor.getFinalResult();
	}
	
	/**
	 * {@link TypeSafeResultSet}を処理するビジターアクセプタメソッド。
	 * 
	 * @param <T> {@link TypeSafeResultSet}が返す型
	 * @param <R> 戻り値の型
	 * @param <X> visitメソッドが投げる可能性のある例外
	 * @param target 処理対象TypeSafeResultSet
	 * @param visitor ビジター
	 * @return accept結果
	 * @throws SQLException SQLの実行に失敗した場合
	 * @throws X ビジターにより指定された例外がスローされた場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public static <T, R, X extends Exception>R accept(TypeSafeResultSet<T> target,
			TypeSafeResultSetVisitor<T, R, X> visitor) throws SQLException, X { // CHECKSTYLE IGNORE THIS LINE
		Validate.notNull(target);
		Validate.notNull(visitor);
		
		while (target.next()) {
			visitor.visit(target.getResult());
		}
		return visitor.getFinalResult();
	}
	
	private ForEachUtil() {
	}
	

	/**
	 * {@link Collection}に対するビジター。
	 * 
	 * @param <T> {@link Collection}が保持する型
	 * @param <R> acceptが返すべき戻り値の型
	 * @param <X> visitメソッドが投げる可能性のある例外
	 * @author daisuke
	 */
	public static interface CollectionVisitor<T, R, X extends Exception> {
		
		/**
		 * ループが終了した後、acceptが返すべき戻り値を取得する。
		 * 
		 * @return ループが終了した後、acceptが返すべき戻り値
		 */
		R getFinalResult();
		
		/**
		 * 処理内容を記述するメソッド。
		 * 
		 * @param element 処理対象要素
		 * @return 引き続きacceptを継続する場合は{@code null}、ループを中断して終了する場合 {@link ForEachUtil#accept}が返すべき戻り値を返す。
		 * @throws X ビジタが指定した例外が発生した場合
		 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
		 */
		R visit(T element) throws X;
	}
	
	/**
	 * {@link Map}に対するビジター。
	 * 
	 * @param <K> 処理対象{@link Map}のキーの型
	 * @param <V> 処理対象{@link Map}の値の型
	 * @param <R> forEachが返すべき戻り値の型
	 * @param <X> visitメソッドが投げる可能性のある例外
	 * @author daisuke
	 */
	public static interface MapVisitor<K, V, R, X extends Exception> {
		
		/**
		 * ループが終了した後、acceptが返すべき戻り値を取得する。
		 * 
		 * @return ループが終了した後、acceptが返すべき戻り値
		 */
		R getFinalResult();
		
		/**
		 * 処理内容を記述するメソッド。
		 * 
		 * @param key 処理対象のキー
		 * @param value 処理対象の値
		 * @return 引き続きacceptを継続する場合null、ループを終了する場合acceptが返すべき戻り値を返す。
		 * @throws X ビジタが指定した例外が発生した場合
		 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
		 */
		R visit(K key, V value) throws X;
	}
	
	/**
	 * {@link ResultSet}に対するビジター。
	 * 
	 * @param <T> ビジターが受け取る要素の型
	 * @param <R> forEachが返すべき戻り値の型
	 * @param <X> visitメソッドが投げる可能性のある例外
	 * @author daisuke
	 */
	public static interface ResultSetVisitor<T, R, X extends Exception> {
		
		/**
		 * ループが終了した後、acceptが返すべき戻り値を取得する。
		 * 
		 * @return ループが終了した後、acceptが返すべき戻り値
		 */
		R getFinalResult();
		
		/**
		 * 処理内容を記述するメソッド。
		 * 
		 * @param element 処理対象要素
		 * @return 引き続きacceptを継続する場合null、ループを終了する場合acceptが返すべき戻り値を返す。
		 * @throws X ビジタが指定した例外が発生した場合
		 * @throws SQLException SQLの実行に失敗した場合
		 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
		 */
		R visit(T element) throws SQLException, X;
	}
	
	/**
	 * {@link TypeSafeResultSet}に対するビジター。
	 * 
	 * @param <T> ビジターが受け取る要素の型
	 * @param <R> forEachが返すべき戻り値の型
	 * @param <X> visitメソッドが投げる可能性のある例外
	 * @author daisuke
	 */
	public static interface TypeSafeResultSetVisitor<T, R, X extends Exception> {
		
		/**
		 * ループが終了した後、acceptが返すべき戻り値を取得する。
		 * 
		 * @return ループが終了した後、acceptが返すべき戻り値
		 */
		R getFinalResult();
		
		/**
		 * 処理内容を記述するメソッド。
		 * 
		 * @param element 処理対象要素
		 * @return 引き続きacceptを継続する場合null、ループを終了する場合acceptが返すべき戻り値を返す。
		 * @throws X ビジタが指定した例外が発生した場合
		 * @throws SQLException SQLの実行に失敗した場合
		 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
		 */
		R visit(T element) throws SQLException, X;
	}
}
