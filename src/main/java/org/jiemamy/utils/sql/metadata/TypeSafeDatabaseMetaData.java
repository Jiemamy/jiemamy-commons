/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2008/12/23
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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.Validate;

import org.jiemamy.utils.sql.TypeSafeResultSet;


/**
 * {@link DatabaseMetaData}の情報をタイプセーフに取り扱うためのラッパークラス。
 * 
 * <p>クエリに対して返すインスタンスは、全てimmutableである。</p>
 * 
 * @author daisuke
 */
public class TypeSafeDatabaseMetaData {
	
	private final DatabaseMetaData meta;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param connection 取り扱うDBへのコネクション
	 * @throws SQLException SQLの実行に失敗した場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public TypeSafeDatabaseMetaData(Connection connection) throws SQLException {
		Validate.notNull(connection);
		meta = connection.getMetaData();
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param meta 取り扱う{@link DatabaseMetaData}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public TypeSafeDatabaseMetaData(DatabaseMetaData meta) {
		Validate.notNull(meta);
		this.meta = meta;
	}
	
	/**
	 * 指定されたスキーマおよびカタログで使用可能なユーザ定義の型 (UDT) のための指定された型の指定された属性に関する記述を取得する。
	 * 
	 * @param catalog カタログ名。データベースに格納されたカタログ名と一致しなければならない。"" はカタログなしでカタログ名を検索する。null は、カタログ名を検索の限定に使用してはならないことを意味する
	 * @param schemaPattern スキーマ名パターン。データベースに格納されたスキーマ名と一致しなければならない。"" はスキーマなしでスキーマ名を検索する。null は、スキーマ名を検索の限定に使用してはならないことを意味する
	 * @param typeNamePattern 型名パターン。データベースに格納された型名と一致しなければならない
	 * @param attributeNamePattern 属性名パターン。データベースで宣言された属性名と一致しなければならない 
	 * @return 属性情報
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	public TypeSafeResultSet<AttributesMeta> getAttributes(String catalog, String schemaPattern,
			String typeNamePattern, String attributeNamePattern) throws SQLException {
		ResultSet attributes = meta.getAttributes(catalog, schemaPattern, typeNamePattern, attributeNamePattern);
		return new TypeSafeResultSet<AttributesMeta>(attributes, AttributesMeta.class);
	}
	
	/**
	 * 行を一意に識別するテーブルの最適な列セットに関する記述を取得する。
	 * 
	 * @param catalog カタログ名。データベースに格納されたカタログ名と一致しなければならない。"" はカタログなしでカタログ名を検索する。null は、カタログ名を検索の限定に使用してはならないことを意味する
	 * @param schema スキーマ名。データベースに格納されたスキーマ名と一致しなければならない。"" はスキーマなしでスキーマ名を検索する。null は、スキーマ名を検索の限定に使用してはならないことを意味する
	 * @param table テーブル名。データベースに格納されたテーブル名と一致しなければならない
	 * @param scope 対象のスケール。SCOPE と同じ値を使用する
	 * @param nullable null 値を許す列を含む 
	 * @return 識別子情報
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	public TypeSafeResultSet<BestRowIdentifierMeta> getBestRowIdentifier(String catalog, String schema, String table,
			int scope, boolean nullable) throws SQLException {
		ResultSet bestRowIdentifier = meta.getBestRowIdentifier(catalog, schema, table, scope, nullable);
		return new TypeSafeResultSet<BestRowIdentifierMeta>(bestRowIdentifier, BestRowIdentifierMeta.class);
	}
	
	/**
	 * このデータベースで使用可能なカタログ名を取得する。
	 * 
	 * @return カタログ名情報
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	public TypeSafeResultSet<CatalogMeta> getCatalogs() throws SQLException {
		ResultSet catalogs = meta.getCatalogs();
		return new TypeSafeResultSet<CatalogMeta>(catalogs, CatalogMeta.class);
	}
	
	/**
	 * テーブルの列へのアクセス権に関する記述を取得する。
	 * 
	 * @param catalog カタログ名。データベースに格納されたカタログ名と一致しなければならない。"" はカタログなしでカタログ名を検索する。null は、カタログ名を検索の限定に使用してはならないことを意味する 
	 * @param schema スキーマ名。データベースに格納されたスキーマ名と一致しなければならない。"" はスキーマなしでスキーマ名を検索する。null は、スキーマ名を検索の限定に使用してはならないことを意味する
	 * @param table テーブル名。データベースに格納されたテーブル名と一致しなければならない
	 * @param columnNamePattern 列名パターン。データベースに格納された列名と一致しなければならない 
	 * @return アクセス権情報
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	public TypeSafeResultSet<ColumnPrivilegeMeta> getColumnPrivileges(String catalog, String schema, String table,
			String columnNamePattern) throws SQLException {
		ResultSet columnPrivileges = meta.getColumnPrivileges(catalog, schema, table, columnNamePattern);
		return new TypeSafeResultSet<ColumnPrivilegeMeta>(columnPrivileges, ColumnPrivilegeMeta.class);
	}
	
	/**
	 * 指定されたカタログで使用可能なテーブル列の記述を取得する。
	 * 
	 * @param catalog カタログ名。データベースに格納されたカタログ名と一致しなければならない。"" はカタログなしでカタログ名を検索する。null は、カタログ名を検索の限定に使用してはならないことを意味する 
	 * @param schemaPattern スキーマ名パターン。データベースに格納されたスキーマ名と一致しなければならない。"" はスキーマなしでスキーマ名を検索する。null は、スキーマ名を検索の限定に使用してはならないことを意味する
	 * @param tableNamePattern テーブル名パターン。データベースに格納されたテーブル名と一致しなければならない  
	 * @param columnNamePattern 列名パターン。データベースに格納された列名と一致しなければならない
	 * @return カラム情報
	 * @throws SQLException SQLの実行に失敗した場合
	 * @see DatabaseMetaData#getColumns(String, String, String, String)
	 */
	public TypeSafeResultSet<ColumnMeta> getColumns(String catalog, String schemaPattern, String tableNamePattern,
			String columnNamePattern) throws SQLException {
		ResultSet tables = meta.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
		return new TypeSafeResultSet<ColumnMeta>(tables, ColumnMeta.class);
	}
	
	/**
	 * 主キーテーブルの主キー列を参照する外部のキーテーブル中の、外部のキー列に関する記述
	 * (テーブルが別のキーをインポートする方法を記述)を取得する。
	 * 
	 * @param primaryCatalog カタログ名。データベースに格納されたカタログ名と一致しなければならない。"" はカタログなしでカタログ名を検索する。null は、選択条件からカタログ名を除外することを意味する
	 * @param primarySchema スキーマ名。データベースに格納されたスキーマ名と一致しなければならない。"" はスキーマなしでスキーマ名を検索する。null は、選択条件からスキーマ名を除外することを意味する
	 * @param primaryTable キーをエクスポートするテーブル名。データベースに格納されたテーブル名と一致しなければならない
	 * @param foreignCatalog カタログ名。データベースに格納されたカタログ名と一致しなければならない。"" はカタログなしでカタログ名を検索する。null は、選択条件からカタログ名を除外することを意味する
	 * @param foreignSchema スキーマ名。データベースに格納されたスキーマ名と一致しなければならない。"" はスキーマなしでスキーマ名を検索する。null は、選択条件からスキーマ名を除外することを意味する
	 * @param foreignTable キーをインポートするテーブル名。データベースに格納されたテーブル名と一致しなければならない 
	 * @return キー情報
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	public TypeSafeResultSet<KeyMeta> getCrossReference(String primaryCatalog, String primarySchema,
			String primaryTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
		ResultSet crossReference =
				meta.getCrossReference(primaryCatalog, primarySchema, primaryTable, foreignCatalog, foreignSchema,
						foreignTable);
		return new TypeSafeResultSet<KeyMeta>(crossReference, KeyMeta.class);
	}
	
	/**
	 * テーブルの主キー列 (テーブルによってエクスポートされた外部キー) を参照する外部キー列に関する記述を取得する。
	 * 
	 * @param catalog -カタログ名。このデータベースに格納されたカタログ名と一致しなければならない。"" はカタログなしでカタログ名を検索する。null は、カタログ名を検索の限定に使用してはならないことを意味する 
	 * @param schema スキーマ名。データベースに格納されたスキーマ名と一致しなければならない。"" はスキーマなしでスキーマ名を検索する。null は、スキーマ名を検索の限定に使用してはならないことを意味する
	 * @param table テーブル名。このデータベースに格納されたテーブル名と一致しなければならない 
	 * @return キー情報
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	public TypeSafeResultSet<KeyMeta> getExportedKeys(String catalog, String schema, String table) throws SQLException {
		ResultSet exportedKeys = meta.getExportedKeys(catalog, schema, table);
		return new TypeSafeResultSet<KeyMeta>(exportedKeys, KeyMeta.class);
	}
	
	/**
	 * テーブルの外部キー列 (テーブルによってインポートされる主キー) を参照する主キー列に関する記述を取得する。
	 * 
	 * @param catalog カタログ名。データベースに格納されたカタログ名と一致しなければならない。"" はカタログなしでカタログ名を検索する。null は、カタログ名を検索の限定に使用してはならないことを意味する 
	 * @param schema スキーマ名。データベースに格納されたスキーマ名と一致しなければならない。"" はスキーマなしでスキーマ名を検索する。null は、スキーマ名を検索の限定に使用してはならないことを意味する
	 * @param table テーブル名。データベースに格納されたテーブル名と一致しなければならない 
	 * @return キー情報
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	public TypeSafeResultSet<KeyMeta> getImportedKeys(String catalog, String schema, String table) throws SQLException {
		ResultSet importedKeys = meta.getImportedKeys(catalog, schema, table);
		return new TypeSafeResultSet<KeyMeta>(importedKeys, KeyMeta.class);
	}
	
	/**
	 * テーブルのインデックスと統計情報に関する記述を取得する。
	 * 
	 * @param catalog カタログ名。このデータベースに格納されたカタログ名と一致しなければならない。"" はカタログなしでカタログ名を検索する。null は、カタログ名を検索の限定に使用してはならないことを意味する 
	 * @param schema スキーマ名。このデータベースに格納されたスキーマ名と一致しなければならない。"" はスキーマなしでスキーマ名を検索する。null は、スキーマ名を検索の限定に使用してはならないことを意味する
	 * @param table テーブル名。このデータベースに格納されたテーブル名と一致しなければならない
	 * @param unique true の場合は、一意の値のインデックスだけを返す。false の場合は、一意であるかどうかにかかわらずインデックスを返す
	 * @param approximate true の場合は、結果は概数またはデータ値から外れることもある。false の場合は、正確であることが要求される 
	 * @return インデックス情報
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	public TypeSafeResultSet<IndexInfoMeta> getIndexInfo(String catalog, String schema, String table, boolean unique,
			boolean approximate) throws SQLException {
		ResultSet indexInfo = meta.getIndexInfo(catalog, schema, table, unique, approximate);
		return new TypeSafeResultSet<IndexInfoMeta>(indexInfo, IndexInfoMeta.class);
	}
	
	/**
	 * テーブルの主キー列の記述を取得する。
	 * 
	 * @param catalog カタログ名。データベースに格納されたカタログ名と一致しなければならない。"" はカタログなしでカタログ名を検索する。null は、カタログ名を検索の限定に使用してはならないことを意味する
	 * @param schema スキーマ名。データベースに格納されたスキーマ名と一致しなければならない。"" はスキーマなしでスキーマ名を検索する。null は、スキーマ名を検索の限定に使用してはならないことを意味する
	 * @param table テーブル名。データベースに格納されたテーブル名と一致しなければならない 
	 * @return キー情報
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	public TypeSafeResultSet<PrimaryKeyMeta> getPrimaryKeys(String catalog, String schema, String table)
			throws SQLException {
		ResultSet primaryKeys = meta.getPrimaryKeys(catalog, schema, table);
		return new TypeSafeResultSet<PrimaryKeyMeta>(primaryKeys, PrimaryKeyMeta.class);
	}
	
	/**
	 * カタログのストアドプロシージャパラメータと結果列に関する記述を取得する。
	 * 
	 * @param catalog カタログ名。データベースに格納されたカタログ名と一致しなければならない。"" はカタログなしでカタログ名を検索する。null は、カタログ名を検索の限定に使用してはならないことを意味する
	 * @param schemaPattern スキーマ名パターン。データベースに格納されたスキーマ名と一致しなければならない。"" はスキーマなしでスキーマ名を検索する。null は、スキーマ名を検索の限定に使用してはならないことを意味する
	 * @param procedureNamePattern プロシージャ名パターン。データベースに格納されたプロシージャ名と一致しなければならない
	 * @param columnNamePattern 列名パターン。データベースに格納された列名と一致しなければならない 
	 * @return ストアドプロシージャ情報
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	public TypeSafeResultSet<ProcedureColumnsMeta> getProcedureColumns(String catalog, String schemaPattern,
			String procedureNamePattern, String columnNamePattern) throws SQLException {
		ResultSet procedureColumns =
				meta.getProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern);
		return new TypeSafeResultSet<ProcedureColumnsMeta>(procedureColumns, ProcedureColumnsMeta.class);
	}
	
	/**
	 * カタログで使用可能なストアドプロシージャに関する記述を取得する。
	 * 
	 * @param catalog カタログ名。データベースに格納されたカタログ名と一致しなければならない。"" はカタログなしでカタログ名を検索する。null は、カタログ名を検索の限定に使用してはならないことを意味する
	 * @param schemaPattern スキーマ名パターン。データベースに格納されたスキーマ名と一致しなければならない。"" はスキーマなしでスキーマ名を検索する。null は、スキーマ名を検索の限定に使用してはならないことを意味する
	 * @param procedureNamePattern プロシージャ名パターン。データベースに格納されたプロシージャ名と一致しなければならない 
	 * @return ストアドプロシージャ情報
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	public TypeSafeResultSet<ProcedureMeta> getProcedures(String catalog, String schemaPattern,
			String procedureNamePattern) throws SQLException {
		ResultSet procedures = meta.getProcedures(catalog, schemaPattern, procedureNamePattern);
		return new TypeSafeResultSet<ProcedureMeta>(procedures, ProcedureMeta.class);
	}
	
	/**
	 * このデータベースで使用可能なスキーマ名を取得する。
	 * 
	 * @return スキーマ名情報
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	public TypeSafeResultSet<SchemaMeta> getSchemas() throws SQLException {
		ResultSet schemas = meta.getSchemas();
		return new TypeSafeResultSet<SchemaMeta>(schemas, SchemaMeta.class);
	}
	
	/**
	 * このデータベースの特定のスキーマで定義されているテーブル階層の説明を取得する。
	 * 
	 * @param catalog カタログ名。"" はカタログなしでカタログ名を検索する。null は、選択条件からカタログ名を除外することを意味する
	 * @param schemaPattern スキーマ名パターン。"" はスキーマなしでスキーマ名を検索する
	 * @param tableNamePattern テーブル名パターン。完全指定名の可能性がある 
	 * @return テーブル階層情報
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	public TypeSafeResultSet<SuperTableMeta> getSuperTables(String catalog, String schemaPattern,
			String tableNamePattern) throws SQLException {
		ResultSet superTables = meta.getSuperTables(catalog, schemaPattern, tableNamePattern);
		return new TypeSafeResultSet<SuperTableMeta>(superTables, SuperTableMeta.class);
	}
	
	/**
	 * このデータベースの特定のスキーマで定義されているユーザ定義型 (UDT) 階層の説明を取得する。
	 * 
	 * @param catalog カタログ名。"" はカタログなしでカタログ名を検索する。null は、選択条件からカタログ名を除外することを意味する
	 * @param schemaPattern スキーマ名パターン。"" はスキーマなしでスキーマ名を検索する
	 * @param typeNamePattern UDT 名パターン。完全指定名の可能性がある 
	 * @return UDT階層情報
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	public TypeSafeResultSet<SuperTypeMeta> getSuperTypes(String catalog, String schemaPattern, String typeNamePattern)
			throws SQLException {
		ResultSet superTypes = meta.getSuperTypes(catalog, schemaPattern, typeNamePattern);
		return new TypeSafeResultSet<SuperTypeMeta>(superTypes, SuperTypeMeta.class);
	}
	
	/**
	 * カタログで使用可能な各テーブルに対するアクセス権に関する記述を取得する。
	 * 
	 * @param catalog カタログ名。データベースに格納されたカタログ名と一致しなければならない。"" はカタログなしでカタログ名を検索する。null は、カタログ名を検索の限定に使用してはならないことを意味する
	 * @param schemaPattern スキーマ名パターン。データベースに格納されたスキーマ名と一致しなければならない。"" はスキーマなしでスキーマ名を検索する。null は、スキーマ名を検索の限定に使用してはならないことを意味する
	 * @param tableNamePattern テーブル名パターン。データベースに格納されたテーブル名と一致しなければならない 
	 * @return アクセス権情報
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	public TypeSafeResultSet<TablePrivilegeMeta> getTablePrivileges(String catalog, String schemaPattern,
			String tableNamePattern) throws SQLException {
		ResultSet tablePrivileges = meta.getTablePrivileges(catalog, schemaPattern, tableNamePattern);
		return new TypeSafeResultSet<TablePrivilegeMeta>(tablePrivileges, TablePrivilegeMeta.class);
	}
	
	/**
	 * カタログで使用可能なテーブルに関する記述を取得する。
	 * 
	 * @param catalog カタログ名。データベースに格納されたカタログ名と一致しなければならない。"" はカタログなしでカタログ名を検索する。null は、カタログ名を検索の限定に使用してはならないことを意味する
	 * @param schemaPattern スキーマ名パターン。データベースに格納されたスキーマ名と一致しなければならない。"" はスキーマなしでスキーマ名を検索する。null は、スキーマ名を検索の限定に使用してはならないことを意味する
	 * @param tableNamePattern テーブル名パターン。データベースに格納されたテーブル名と一致しなければならない
	 * @param types 組み込むテーブルの型のリスト。null はすべての型を返す 
	 * @return テーブル情報
	 * @throws SQLException SQLの実行に失敗した場合
	 * @see DatabaseMetaData#getTables(String, String, String, String[])
	 */
	public TypeSafeResultSet<TableMeta> getTables(String catalog, String schemaPattern, String tableNamePattern,
			String[] types) throws SQLException {
		ResultSet tables = meta.getTables(catalog, schemaPattern, tableNamePattern, types);
		return new TypeSafeResultSet<TableMeta>(tables, TableMeta.class);
	}
	
	/**
	 * このデータベースで使用可能なテーブルタイプを取得する。
	 * 
	 * @return テーブルタイプ情報
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	public TypeSafeResultSet<TableTypeMeta> getTableTypes() throws SQLException {
		ResultSet tableTypes = meta.getTableTypes();
		return new TypeSafeResultSet<TableTypeMeta>(tableTypes, TableTypeMeta.class);
	}
	
	/**
	 * このデータベースでサポートされているすべての標準 SQL タイプに関する記述を取得する。
	 * 
	 * @return データ型情報
	 * @throws SQLException SQLの実行に失敗した場合
	 * @see DatabaseMetaData#getTypeInfo()
	 */
	public TypeSafeResultSet<TypeInfoMeta> getTypeInfo() throws SQLException {
		ResultSet typeInfo = meta.getTypeInfo();
		return new TypeSafeResultSet<TypeInfoMeta>(typeInfo, TypeInfoMeta.class);
	}
	
	/**
	 * 特定のスキーマで定義されているユーザ定義型の説明を取得する。
	 * 
	 * @param catalog カタログ名。データベースに格納されたカタログ名と一致しなければならない。"" はカタログなしでカタログ名を検索する。null は、カタログ名を検索の限定に使用してはならないことを意味する
	 * @param schemaPattern スキーマパターン名。データベースに格納されたスキーマ名と一致しなければならない。"" はスキーマなしでスキーマ名を検索する。null は、スキーマ名を検索の限定に使用してはならないことを意味する
	 * @param typeNamePattern 型名パターン。データベースに格納された型名と一致しなければならない。完全指定名の可能性がある
	 * @param types ユーザ定義型のリスト (JAVA_OBJECT、STRUCT、または DISTINCT を含む)。null の場合はすべての型を返す 
	 * @return ユーザ定義型情報
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	public TypeSafeResultSet<UDTMeta> getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types)
			throws SQLException {
		ResultSet udts = meta.getUDTs(catalog, schemaPattern, typeNamePattern, types);
		return new TypeSafeResultSet<UDTMeta>(udts, UDTMeta.class);
	}
	
	/**
	 * 行の任意の値が変更された場合に、自動的に更新されるテーブルの列に関する記述を取得する。
	 * 
	 * @param catalog カタログ名。データベースに格納されたカタログ名と一致しなければならない。"" はカタログなしでカタログ名を検索する。null は、カタログ名を検索の限定に使用してはならないことを意味する
	 * @param schema スキーマ名。データベースに格納されたスキーマ名と一致しなければならない。"" はスキーマなしでスキーマ名を検索する。null は、スキーマ名を検索の限定に使用してはならないことを意味する
	 * @param table テーブル名。データベースに格納されたテーブル名と一致しなければならない 
	 * @return バージョンカラム情報
	 * @throws SQLException SQLの実行に失敗した場合
	 */
	public TypeSafeResultSet<VersionColumnMeta> getVersionColumns(String catalog, String schema, String table)
			throws SQLException {
		ResultSet versionColumns = meta.getVersionColumns(catalog, schema, table);
		return new TypeSafeResultSet<VersionColumnMeta>(versionColumns, VersionColumnMeta.class);
	}
}
