package com.feng.autocode.utils;

import com.feng.autocode.entity.Field;
import com.feng.autocode.entity.InitParamEntity;
import com.feng.autocode.entity.Table;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 
 * 将所有需要生成代码的表以及其所有列的信息加载到实体类中
 *
 */
@Slf4j
public class DbUtil {
	/**
	 * 生成多表关联的查询javabean的name
	 */
	public static String dummyTable = "";


	public static List<Table> initTable(String[] tableNames, InitParamEntity p){
		List<Table> tableList = new ArrayList<>();
		Connection conn = null;
		String url = p.getUrl();
		String jdbcDriver = p.getJdbcDriver();
		String user = p.getUser();
		String password = p.getPassword();
		DbUtils.loadDriver(jdbcDriver);
		try {
			conn = DriverManager.getConnection(url, user, password);
			Statement stmt = conn.createStatement();
			////生成多表关联的查询javabean
			boolean delTable = false;
			//如果有配置多表查询的bo，tables就失效
			if(p.isSqlBean()){
				dummyTable = p.getSqlTableName();
				try {
					String sql = "CREATE TABLE "+ dummyTable + " as " + ReadTxtFile(System.getProperty("user.dir") + PathUtils.mavenResourcePath +"/templates/sql.txt");
					stmt.execute(sql);
					delTable = true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tableNames = new String[]{dummyTable};
			}

			for (int i = 0; i < tableNames.length; i++) {
				log.info("表："+tableNames[i]);
				ResultSet rs = stmt.executeQuery("show full columns from "+ tableNames[i]);
				List<Field> tl = new ArrayList<>();
				Table table = new Table();
				table.setHasImage(false);
				while (rs.next()){
					log.info("字段："+rs.getString("Field"));
					Field field = new Field();
					field.setField(rs.getString("Field").toLowerCase());
					//截取类型与长度
					String sqlType = rs.getString("Type");
					if(sqlType.contains("(") && !sqlType.contains("enum")){
						field.setType(sqlType.substring(0, sqlType.lastIndexOf("(")));
						String dataPoint = sqlType.substring(sqlType.lastIndexOf("(") + 1,sqlType.lastIndexOf(")"));
						if(dataPoint.contains(",")){
							field.setDataLength(Integer.parseInt(dataPoint.substring(0, dataPoint.indexOf(","))));
							field.setPointLength(Integer.parseInt(dataPoint.substring(dataPoint.indexOf(",") + 1)));
						}else{
							field.setDataLength(Integer.parseInt(dataPoint));
							field.setPointLength(0);
						}
					}else{
						field.setType(rs.getString("Type"));
						field.setDataLength(0);
						field.setPointLength(0);
					}
					field.setCollation(rs.getString("Collation"));
					field.setKey(rs.getString("Key"));
					if("PRI".equalsIgnoreCase(rs.getString("Key"))){
						table.setId(rs.getString("Field").toLowerCase());
						table.setIdType(rs.getString("Type"));
						if(sqlType.contains("(")){
							table.setIdType(sqlType.substring(0, sqlType.lastIndexOf("(")));
						}
						table.setExtra(rs.getString("Extra"));
					}
					//类似    用户名|text   用户名|email
					String comment = rs.getString("Comment");
					if(comment.contains("|")){
						//field.setComment(org.apache.commons.lang.StringUtils.splitPreserveAllTokens(comment, "|")[0]);
						//field.setDataType(org.apache.commons.lang.StringUtils.splitPreserveAllTokens(comment, "|")[1]);
					}else{
						field.setComment(comment);
						field.setDataType("text");
					}
					if("image".equals(field.getDataType())){
						table.setHasImage(true);
					}
					field.setCanNull(returnNull_Boolean(rs.getString("Null")));
					field.setDefaultStr(rs.getObject("Default"));
					field.setExtra(rs.getString("Extra"));
					field.setTableName(tableNames[i].replace("w_", ""));
					field.setRelTableName(tableNames[i]);
					tl.add(field);
				}
				table.setFieldList(tl);
//				table.setTableName(tableNames[i].replace("w_", ""));
				table.setTableName(tableNames[i]);
				table.setRelTableName(tableNames[i]);
				//获取表的信息
				rs = stmt.executeQuery("show table status WHERE NAME=  '"+ tableNames[i] + "'");
				while (rs.next()){
					table.setTableRemark(replaceBlank(rs.getString("Comment")));
					break;
				}
				tableList.add(table);
			}

			if(p.isSqlBean() && delTable){//只有虚拟表才会删除
				stmt.execute("DROP TABLE " + dummyTable);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return tableList;
	}

	public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	/**
	 * 是否可以为空
	 * @param nullStr
	 */
	private static boolean returnNull_Boolean(String nullStr){
		if("NO".equals(nullStr.toUpperCase())){
			return false;
		}
		return true;
	}

	/**
	 * 获取sql.txt的内容
	 * @param FileName
	 * @return
	 * @throws Exception
	 */
	public static String ReadTxtFile(String FileName) throws Exception {
		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(FileName));
		ByteArrayOutputStream memStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = bufferedInputStream.read(buffer)) != -1){
			memStream.write(buffer, 0, len);
		}
		byte[] data = memStream.toByteArray();
		bufferedInputStream.close();
		memStream.close();
		bufferedInputStream.close();
		return new String(data);
	}

}
