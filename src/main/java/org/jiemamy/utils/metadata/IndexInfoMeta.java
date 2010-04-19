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
 * {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)}の結果の一つを表す値クラス。
 * 
 * @author daisuke
 */
public class IndexInfoMeta {
	
	/** table catalog (may be null) */
	public final String tableCat;
	
	/** table schema (may be null) */
	public final String tableSchem;
	
	/** table name */
	public final String tableName;
	
	/** Can index values be non-unique. false when TYPE is tableIndexStatistic */
	public final boolean nonUnique;
	
	/** index catalog (may be null); null when TYPE is tableIndexStatistic */
	public final String indexQualifier;
	
	/** index name; null when TYPE is tableIndexStatistic */
	public final String indexName;
	
	/** index type */
	public final IndexType type;
	
	/** column sequence number within index; zero when TYPE is tableIndexStatistic */
	public final short ordinalPosition;
	
	/** column name; null when TYPE is tableIndexStatistic */
	public final String columnName;
	
	/**
	 * column sort sequence, "A" => ascending, "D" => descending,
	 * may be null if sort sequence is not supported; null when TYPE is tableIndexStatistic
	 */
	public final SortSequence ascOrDesc;
	
	/**
	 * When TYPE is tableIndexStatistic, then this is the number of rows in the table; otherwise,
	 * it is the number of unique values in the index.
	 * */
	public final int cardinality;
	
	/**
	 * When TYPE is tableIndexStatisic then this is the number of pages used for the table,
	 * otherwise it is the number of pages used for the current index.
	 */
	public final int pages;
	
	/** Filter condition, if any. (may be null) */
	public final String filterCondition;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param indexInfo {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)}の結果{@link ResultSet}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public IndexInfoMeta(ResultSet indexInfo) {
		Validate.notNull(indexInfo);
		tableCat = ResultSetUtil.getString(indexInfo, "TABLE_CAT");
		tableSchem = ResultSetUtil.getString(indexInfo, "TABLE_SCHEM");
		tableName = ResultSetUtil.getString(indexInfo, "TABLE_NAME");
		nonUnique = ResultSetUtil.getBoolean(indexInfo, "NON_UNIQUE");
		indexQualifier = ResultSetUtil.getString(indexInfo, "INDEX_QUALIFIER");
		indexName = ResultSetUtil.getString(indexInfo, "INDEX_NAME");
		type = IndexType.getIndexType(ResultSetUtil.getShort(indexInfo, "TYPE"));
		ordinalPosition = ResultSetUtil.getShort(indexInfo, "ORDINAL_POSITION");
		columnName = ResultSetUtil.getString(indexInfo, "COLUMN_NAME");
		ascOrDesc = SortSequence.getSortSequence(ResultSetUtil.getString(indexInfo, "ASC_OR_DESC"));
		cardinality = ResultSetUtil.getInt(indexInfo, "CARDINALITY");
		pages = ResultSetUtil.getInt(indexInfo, "PAGES");
		filterCondition = ResultSetUtil.getString(indexInfo, "FILTER_CONDITION");
		
		assert tableName != null;
		assert type != null;
		assert ascOrDesc != null;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	

	/**
	 * インデックスの型を表す列挙型。
	 * @author daisuke
	 */
	public static enum IndexType {
		
		/** this identifies table statistics that are returned in conjuction with a table's index descriptions */
		STATISTIC(DatabaseMetaData.tableIndexStatistic),

		/** this is a clustered index */
		CLUSTERED(DatabaseMetaData.tableIndexClustered),

		/** this is a hashed index */
		HASHED(DatabaseMetaData.tableIndexHashed),

		/** this is some other style of index */
		OTHER(DatabaseMetaData.tableIndexOther);
		
		private static IndexType getIndexType(short value) {
			for (IndexType indexType : IndexType.values()) {
				if (indexType.value == value) {
					return indexType;
				}
			}
			return null;
		}
		

		private int value;
		

		IndexType(int value) {
			this.value = value;
		}
	}
	
	/**
	 * 列ソートシーケンスを表す列挙型。
	 * @author daisuke
	 */
	public static enum SortSequence {
		
		/** ascending */
		ASC("A"),

		/** descending */
		DESC("D");
		
		private static SortSequence getSortSequence(String value) {
			for (SortSequence sortSequence : SortSequence.values()) {
				if (sortSequence.value.equals(value)) {
					return sortSequence;
				}
			}
			return null;
		}
		

		private String value;
		

		SortSequence(String value) {
			this.value = value;
		}
	}
}
