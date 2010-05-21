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
 * {@link DatabaseMetaData#getExportedKeys(String, String, String)},
 * {@link DatabaseMetaData#getImportedKeys(String, String, String)}の結果の一つを表す値クラス。
 * 
 * @author daisuke
 */
public class KeyMeta {
	
	/** primary key table catalog being imported (may be null) */
	public final String pkTableCat;
	
	/** primary key table schema being imported (may be null) */
	public final String pkTableSchem;
	
	/** primary key table name being imported */
	public final String pkTableName;
	
	/** primary key column name being imported */
	public final String pkColumnName;
	
	/** foreign key table catalog (may be null) */
	public final String fkTableCat;
	
	/** foreign key table schema (may be null) */
	public final String fkTableSchem;
	
	/** foreign key table name */
	public final String fkTableName;
	
	/** foreign key column name */
	public final String fkColumnName;
	
	/** sequence number within a foreign key */
	public final short keySeq;
	
	/** What happens to a foreign key when the primary key is updated. */
	public final Rule updateRule;
	
	/** What happens to the foreign key when primary is deleted. */
	public final Rule deleteRule;
	
	/** foreign key name (may be null) */
	public final String fkName;
	
	/** primary key name (may be null) */
	public final String pkName;
	
	/** can the evaluation of foreign key */
	public final Deferrability deferrability;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param importedKey {@link DatabaseMetaData#getImportedKeys(String, String, String)}等の結果{@link ResultSet}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	KeyMeta(ResultSet importedKey) {
		Validate.notNull(importedKey);
		pkTableCat = ResultSetUtil.getValue(String.class, importedKey, "PKTABLE_CAT", null);
		pkTableSchem = ResultSetUtil.getValue(String.class, importedKey, "PKTABLE_SCHEM", null);
		pkTableName = ResultSetUtil.getValue(String.class, importedKey, "PKTABLE_NAME", null);
		pkColumnName = ResultSetUtil.getValue(String.class, importedKey, "PKCOLUMN_NAME", null);
		fkTableCat = ResultSetUtil.getValue(String.class, importedKey, "FKTABLE_CAT", null);
		fkTableSchem = ResultSetUtil.getValue(String.class, importedKey, "FKTABLE_SCHEM", null);
		fkTableName = ResultSetUtil.getValue(String.class, importedKey, "FKTABLE_NAME", null);
		fkColumnName = ResultSetUtil.getValue(String.class, importedKey, "FKCOLUMN_NAME", null);
		keySeq = ResultSetUtil.getValue(short.class, importedKey, "KEY_SEQ", (short) 0);
		updateRule = Rule.getRule(ResultSetUtil.getValue(short.class, importedKey, "UPDATE_RULE", (short) 0));
		deleteRule = Rule.getRule(ResultSetUtil.getValue(short.class, importedKey, "DELETE_RULE", (short) 0));
		fkName = ResultSetUtil.getValue(String.class, importedKey, "FK_NAME", null);
		pkName = ResultSetUtil.getValue(String.class, importedKey, "PK_NAME", null);
		deferrability =
				Deferrability.getDeferrability(ResultSetUtil.getValue(short.class, importedKey, "DEFERRABILITY",
						(short) 0));
		
		assert pkTableName != null;
		assert pkColumnName != null;
		assert fkTableName != null;
		assert fkColumnName != null;
		assert updateRule != null;
		assert deleteRule != null;
		assert deferrability != null;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	

	/**
	 * 外部キーの制限の評価はコミットまで延期できるかどうかを表す列挙型。
	 * @author daisuke
	 */
	public static enum Deferrability {
		
		/** see SQL92 for definition */
		INITIALLY_DEFERRED(DatabaseMetaData.importedKeyInitiallyDeferred),

		/** see SQL92 for definition */
		INITIALLY_IMMEDIATE(DatabaseMetaData.importedKeyInitiallyImmediate),

		/** see SQL92 for definition */
		NOT_DEFERRABLE(DatabaseMetaData.importedKeyNotDeferrable);
		
		private static Deferrability getDeferrability(int value) {
			for (Deferrability deferrability : Deferrability.values()) {
				if (deferrability.value == value) {
					return deferrability;
				}
			}
			return null;
		}
		

		private final int value;
		

		Deferrability(int value) {
			this.value = value;
		}
	}
	
	/**
	 * 主キーが更新/削除されるときに、外部キーに起こる内容を表す列挙型。
	 * @author daisuke
	 */
	public static enum Rule {
		
		/** do not allow update/delete of primary key if it has been imported */
		NO_ACTION(DatabaseMetaData.importedKeyNoAction),

		/** change imported key to agree with primary key update / delete rows that import a deleted key */
		CASCADE(DatabaseMetaData.importedKeyCascade),

		/** change imported key to NULL if its primary key has been updated/deleted */
		SET_NULL(DatabaseMetaData.importedKeySetNull),

		/** change imported key to default values if its primary key has been updated/deleted */
		SET_DEFAULT(DatabaseMetaData.importedKeySetDefault),

		/** same as importedKeyNoAction (for ODBC 2.x compatibility) */
		RESTRICT(DatabaseMetaData.importedKeyRestrict);
		
		private static Rule getRule(short value) {
			for (Rule rule : Rule.values()) {
				if (rule.value == value) {
					return rule;
				}
			}
			return null;
		}
		

		private final int value;
		

		Rule(int value) {
			this.value = value;
		}
	}
	
}
