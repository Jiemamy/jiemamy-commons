/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy.utils.sql.metadata;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.utils.sql.ResultSetUtil;

/**
 * {@link DatabaseMetaData#getVersionColumns(String, String, String)}の結果の一つを表す値クラス。
 * 
 * @author daisuke
 */
public class VersionColumnMeta {
	
	/** is not used */
	public final String scope;
	
	/** column name */
	public final String columnName;
	
	/** SQL data type from java.sql.Types */
	public final int dataType;
	
	/** Data source-dependent type name */
	public final String typeName;
	
	/** precision */
	public final int columnSize;
	
	/** length of column value in bytes */
	public final int bufferLength;
	
	/** scale */
	public final short decimalDigits;
	
	/** whether this is pseudo column like an Oracle ROWID */
	public final PseudoColumn pseudoColumn;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param versionColumn {@link DatabaseMetaData#getVersionColumns(String, String, String)}の結果{@link ResultSet}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public VersionColumnMeta(ResultSet versionColumn) {
		Validate.notNull(versionColumn);
		scope = ResultSetUtil.getValue(String.class, versionColumn, "SCOPE", null);
		columnName = ResultSetUtil.getValue(String.class, versionColumn, "COLUMN_NAME", null);
		dataType = ResultSetUtil.getValue(int.class, versionColumn, "DATA_TYPE", 0);
		typeName = ResultSetUtil.getValue(String.class, versionColumn, "TYPE_NAME", null);
		columnSize = ResultSetUtil.getValue(int.class, versionColumn, "COLUMN_SIZE", 0);
		bufferLength = ResultSetUtil.getValue(int.class, versionColumn, "BUFFER_LENGTH", 0);
		decimalDigits = ResultSetUtil.getValue(short.class, versionColumn, "DECIMAL_DIGITS", (short) 0);
		pseudoColumn =
				PseudoColumn.getPseudoColumn(ResultSetUtil.getValue(short.class, versionColumn, "PSEUDO_COLUMN",
						(short) 0));
		
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
		UNKNOWN(DatabaseMetaData.versionColumnUnknown),

		/** is NOT a pseudo column */
		NOT_PSEUDO(DatabaseMetaData.versionColumnNotPseudo),

		/** is a pseudo column */
		PSEUDO(DatabaseMetaData.versionColumnPseudo);
		
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
}
