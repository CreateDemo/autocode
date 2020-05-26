package com.feng.autocode.utils;

import org.beetl.core.Format;


public class BeetlSqlTypeUtil implements Format {


	@Override
	public Object format(Object data, String pattern) {
		String s = data.toString();
		return ConvertSqlType2JavaUtil.sqlType2JavaType(s);
	}

}
