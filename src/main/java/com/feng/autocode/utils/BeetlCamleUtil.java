package com.feng.autocode.utils;


import org.beetl.core.Format;


public class BeetlCamleUtil implements Format {


	@Override
	public Object format(Object data, String pattern) {
		String s = data.toString();
		return CamelUtil.underlineToCamel2(s);
	}

}
