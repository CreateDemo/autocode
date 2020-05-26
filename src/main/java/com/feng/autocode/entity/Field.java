package com.feng.autocode.entity;

import lombok.Data;

/**
 * 表的列描述
 * @author sunlin
 *
 */
@Data
public class Field {

	private String field;
	
	private String type;
	
	private String collation;
	
	private String key;
	
	private String comment;
	
	private boolean canNull;
	
	private Object defaultStr;
	
	private String extra;
	
	private int dataLength;
	
	private int pointLength;
	
	/**
	 * 去掉es_前缀的表名
	 */
	private String tableName;
	/**
	 * 没有去掉es_前缀的真实表名
	 */
	private String relTableName;
	
	/**
	 * 字段类型,决定页面的控件与验证
	 */
	private String dataType;


	
	
	
}
