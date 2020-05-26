package com.feng.autocode.utils;


import com.feng.autocode.entity.Field;
import org.beetl.core.Format;
/**
 * @Author xiaofeng
 * @Description //TODO
 * @Param
 * @return
 **/
public class BeetlControlUtil implements Format {

	@Override
	public Object format(Object data, String pattern) {
		Field field = (Field) data;
		String formatStr = "";
		//模板中的命名
		String name = CamelUtil.underlineToCamel2(field.getField());
		//模板中的取值
		String value = "${"+CamelUtil.underlineToCamel2(field.getTableName())+"."+CamelUtil.underlineToCamel2(field.getField())+"}";
		switch (field.getDataType()) {
		//文本域
		case "textarea":
			formatStr = String.format("<textarea type=\"text\" name=\"%s\" maxlength=\"%s\" rows=\"4\" cols=\"60\" %s >%s</textarea>",
					name,field.getDataLength(),classFormat(field),value);
			break;
		//html富文本编辑器
		case "html":
			formatStr = String.format("<textarea %s name=\"%s\" cols=\"120\" rows=\"8\">%s</textarea>",
					classFormat(field),name,value);
			break;
		//time
		case "time":
			formatStr = String.format("<input type=\"text\" name=\"%s\" value=\"%s\""+ 
								" %s dateFmt=\"yyyy-MM-dd\" />", name,value,classFormat(field));
			break;
		//select
		case "select":
			formatStr = String.format("<cf:dict-select name=\"%s\" code=\"%s\"  value=\"%s\"/>", name,"",value);
			break;
		//图片
		case "image":
			formatStr = String.format("<div style=\"width: 400px;float: left;\" id=\"%sFileDiv\">"
				 		+"<p><input type=\"file\" id=\"%sFile\" class=\"file\"  name=\"%sFile\" onchange=\"mychange('%sThumb','%sFile')\"/></p>"
				 		+"<img id=\"%sThumb\"  src=\"\\${ctxIMG}/%s\" height=\"150\" width=\"150\"/>"
				 		+"<span><font style=\"color:#999\">图片格式为jpg bmp jpeg gif或png</font> </span>"
				 		+"</div>", name+"FileDiv",name,name,name,name,name,value);
			break;
		default:
			formatStr = String.format("<input type=\"text\" name=\"%s\" value=\"%s\" %s maxlength=\"%s\"/>",
					name,value,classFormat(field),field.getDataLength());
			break;
		}
		return formatStr;
	}
	
	/**
	 * dwz特有的验证
	 * @param f
	 * @return
	 */
	@SuppressWarnings("unused")
	private String classFormat(Field f){
		String dwz_class = "";
		if(!f.isCanNull()){
			dwz_class = "required ";
		}
		//commontArr[0]字段备注，commontArr[1]数据具体类型（非java类型）
		switch (f.getDataType()) {
		case "email":
			dwz_class += "email";
			break;
		case "phone":
			dwz_class += "phone";
			break;
		case "digits":
			dwz_class += "digits";
			break;
		case "number":
			dwz_class += "number";
			break;
		case "url":
			dwz_class += "url";
			break;
		case "html":
			dwz_class += "editor";
			break;
		case "time":
			dwz_class += "date";
			break;
		default:
			break;
		}
		if(!"".equals(dwz_class)){
			return "class=\""+dwz_class+"\"";
		}
		return dwz_class;
	}

}
