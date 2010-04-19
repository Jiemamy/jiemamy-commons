/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2008/12/30
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
package org.jiemamy.utils.metadata;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.utils.ResultSetUtil;

/**
 * 行を一意に識別するテーブルの最適な列セットに関する記述。
 * 
 * <p>{@link DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)}の結果の一つを表す値クラス。</p>
 * 
 * @author daisuke
 */
public class BestRowIdentifierMeta {
	
	/** actual scope of result */
	public final Scope scope;
	
	/** column name */
	public final String columnName;
	
	/** SQL data type from java.sql.Types */
	public final int dataType;
	
	/** Data source dependent type name, for a UDT the type name is fully qualified */
	public final String typeName;
	
	/** precision */
	public final int columnSize;
	
	/** not used */
	public final int bufferLength;
	
	/** scale */
	public final short decimalDigits;
	
	/** is this a pseudo column like an Oracle ROWID */
	public final PseudoColumn pseudoColumn;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param bestRowIdentifier {@link DatabaseMetaData#getBestRowIdentifier(String, String, String, int, boolean)}の
	 * 		結果{@link ResultSet}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public BestRowIdentifierMeta(ResultSet bestRowIdentifier) {
		Validate.notNull(bestRowIdentifier);
		scope = Scope.getScope(ResultSetUtil.getShort(bestRowIdentifier, "SCOPE"));
		columnName = ResultSetUtil.getString(bestRowIdentifier, "COLUMN_NAME");
		dataType = ResultSetUtil.getInt(bestRowIdentifier, "DATA_TYPE");
		typeName = ResultSetUtil.getString(bestRowIdentifier, "TYPE_NAME");
		columnSize = ResultSetUtil.getInt(bestRowIdentifier, "COLUMN_SIZE");
		bufferLength = ResultSetUtil.getInt(bestRowIdentifier, "BUFFER_LENGTH");
		decimalDigits = ResultSetUtil.getShort(bestRowIdentifier, "DECIMAL_DIGITS");
		pseudoColumn = PseudoColumn.getPseudoColumn(ResultSetUtil.getShort(bestRowIdentifier, "PSEUDO_COLUMN"));
		
		assert scope != null;
		assert columnName != null;
		assert typeName != null;
		assert pseudoColumn != null;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	

	/**
	 * 疑似列であるかどうかを示す列挙型。
	 * @author daisuke
	 */
	public static enum PseudoColumn {
		
		/** may or may not be pseudo column */
		UNKNOWN(DatabaseMetaData.bestRowUnknown),

		/** is NOT a pseudo column */
		NOT_PSEUDO(DatabaseMetaData.bestRowNotPseudo),

		/** is a pseudo column */
		PSEUDO(DatabaseMetaData.bestRowPseudo);
		
		private static PseudoColumn getPseudoColumn(short value) {
			for (PseudoColumn pseudoColumn : PseudoColumn.values()) {
				if (pseudoColumn.value == value) {
					return pseudoColumn;
				}
			}
			return null;
		}
		

		private int value;
		

		PseudoColumn(int value) {
			this.value = value;
		}
	}
	
	/**
	 * 結果の実際のスケール？（何のこっちゃ）
	 * @author daisuke
	 */
	public static enum Scope {
		
		/** very temporary, while using row */
		TEMPORARY(DatabaseMetaData.bestRowTemporary),

		/** valid for remainder of current transaction */
		TRANSACTION(DatabaseMetaData.bestRowTransaction),

		/** valid for remainder of current session */
		SESSION(DatabaseMetaData.bestRowSession);
		
		private static Scope getScope(short value) {
			for (Scope scope : Scope.values()) {
				if (scope.value == value) {
					return scope;
				}
			}
			return null;
		}
		

		private int value;
		

		Scope(int value) {
			this.value = value;
		}
	}
	
}
