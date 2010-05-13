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
 * {@link DatabaseMetaData#getProcedures(String, String, String)}の結果の一つを表す値クラス。
 * 
 * @author daisuke
 */
public class ProcedureMeta {
	
	/** procedure catalog (may be null) */
	public final String procedureCat;
	
	/** procedure schema (may be null) */
	public final String procedureSchem;
	
	/** procedure name */
	public final String procedureName;
	
	/** explanatory comment on the procedure */
	public final String remarks;
	
	/** kind of procedure */
	public final ProcedureType procedureType;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param procedure {@link DatabaseMetaData#getProcedures(String, String, String)}の結果{@link ResultSet}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public ProcedureMeta(ResultSet procedure) {
		Validate.notNull(procedure);
		procedureCat = ResultSetUtil.getValue(String.class, procedure, "PROCEDURE_CAT", null);
		procedureSchem = ResultSetUtil.getValue(String.class, procedure, "PROCEDURE_SCHEM", null);
		procedureName = ResultSetUtil.getValue(String.class, procedure, "PROCEDURE_NAME", null);
		remarks = ResultSetUtil.getValue(String.class, procedure, "REMARKS", null);
		procedureType =
				ProcedureType.getProcedureType(ResultSetUtil.getValue(short.class, procedure, "PROCEDURE_TYPE",
						(short) 0));
		
		assert procedureName != null;
		assert procedureType != null;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	

	/**
	 * プロシージャの種類を表す列挙型。
	 * @author daisuke
	 */
	public static enum ProcedureType {
		
		/** May return a result */
		RESULT_UNKNOWN(DatabaseMetaData.procedureResultUnknown),

		/** Does not return a result */
		NO_RESULT(DatabaseMetaData.procedureNoResult),

		/** Returns a result */
		RETURNS_RESULT(DatabaseMetaData.procedureReturnsResult);
		
		private static ProcedureType getProcedureType(short value) {
			for (ProcedureType procedureType : ProcedureType.values()) {
				if (procedureType.value == value) {
					return procedureType;
				}
			}
			return null;
		}
		

		private int value;
		

		ProcedureType(int value) {
			this.value = value;
		}
	}
}
