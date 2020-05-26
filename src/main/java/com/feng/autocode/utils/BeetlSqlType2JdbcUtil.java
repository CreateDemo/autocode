package com.feng.autocode.utils;

import org.beetl.core.Format;


public class BeetlSqlType2JdbcUtil implements Format {


	@Override
	public Object format(Object data, String pattern) {
		String s = data.toString();
		return ConvertSqlType2JdbcUtil.sqlType2JdbcType(s);
	}

}
