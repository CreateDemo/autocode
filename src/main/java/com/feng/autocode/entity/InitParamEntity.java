package com.feng.autocode.entity;

import lombok.Data;

/**
 * @Author xiaofeng
 * @Description //TODO
 * @Date 15:11 2020/5/26
 **/
@Data
public class InitParamEntity {
	/**
	 *目标项目根目录
	 */
	private String projectMkdir;
	/**
	 * 根包名
	 */
	private String rootPackageName;
	/**
	 * 业务名
	 */
	private String aliasPackageName;
	/**
	 * 页面存放路径
	 */
	private String jspDir;
	/**
	 * 控制器访问主目录
	 */
	private String mapping;
	/**
	 * domain model的包目录
	 */
	private String domainPackageName;
	/**
	 * 是否生成isbo
	 */
	private boolean isbo;
	/**
	 * 是否生成isresp
	 */
	private boolean isresp;
	/**
	 * 是否生成isreq
	 */
	private boolean isreq;
	/**
	 * 是否生成Dao
	 */
	private boolean	isdao;
	/**
	 * 是否生成isMapper
	 */
	private boolean ismapper;
	/**
	 * 是否生成isEntity
	 */
	private boolean	isentity;
	/**
	 * 是否生成isService
	 */
	private boolean	isservice;
	/**
	 * 是否生成isServiceImpl
	 */
	private boolean	isserviceImpl;

	/**
	 * 是否生成iscontroller
	 */
	private boolean	iscontroller;

	/**
	 * 是否生成isdomain
	 */
	private boolean	isdomain;

	/**
	 * 是否生成isdomainImpl
	 */
	private boolean	isdomainImpl;

	/**
	 * 是否生成isapp
	 */
	private boolean	isapp;

	/**
	 * 是否生成iisappImpl
	 */
	private boolean	isappImpl;

	/**
	 * 是否生成build.gradle的配置文件
	 */
	private boolean isgradle;

	/**
	 * mysql 用户名
	 */
	private String	user;
	/**
	 * mysql 密码
	 */
	private String	password;
	/**
	 * 需要生成的表多表以,分隔
	 */
	private String tables;

	private String url;

	/**
	 * 是否生成多表关联javabean
	 */
	private boolean isSqlBean;
	/**
	 * 定义的生成多表关联的虚拟表名
	 */
	private String sqlTableName;

	/**
	 * 項目名 例如 wtoip-rs-product  就是product
	 */
	private String projectOrtherName;
	private String jdbcDriver;
	/**
	 * 代码编写人
	 */
	private String codeAuthor;

}
