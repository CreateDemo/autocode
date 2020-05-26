package com.feng.autocode.utils;

import com.feng.autocode.entity.InitParamEntity;
import com.feng.autocode.entity.Table;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.exception.BeetlException;
import org.beetl.core.resource.FileResourceLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author xiaofeng
 * @Description //TODO 
 * @Date 16:47 2020/5/26
 * @Param 
 * @return 
 **/
public class BeetlUtil {
	
	/**
	 * 包名
	 */
	private InitParamEntity param;
	
	
	public InitParamEntity getParam() {
		return param;
	}

	public void setParam(InitParamEntity param) {
		this.param = param;
	}
	String templateRoot = System.getProperty("user.dir") + PathUtils.mavenResourcePath +"/templates";
	/**
	 * 模板生成核心工具
	 */
	private GroupTemplate gt;
	/**
	 * 初始化beetl
	 * @param param
	 * @throws IOException
	 */
	public BeetlUtil(InitParamEntity param) throws IOException{
		this.param = param;
		System.out.println("所有文件生成到文件夹：" + param.getProjectMkdir());
		FileResourceLoader frl = new FileResourceLoader(templateRoot, "UTF-8");
		Configuration cfg = Configuration.defaultConfiguration();
		gt = new GroupTemplate(frl, cfg);
		//首字母大写
		gt.registerFormat("TCase", new BeetlInitcapUtil());
		//类型转换
		gt.registerFormat("TConver", new BeetlSqlTypeUtil());
		//字段驼峰
		gt.registerFormat("Camle", new BeetlCamleUtil());
		//表名驼峰,首字母大写
		gt.registerFormat("TCaseCamle", new BeetlCamleInitCapUtil());
		//根据字段类型来生成相应的html控件
		gt.registerFormat("TControl", new BeetlControlUtil());
		//大写
		gt.registerFormat("Upcase", new BeetlSqlType2JdbcUtil());
	}

	/**
	 * 每个java模块的目录
	 * @param type
	 * @param params
	 * @return
	 */
	private String getRealJavaPath(String type,InitParamEntity params){
		String smallType = type.substring(type.indexOf(".") + 1);
		if ("bo".equals(smallType)) {
			smallType = "pojo/" + smallType;
		} else if ("entity".equals(smallType)) {
			smallType = "entity";
		} else if ("app".equals(smallType)) {
			smallType = "application";
		} else if ("app.impl".equals(smallType)) {
			smallType = "application/impl";
		} else if ("web".equals(smallType)) {
			smallType = "controller";
		}else if ("mapper".equals(smallType)) {
			smallType = "repository/mapper";
		}else if ("repository.impl".equals(smallType)) {
			smallType = "repository/impl";
		}else if ("service.impl".equals(smallType)) {
			smallType = "service/impl";
		}else if ("resp".equals(smallType)) {
			smallType = "vo/resp";
		}else if ("req".equals(smallType)) {
			smallType = "vo/req";
		}

//		String javamulu = String.format(type,params.getProjectMkdir()) + PathConstant.mavenJavaPath + "/"
		String javamulu = params.getProjectMkdir() + PathUtils.mavenJavaPath + "/"
				+ params.getRootPackageName().replace(".", "/")
				+ "/" + smallType.replace(".","/");
		if(StringUtils.isBlank(params.getAliasPackageName())){
			return javamulu;
		}
		String tempAliasPackageName = params.getAliasPackageName();
        if(StringUtils.startsWith(params.getAliasPackageName(),".")){
			tempAliasPackageName = tempAliasPackageName.replace(".","/");
		}else{
			tempAliasPackageName = "/" + tempAliasPackageName;
		}
		return javamulu + tempAliasPackageName;
	}
	/**
	 * 每个mapper模块的目录
	 * @param type
	 * @param params
	 * @return
	 */
	private String getRealMapperResPath(String type,InitParamEntity params){
//		String resmulu = String.format(type,params.getProjectMkdir()) + PathConstant.mavenResourcePath + "/mapper";
		String resmulu = params.getProjectMkdir() + PathUtils.mavenResourcePath;
		if(StringUtils.isNotBlank(params.getAliasPackageName())){
			return resmulu;
		}
		String tempAliasPackageName = params.getAliasPackageName();
		if(StringUtils.startsWith(params.getAliasPackageName(),".")){
			tempAliasPackageName = tempAliasPackageName.replace(".","/");
		}else{
			tempAliasPackageName = "/" + tempAliasPackageName;
		}
		String rootPackageName = params.getRootPackageName().replace(".", "/");
//				+ "/" + smallType.replace(".","/");
		return resmulu + tempAliasPackageName + rootPackageName + "/repository/mapper/";
	}
	/**
	 * 根据配置以及表信息生成代码
	 * @throws FileNotFoundException
	 * @throws BeetlException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public void makeCode(List<Table> tableList) throws BeetlException, FileNotFoundException, IllegalAccessException, InvocationTargetException{

		Template t = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(new Date());
		for (Table table : tableList) {
			String tableName = table.getTableName();
			table.setTableName(tableName);
//			String reltableName = table.getRelTableName();
//			reltableName = reltableName.substring(4);
//			table.setRelTableName(reltableName);
			InitParamEntity params = new InitParamEntity();
			BeanUtils.copyProperties(params,param);
			//表名驼峰并且首字母大写
			String tableUp = ConvertSqlType2JavaUtil.initcap(CamelUtil.underlineToCamel2(table.getTableName()));
			//bo
			if(params.isIsbo()){
				String javamulu = getRealJavaPath(PathUtils.targetParentProjectBo,params);
				t = gt.getTemplate("dto.beetl");
				t.binding("rootPackageName", params.getRootPackageName());
				t.binding("aliasPackageName", params.getAliasPackageName());
				t.binding("table", table);
				t.binding("dateStr", dateStr);
				t.binding("codeAuthor",params.getCodeAuthor());
				File pack = new File(javamulu);
				if(!pack.exists()){
					pack.mkdirs();
				}
				t.renderTo(new FileOutputStream(new File(pack , tableUp + "Dto.java")));
//				generateBuildGradle(params,PathConstant.targetParentProjectBo);
			}
			//resp
			if(params.isIsresp()){
				String javamulu = getRealJavaPath(PathUtils.targetParentProjectResp,params);
				t = gt.getTemplate("resp.beetl");
				t.binding("rootPackageName", params.getRootPackageName());
				t.binding("aliasPackageName", params.getAliasPackageName());
				t.binding("table", table);
				t.binding("dateStr", dateStr);
				t.binding("codeAuthor",params.getCodeAuthor());
				File pack = new File(javamulu);
				if(!pack.exists()){
					pack.mkdirs();
				}
				t.renderTo(new FileOutputStream(new File(pack , tableUp + "Resp.java")));
			}
			//req
			if(params.isIsreq()){
				String javamulu = getRealJavaPath(PathUtils.targetParentProjectReq,params);
				t = gt.getTemplate("req.beetl");
				t.binding("rootPackageName", params.getRootPackageName());
				t.binding("aliasPackageName", params.getAliasPackageName());
				t.binding("table", table);
				t.binding("dateStr", dateStr);
				t.binding("codeAuthor",params.getCodeAuthor());
				File pack = new File(javamulu);
				if(!pack.exists()){
					pack.mkdirs();
				}
				t.renderTo(new FileOutputStream(new File(pack , tableUp + "Req.java")));
			}
			//dao
			if(params.isIsdao()){
				String javamulu = getRealJavaPath(PathUtils.targetParentProjectDao,params);
				t = gt.getTemplate("dao.beetl");
				t.binding("rootPackageName", params.getRootPackageName());
				t.binding("aliasPackageName", params.getAliasPackageName());
				t.binding("table", table);
				t.binding("dateStr", dateStr);
				t.binding("codeAuthor",params.getCodeAuthor());
				File pack = new File(javamulu);
				if(!pack.exists()){
					pack.mkdirs();
				}
				t.renderTo(new FileOutputStream(new File(pack , tableUp + "Mapper.java")));
//				generateBuildGradle(params,PathConstant.targetParentProjectDao);
			}
			//mapper
			if(params.isIsmapper()){
				String javamulu = getRealMapperResPath(PathUtils.targetParentProjectDao,params);
				t = gt.getTemplate("mapper.beetl");
				t.binding("rootPackageName", params.getRootPackageName());
				t.binding("aliasPackageName", params.getAliasPackageName());
				t.binding("table", table);
				t.binding("dateStr", dateStr);
				t.binding("codeAuthor",params.getCodeAuthor());
				File pack = new File(javamulu);
				if(!pack.exists()){
					pack.mkdirs();
				}
				t.renderTo(new FileOutputStream(new File(pack , tableUp + "Mapper.xml")));
//				generateBuildGradle(params,PathConstant.targetParentProjectDao);
			}

			//entity
			if(params.isIsentity()){
				String javamulu = getRealJavaPath(PathUtils.targetParentProjectEntity,params);
				t = gt.getTemplate("entity.beetl");
				t.binding("rootPackageName", params.getRootPackageName());
				t.binding("aliasPackageName", params.getAliasPackageName());
				t.binding("table", table);
				t.binding("dateStr", dateStr);
				t.binding("codeAuthor",params.getCodeAuthor());
				File pack = new File(javamulu);
				if(!pack.exists()){
					pack.mkdirs();
				}
				t.renderTo(new FileOutputStream(new File(pack , tableUp + "Entity.java")));
//				generateBuildGradle(params,PathConstant.targetParentProjectEntity);
			}

			//paramEntity
			if(params.isIsentity()){
				String javamulu = getRealJavaPath(PathUtils.targetParentProjectEntity,params);
				t = gt.getTemplate("paramEntity.beetl");
				t.binding("rootPackageName", params.getRootPackageName());
				t.binding("aliasPackageName", params.getAliasPackageName());
				t.binding("table", table);
				t.binding("dateStr", dateStr);
				t.binding("codeAuthor",params.getCodeAuthor());
				File pack = new File(javamulu + "/param");
				if(!pack.exists()){
					pack.mkdirs();
				}
				t.renderTo(new FileOutputStream(new File(pack , tableUp + "ParamEntity.java")));
//				generateBuildGradle(params,PathConstant.targetParentProjectEntity);
			}

			//repository
			if(params.isIsservice()){
				String javamulu = getRealJavaPath(PathUtils.targetParentProjectService,params);
				t = gt.getTemplate("repository.beetl");
				t.binding("rootPackageName", params.getRootPackageName());
				t.binding("aliasPackageName", params.getAliasPackageName());
				t.binding("table", table);
				t.binding("dateStr", dateStr);
				t.binding("codeAuthor",params.getCodeAuthor());
				File pack = new File(javamulu);
				if(!pack.exists()){
					pack.mkdirs();
				}
				t.renderTo(new FileOutputStream(new File(pack , tableUp + "Repository.java")));
//				generateBuildGradle(params,PathConstant.targetParentProjectService);
			}

			//repositoryimpl
			if(params.isIsserviceImpl()){
				String javamulu = getRealJavaPath(PathUtils.targetParentProjectServiceImpl,params);
				t = gt.getTemplate("repositoryImpl.beetl");
				t.binding("rootPackageName", params.getRootPackageName());
				t.binding("aliasPackageName", params.getAliasPackageName());
				t.binding("table", table);
				t.binding("dateStr", dateStr);
				t.binding("codeAuthor",params.getCodeAuthor());
				File pack = new File(javamulu);
				if(!pack.exists()){
					pack.mkdirs();
				}
				t.renderTo(new FileOutputStream(new File(pack , tableUp + "RepositoryImpl.java")));
//				generateBuildGradle(params,PathConstant.targetParentProjectServiceImpl);
			}

			//serviceimpl
			if(params.isIscontroller()){
				String javamulu = getRealJavaPath(PathUtils.targetParentProjectWeb,params);
				t = gt.getTemplate("controller.beetl");
				t.binding("rootPackageName", params.getRootPackageName());
				t.binding("aliasPackageName", params.getAliasPackageName());
				t.binding("table", table);
				t.binding("dateStr", dateStr);
				t.binding("codeAuthor",params.getCodeAuthor());
				File pack = new File(javamulu);
				if(!pack.exists()){
					pack.mkdirs();
				}
				t.renderTo(new FileOutputStream(new File(pack , tableUp + "Controller.java")));
//				generateBuildGradle(params,PathConstant.targetParentProjectWeb);
			}

			//生成多表关联的查询javabean对应bo
			if(params.isSqlBean()){
				String javamulu = getRealJavaPath(PathUtils.targetParentProjectBo,params);
				t = gt.getTemplate("dto.beetl");
				t.binding("rootPackageName", params.getRootPackageName());
				t.binding("aliasPackageName", params.getAliasPackageName());
				t.binding("table", table);
				t.binding("dateStr", dateStr);
				t.binding("codeAuthor",params.getCodeAuthor());
				File pack = new File(javamulu);
				if(!pack.exists()){
					pack.mkdirs();
				}
				t.renderTo(new FileOutputStream(new File(pack , params.getSqlTableName() + "Bo.java")));
				System.out.println("请复制以下内容到文件");
				System.out.println("------------------------------以下内容归属：*Mapper.xml");
				//mapper
				t = gt.getTemplate("beanMapper.beetl");
				t.binding("rootPackageName", params.getRootPackageName());
				t.binding("aliasPackageName", params.getAliasPackageName());
				t.binding("table", table);
				t.binding("dateStr", dateStr);
				t.binding("domainPackageName", params.getDomainPackageName());
				t.binding("codeAuthor",params.getCodeAuthor());
				try {
					t.binding("showsql", DbUtil.ReadTxtFile(templateRoot+ "/sql.txt"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(t.render());
				System.out.println("------------------------------以下内容归属：*Dao.java");
				//dao
				t = gt.getTemplate("beanDao.beetl");
				t.binding("rootPackageName", params.getRootPackageName());
				t.binding("aliasPackageName", params.getAliasPackageName());
				t.binding("table", table);
				t.binding("dateStr", dateStr);
				t.binding("domainPackageName", params.getDomainPackageName());
				t.binding("codeAuthor",params.getCodeAuthor());
				System.out.println(t.render());
				System.out.println("------------------------------");

			}

			// 生成Service类
			if(params.isIsdomain()) {
				String targetParentProject = PathUtils.targetParentProjectDomain;
				String templateBeetl = "service.beetl";
				makeDetailCode(params, table, targetParentProject, templateBeetl);
			}
			// 生成ServiceImpl类
			if (params.isIsdomainImpl()) {
				String targetParentProject = PathUtils.targetParentProjectDomainImpl;
				String templateBeetl = "serviceImpl.beetl";
				makeDetailCode(params, table, targetParentProject, templateBeetl);
			}
			// 生成application类
			if (params.isIsapp()) {
				String targetParentProject = PathUtils.targetParentProjectApp;
				String templateBeetl = "application.beetl";
				makeDetailCode(params, table, targetParentProject, templateBeetl);
			}
			// 生成applicationImpl类
			if (params.isIsappImpl()) {
				String targetParentProject = PathUtils.targetParentProjectAppImpl;
				String templateBeetl = "applicationImpl.beetl";
				makeDetailCode(params, table, targetParentProject, templateBeetl);
			}
		}
	}


	private void makeDetailCode(InitParamEntity params, Table table, String targetParentProject, String templateBeetl) throws BeetlException, FileNotFoundException, IllegalAccessException, InvocationTargetException {
		Template t = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(new Date());
		//表名驼峰并且首字母大写
		String tableUp = ConvertSqlType2JavaUtil.initcap(CamelUtil.underlineToCamel2(table.getTableName()));
		//serviceimpl
		String javamulu = getRealJavaPath(targetParentProject,params);
		t = gt.getTemplate(templateBeetl);
		t.binding("rootPackageName", params.getRootPackageName());
		t.binding("aliasPackageName", params.getAliasPackageName());
		t.binding("table", table);
		t.binding("dateStr", dateStr);
		t.binding("codeAuthor",params.getCodeAuthor());
		File pack = new File(javamulu);
		if(!pack.exists()){
			pack.mkdirs();
		}
		if ("service.beetl".equals(templateBeetl)) {
			t.renderTo(new FileOutputStream(new File(pack, tableUp + "Service.java")));
		} else if ("serviceImpl.beetl".equals(templateBeetl)) {
			t.renderTo(new FileOutputStream(new File(pack, tableUp + "ServiceImpl.java")));
		} else if ("application.beetl".equals(templateBeetl)) {
			t.renderTo(new FileOutputStream(new File(pack, tableUp + "Application.java")));
		} else if ("applicationImpl.beetl".equals(templateBeetl)) {
			t.renderTo(new FileOutputStream(new File(pack, tableUp + "ApplicationImpl.java")));
		}

	}

    //第一次生成build.gradle配置文件
	public void generateBuildGradle(InitParamEntity params,String type) throws FileNotFoundException {
		if(params.isIsgradle()){
			String javamulu = String.format(type,params.getProjectMkdir());
			Template t = gt.getTemplate("gradle_build.beetl");
			t.binding("projectOrtherName", params.getProjectOrtherName());
			if(StringUtils.equalsIgnoreCase(PathUtils.targetParentProjectBo,type)){
				t.binding("buildType", 1);
			}else if(StringUtils.equalsIgnoreCase(PathUtils.targetParentProjectEntity,type)){
				t.binding("buildType", 2);
			}else if(StringUtils.equalsIgnoreCase(PathUtils.targetParentProjectDao,type)){
				t.binding("buildType", 3);
			}else if(StringUtils.equalsIgnoreCase(PathUtils.targetParentProjectService,type)){
				t.binding("buildType", 4);
			}else if(StringUtils.equalsIgnoreCase(PathUtils.targetParentProjectServiceImpl,type)){
				t.binding("buildType", 5);
			}else if(StringUtils.equalsIgnoreCase(PathUtils.targetParentProjectWeb,type)){
				t.binding("buildType", 6);
			}
			File pack = new File(javamulu);
			if(!pack.exists()){
				pack.mkdirs();
			}
			File buildGradleFile = new File(pack , "build.gradle");
			if(!buildGradleFile.exists()){
				t.renderTo(new FileOutputStream(buildGradleFile));
			}
		}
	}


}
