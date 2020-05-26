package com.feng.autocode.utils;

/**
 * @Author xiaofeng
 * @Description //TODO
 * @Param
 * @return
 **/
public class ConvertSqlType2JdbcUtil {
	/**
	 * 功能：获得列的jdbc类型
	 * 
	 * @param sqlType
	 * @return
	 */
	public static String sqlType2JdbcType(String sqlType) {
		if(sqlType.contains("(")){
			sqlType = sqlType.substring(0, sqlType.lastIndexOf("("));
		}
		switch (sqlType.toUpperCase()) {
		case "BIGINT":
			return "BIGINT";
		case "TINYINT":
			return "TINYINT";
		case "SMALLINT":
			return "SMALLINT";
		case "MEDIUMINT":
			return "INTEGER";
		case "INTEGER":
			return "INTEGER";
		case "INT":
			return "INTEGER";
		case "FLOAT":
			return "REAL";
		case "DOUBLE":
			return "DOUBLE";
		case "DECIMAL":
			return "DECIMAL";
		case "NUMERIC":
			return "DECIMAL";
		case "CHAR":
			return "CHAR";
		case "VARCHAR":
			return "VARCHAR";
		case "TINYBLOB":
			return "BINARY";
		case "TINYTEXT":
			return "VARCHAR";
		case "BLOB":
			return "BINARY";
		case "TEXT":
			return "LONGVARCHAR";
		case "MEDIUMBLOB":
			return "LONGVARBINARY";
		case "MEDIUMTEXT":
			return "LONGVARCHAR";
		case "LONGBLOB":
			return "LONGVARBINARY";
		case "LONGTEXT":
			return "LONGVARCHAR";
		case "DATE":
			return "DATE";
		case "TIME":
			return "TIME";
		case "YEAR":
			return "DATE";
		case "DATETIME":
			return "TIMESTAMP";
		case "TIMESTAMP":
			return "TIMESTAMP";
		default:
			break;
		}
		return sqlType.toUpperCase();
	}
	
}
