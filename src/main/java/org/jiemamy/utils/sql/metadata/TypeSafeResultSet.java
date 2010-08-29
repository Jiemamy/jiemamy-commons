package org.jiemamy.utils.sql.metadata;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyError;

/**
 * {@link ResultSet}が持つ複数の結果を、それぞれタイプセーフに扱うためのラッパークラス。
 * 
 * @param <T> 結果1つを表す型
 * @author daisuke
 */
class TypeSafeResultSet<T> {
	
	private final ResultSet resultSet;
	
	private final Constructor<T> constructor;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * <p>{@code resultClass}は、引数に{@link ResultSet}を1つだけ受け取るコンストラクタを持っていて、
	 * かつ、abstractではないクラス（非インターフェイス）でなければならない。</p>
	 * 
	 * @param resultSet 読み込み対象の {@link ResultSet}
	 * @param resultClass 結果1つを表す型
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws IllegalArgumentException 引数{@code resultClass}が上記要件を満たしていない場合
	 */
	public TypeSafeResultSet(ResultSet resultSet, Class<T> resultClass) {
		Validate.notNull(resultSet);
		Validate.notNull(resultClass);
		this.resultSet = resultSet;
		try {
			constructor = resultClass.getConstructor(ResultSet.class);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("resultClass must have ResultSet constructor.", e);
		}
	}
	
	/**
	 * Releases this {@code ResultSet} object's database and
	 * JDBC resources immediately instead of waiting for
	 * this to happen when it is automatically closed.
	 * 
	 * @throws SQLException if a database access error occurs
	 */
	public void close() throws SQLException {
		resultSet.close();
	}
	
	/**
	 * 現在のカーソル位置の結果を取得する。
	 * 
	 * @return 現在のカーソル位置の結果
	 * @throws SQLException SQLの実行に失敗した場合。
	 */
	public T getResult() throws SQLException {
		try {
			return constructor.newInstance(resultSet);
		} catch (IllegalArgumentException e) {
			throw new JiemamyError("Coding miss.", e);
		} catch (InstantiationException e) {
			throw new JiemamyError("resultClass must not be abstract class.", e);
		} catch (IllegalAccessException e) {
			throw new JiemamyError("resultClass must have public constructor.", e);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof SQLException) {
				throw (SQLException) cause;
			}
			throw new JiemamyError("resultClass construction failed.", e);
		}
	}
	
	/**
	 * Moves the cursor down one row from its current position.
	 * A {@code ResultSet} cursor is initially positioned
	 * before the first row; the first call to the method
	 * {@code next} makes the first row the current row; the
	 * second call makes the second row the current row, and so on. 
	 *
	 * <P>If an input stream is open for the current row, a call
	 * to the method {@code next} will
	 * implicitly close it. A {@code ResultSet} object's
	 * warning chain is cleared when a new row is read.
	 *
	 * @return {@code true} if the new current row is valid; 
	 * {@code false} if there are no more rows 
	 * @exception SQLException if a database access error occurs
	 */
	public boolean next() throws SQLException {
		return resultSet.next();
	}
}
