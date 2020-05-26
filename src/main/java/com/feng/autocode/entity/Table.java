package com.feng.autocode.entity;

import lombok.Data;

import java.util.List;

/**
 * 表信息
 * @author sunlin
 *
 */
@Data
public class Table {

	/**
	 * 去掉es_前缀的表名
	 */
	private String tableName;
	/**
	 * 没有去掉es_前缀的真实表名
	 */
	private String relTableName;
	/**
	 * 表的主键名称
	 */
	private String id;
	/**
	 * 主键类型
	 */
	private String idType;
	/**
	 * 表的备注
	 */
	private String tableRemark;
	/**
	 * 主键策略
	 */
	private String extra;
	/**
	 * 表中是否存放图片信息
	 */
	private boolean hasImage;
	/**
	 * 表的所有字段
	 */
	private List<Field> fieldList;
}
