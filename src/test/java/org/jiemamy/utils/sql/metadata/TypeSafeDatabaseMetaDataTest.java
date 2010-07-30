/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/07/11
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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.h2.Driver;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * {@link TypeSafeDatabaseMetaData} のテストクラス。
 * 
 * @version $Id$
 * @author yamkazu
 */
public class TypeSafeDatabaseMetaDataTest {
	
	/**
	 * テストクラスの初期を行う。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@BeforeClass
	public static void beforeClass() throws Exception {
		DriverManager.registerDriver(new Driver());
	}
	
	/**
	 * H2に登録するためのテスト用プロシージャメソッドです。 
	 * 
	 * @param str ダミーの仮引数
	 * @return ダミー文字
	 */
	public static String testProcedure(String str) {
		return "";
	}
	
	private static Connection getConnection() throws Exception {
		return DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
	}
	
	private static TypeSafeDatabaseMetaData newTypeSafeDatabaseMetaData(Connection con) throws Exception {
		return new TypeSafeDatabaseMetaData(con.getMetaData());
	}
	

	private Connection con;
	
	private TypeSafeDatabaseMetaData metaData;
	

	/**
	 * テストを初期化する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Before
	public void setUp() throws Exception {
		con = getConnection();
		
		// FORMAT-OFF
		PreparedStatement stat = con.prepareStatement("CREATE TABLE DEPT (" 
				+ "ID IDENTITY NOT NULL PRIMARY KEY," 
				+ "DEPTNO INTEGER NOT NULL," 
				+ "DEPTNAME VARCHAR(20)," 
				+ "LOC VARCHAR(20), " 
				+ "VERSION_NO INTEGER);");
		// FORMAT-ON
		stat.executeUpdate();
		stat.close();
		
		// FORMAT-OFF
		stat = con.prepareStatement("CREATE TABLE EMP ("
				+ "EMPNO IDENTITY NOT NULL PRIMARY KEY,"
				+ "ENAME VARCHAR(10),"
				+ "JOB VARCHAR(9),"
				+ "MGR NUMERIC(4),"
				+ "HIREDATE DATE,"
				+ "SAL NUMERIC(7, 2),"
				+ "COMM NUMERIC(7, 2),"
				+ "DEPTNO BIGINT REFERENCES DEPT(DEPTNO));");
		// FORMAT-ON
		stat.executeUpdate();
		stat.close();
		
		stat = con.prepareStatement("CREATE USER TESTUSER PASSWORD 'passowrd';");
		stat.executeUpdate();
		stat.close();
		
		stat = con.prepareStatement("GRANT ALL ON EMP TO TESTUSER;");
		stat.executeUpdate();
		stat.close();
		
		// FORMAT-OFF
		stat = con.prepareStatement("CREATE ALIAS TESTPROCEDURE FOR \"" 
				+ TypeSafeDatabaseMetaDataTest.class.getName()
				+ ".testProcedure\";");
		// FORMAT-ON
		stat.executeUpdate();
		stat.close();
		
		metaData = newTypeSafeDatabaseMetaData(con);
	}
	
	/**
	 * テストの情報を破棄する。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@After
	public void tearDown() throws Exception {
		con.close();
	}
	
	/**
	 * TableMetaが取得できること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test01_TableMetaが取得できること() throws Exception {
		TypeSafeResultSet<TableMeta> tables = metaData.getTables(null, null, "EMP", null);
		
		if (tables.next() == false) {
			fail();
		}
		
		TableMeta tableMeta = tables.getResult();
		assertThat(tableMeta.tableName, is("EMP"));
	}
	
	/**
	 * BestRowIdentifierMetaが取得できること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test02_TypeSafeDatabaseMetaDataでBestRowIdentifierMetaが取得できること() throws Exception {
		TypeSafeResultSet<BestRowIdentifierMeta> bestRowIdentifier =
				metaData.getBestRowIdentifier(null, null, "EMP", 0, true);
		
		if (bestRowIdentifier.next() == false) {
			fail();
		}
		
		BestRowIdentifierMeta bestRowIdentifierMeta = bestRowIdentifier.getResult();
		assertThat(bestRowIdentifierMeta.scope, is(BestRowIdentifierMeta.Scope.SESSION));
		assertThat(bestRowIdentifierMeta.columnName, is("EMPNO"));
	}
	
	/**
	 * CatalogMetaが取得できること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test03_CatalogMetaが取得できること() throws Exception {
		TypeSafeResultSet<CatalogMeta> catalogs = metaData.getCatalogs();
		
		if (catalogs.next() == false) {
			fail();
		}
		CatalogMeta catalogMeta = catalogs.getResult();
		assertThat(catalogMeta.tableCat, is("TEST"));
	}
	
	/**
	 * ColumnMetaが取得できること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test04_ColumnMetaが取得できること() throws Exception {
		TypeSafeResultSet<ColumnMeta> columns = metaData.getColumns(null, null, "EMP", "EMPNO");
		
		if (columns.next() == false) {
			fail();
		}
		
		ColumnMeta columnMeta = columns.getResult();
		assertThat(columnMeta.columnName, is("EMPNO"));
		assertThat(columnMeta.isNullable, is(ColumnMeta.IsNullable.NO));
	}
	
	/**
	 * ColumnPrivilegeMetaが取得できること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test05_ColumnPrivilegeMetaが取得できること() throws Exception {
		TypeSafeResultSet<ColumnPrivilegeMeta> columnPrivileges = metaData.getColumnPrivileges(null, null, "EMP", null);
		
		if (columnPrivileges.next() == false) {
			fail();
		}
		
		ColumnPrivilegeMeta columnPrivilegeMeta = columnPrivileges.getResult();
		assertThat(columnPrivilegeMeta.tableName, is("EMP"));
		assertThat(columnPrivilegeMeta.grantee, is("TESTUSER"));
	}
	
	/**
	 * IndexInfoMetaが取得できること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test06_IndexInfoMetaが取得できること() throws Exception {
		TypeSafeResultSet<IndexInfoMeta> indexInfo = metaData.getIndexInfo(null, null, "EMP", true, true);
		
		if (indexInfo.next() == false) {
			fail();
		}
		
		IndexInfoMeta indexInfoMeta = indexInfo.getResult();
		assertThat(indexInfoMeta.tableName, is("EMP"));
		assertThat(indexInfoMeta.columnName, is("EMPNO"));
	}
	
	/**
	 * KeyMetaが取得できること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test07_KeyMetaが取得できること() throws Exception {
		TypeSafeResultSet<KeyMeta> exportedKeys = metaData.getExportedKeys(null, null, "DEPT");
		
		if (exportedKeys.next() == false) {
			fail();
		}
		
		KeyMeta exportedKeyMeta = exportedKeys.getResult();
		assertThat(exportedKeyMeta.pkTableName, is("DEPT"));
		assertThat(exportedKeyMeta.pkColumnName, is("DEPTNO"));
		
		TypeSafeResultSet<KeyMeta> importedKeys = metaData.getImportedKeys(null, null, "EMP");
		
		if (importedKeys.next() == false) {
			fail();
		}
		
		KeyMeta importedKeyMeta = importedKeys.getResult();
		assertThat(importedKeyMeta.pkTableName, is("DEPT"));
		assertThat(importedKeyMeta.pkColumnName, is("DEPTNO"));
	}
	
	/**
	 * PrimaryKeyMetaが取得できること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test08_PrimaryKeyMetaが取得できること() throws Exception {
		TypeSafeResultSet<PrimaryKeyMeta> primaryKeys = metaData.getPrimaryKeys(null, null, "EMP");
		
		if (primaryKeys.next() == false) {
			fail();
		}
		
		PrimaryKeyMeta primaryKeyMeta = primaryKeys.getResult();
		assertThat(primaryKeyMeta.tableName, is("EMP"));
		assertThat(primaryKeyMeta.columnName, is("EMPNO"));
	}
	
	/**
	 * ProcedureMetaが取得出来ること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test09_ProcedureMetaが取得出来ること() throws Exception {
		TypeSafeResultSet<ProcedureMeta> procedures = metaData.getProcedures(null, null, null);
		
		if (procedures.next() == false) {
			fail();
		}
		
		ProcedureMeta procedureMeta = procedures.getResult();
		assertThat(procedureMeta.procedureName, is("TESTPROCEDURE"));
		assertThat(procedureMeta.procedureType, is(ProcedureMeta.ProcedureType.RETURNS_RESULT));
	}
	
	/**
	 * ProcedureColumnsMetaが取得できること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test10_ProcedureColumnsMetaが取得できること() throws Exception {
		TypeSafeResultSet<ProcedureColumnsMeta> procedureColumns =
				metaData.getProcedureColumns(null, null, "TESTPROCEDURE", null);
		
		if (procedureColumns.next() == false) {
			fail();
		}
		
		ProcedureColumnsMeta procedureColumnsMeta = procedureColumns.getResult();
		assertThat(procedureColumnsMeta.procedureName, is("TESTPROCEDURE"));
	}
	
	/**
	 * SchemaMetaが取得できること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test11_SchemaMetaが取得できること() throws Exception {
		TypeSafeResultSet<SchemaMeta> schemas = metaData.getSchemas();
		
		if (schemas.next() == false) {
			fail();
		}
	}
	
	/**
	 * TablePrivilegeMetaが取得できること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test12_TablePrivilegeMetaが取得できること() throws Exception {
		TypeSafeResultSet<TablePrivilegeMeta> tablePrivileges = metaData.getTablePrivileges(null, null, "EMP");
		
		if (tablePrivileges.next() == false) {
			fail();
		}
		
		TablePrivilegeMeta tablePrivilegeMeta = tablePrivileges.getResult();
		assertThat(tablePrivilegeMeta.tableName, is("EMP"));
		assertThat(tablePrivilegeMeta.grantee, is("TESTUSER"));
	}
	
	/**
	 * TableTypeMetaが取得できること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void test13_TableTypeMetaが取得できること() throws Exception {
		TypeSafeResultSet<TableTypeMeta> tableTypes = metaData.getTableTypes();
		
		if (tableTypes.next() == false) {
			fail();
		}
	}
	
}
