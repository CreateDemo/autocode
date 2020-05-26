package com.feng.autocode.utils;

public class ConvertSqlType2JavaUtil {
	/**
	 * 功能：获得列的数据类型
	 * 
	 * @param sqlType
	 * @return
	 */
	public static String sqlType2JavaType(String sqlType) {
		if(sqlType.contains("(")){
			sqlType = sqlType.substring(0, sqlType.lastIndexOf("("));
		}
		switch (sqlType.toUpperCase()) {
		case "BIGINT":
			return "Long";
		case "TINYINT":
			return "Integer";
		case "SMALLINT":
			return "Short";
		case "MEDIUMINT":
			return "Integer";
		case "INTEGER":
			return "Integer";
		case "INT":
			return "Integer";
		case "FLOAT":
			return "Float";
		case "DOUBLE":
			return "Double";
		case "DECIMAL":
			return "BigDecimal";
		case "NUMERIC":
			return "BigDecimal";
		case "CHAR":
			return "String";
		case "VARCHAR":
			return "String";
		case "TINYBLOB":
			return "DataTypeWithBLOBs.byte[]";
		case "TINYTEXT":
			return "String";
		case "BLOB":
			return "byte[]";
		case "TEXT":
			return "String";
		case "MEDIUMBLOB":
			return "byte[]";
		case "MEDIUMTEXT":
			return "String";
		case "LONGBLOB":
			return "byte[]";
		case "LONGTEXT":
			return "String";
		case "DATE":
			return "Date";
		case "TIME":
			return "Date";
		case "YEAR":
			return "Date";
		case "DATETIME":
			return "Date";
		case "TIMESTAMP":
			return "Date";
		default:
			break;
		}
		return null;
	}
	/** 
	  * 功能：将输入字符串的首字母改成大写 
	  * @param str 
	  * @return 
	  */  
	 public static String initcap(String str) {  
	       
	     char[] ch = str.toCharArray();  
	     if(ch[0] >= 'a' && ch[0] <= 'z'){  
	         ch[0] = (char)(ch[0] - 32);  
	     }  
	       
	     return new String(ch);  
	 }  
	 /**
	  * 转换为布尔
	  * @param isStr
	  * @return
	  */
	 public static boolean returnIs(String isStr){
			if("1".equals(isStr)){
				return true;
			}
			return false;
		}
}
