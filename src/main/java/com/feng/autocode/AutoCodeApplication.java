package com.feng.autocode;

import com.feng.autocode.entity.InitParamEntity;
import com.feng.autocode.entity.Table;
import com.feng.autocode.utils.BeetlUtil;
import com.feng.autocode.utils.ConvertSqlType2JavaUtil;
import com.feng.autocode.utils.DbUtil;
import com.feng.autocode.utils.PathUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Properties;


@Slf4j
public class AutoCodeApplication {

      /**
       *
       * 根据配置文件生成代码  具体配置见application.properties
       * 根据自己的需要进行配置
       *   运行入口
       *
       *
       **/
    public static void main(String[] args) throws IOException, InvocationTargetException, IllegalAccessException {
        Properties p = readProperties();
        InitParamEntity param =new InitParamEntity();
        String projectMkdir = p.getProperty("projectMkdir");
        String packageName = p.getProperty("packageName");
        param.setProjectMkdir(projectMkdir);
        int lastindex = 0;
        if (StringUtils.contains(projectMkdir, ".")) {
            lastindex = projectMkdir.lastIndexOf(".") + 1;
        } else {
            lastindex = projectMkdir.lastIndexOf("/") + 1;
        }
        param.setRootPackageName(packageName);
        param.setProjectOrtherName(StringUtils.substring(projectMkdir, lastindex));
        String aliasPackageName = p.getProperty("aliasPackageName");
        if (StringUtils.isNotBlank(aliasPackageName) && !StringUtils.startsWith(aliasPackageName, ".")) {
            aliasPackageName = "." + aliasPackageName;
        }
        param.setAliasPackageName(aliasPackageName);
        param.setAliasPackageName(aliasPackageName);
        param.setIsgradle(ConvertSqlType2JavaUtil.returnIs(p.getProperty("isGradle")));
        param.setIsresp(ConvertSqlType2JavaUtil.returnIs(p.getProperty("isResp")));
        param.setIsreq(ConvertSqlType2JavaUtil.returnIs(p.getProperty("isReq")));
        param.setIsbo(ConvertSqlType2JavaUtil.returnIs(p.getProperty("isBo")));
        param.setIsreq(ConvertSqlType2JavaUtil.returnIs(p.getProperty("isReq")));
        param.setIsresp(ConvertSqlType2JavaUtil.returnIs(p.getProperty("isResp")));
        param.setIsdao(ConvertSqlType2JavaUtil.returnIs(p.getProperty("isDao")));
        param.setIsmapper(ConvertSqlType2JavaUtil.returnIs(p.getProperty("isMapper")));
        param.setIsentity(ConvertSqlType2JavaUtil.returnIs(p.getProperty("isEntity")));
        param.setIsservice(ConvertSqlType2JavaUtil.returnIs(p.getProperty("isService")));
        param.setIsserviceImpl(ConvertSqlType2JavaUtil.returnIs(p.getProperty("isServiceImpl")));
        param.setIscontroller(ConvertSqlType2JavaUtil.returnIs(p.getProperty("isController")));
        param.setIsdomain(ConvertSqlType2JavaUtil.returnIs(p.getProperty("isDomain")));
        param.setIsdomainImpl(ConvertSqlType2JavaUtil.returnIs(p.getProperty("isDomainImpl")));
        param.setIsapp(ConvertSqlType2JavaUtil.returnIs(p.getProperty("isApp")));
        param.setIsappImpl(ConvertSqlType2JavaUtil.returnIs(p.getProperty("isAppImpl")));
        param.setSqlBean(ConvertSqlType2JavaUtil.returnIs(p.getProperty("isSqlBean")));
        param.setCodeAuthor(p.getProperty("codeAuthor"));
        param.setSqlTableName(p.getProperty("sqlTableName"));
        param.setUrl(p.getProperty("url"));
        param.setJdbcDriver(p.getProperty("jdbcDriver"));
        param.setUser(p.getProperty("user"));
        param.setPassword(p.getProperty("password"));
        param.setTables(p.getProperty("tables"));
        param.setJspDir(p.getProperty("jspDir"));
        param.setMapping(p.getProperty("mapping"));
        log.info("-----读取配置结束-----");
        log.info("-----加载对应的表开始-----");
        String[] tableNames = null;
        if (StringUtils.isNotBlank(param.getTables())) {
            tableNames = param.getTables().split(",");
        }
        List<Table> list = DbUtil.initTable(tableNames, param);
        if (list.size() == 0) {
            log.info("-----未加载任何表，程序退出-----");
            return;
        }
        log.info("-----加载对应的表结束,总共需要生成" + list.size() + "个表的代码-----");
        log.info("-----生成代码开始-----");
        BeetlUtil bu = new BeetlUtil(param);
        bu.makeCode(list);
        log.info("-----生成代码结束-----");
    }


    private static Properties readProperties() throws IOException {
        log.info("--------读取配置开始-----");
        Properties p = new Properties();
        InputStream inputStream =null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(System.getProperty("user.dir") + PathUtils.mavenResourcePath + "/application.properties"));
            p.load(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (inputStream!=null){
                inputStream.close();
            }
        }
        return  p;
    }
}
