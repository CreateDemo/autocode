package com.feng.autocode.utils;

/**
 * @Author xiaofeng
 * @Description //TODO
 * @Date 15:04 2020/5/26
 * @Param
 * @return
 **/
public class PathUtils {
    /**
     * maven根目录
     */
    private static String mavenPath = "/src/main";
    /**
     * maven根目录java
     */
    public static String mavenJavaPath = mavenPath + "/java";
    /**
     * maven根目录resources
     */
    public static String mavenResourcePath = mavenPath + "/resources";
    /**
     * 项目bo  1
     */
    public static String targetParentProjectBo = "%s.dto";
    /**
     * 项目resp  1
     */
    public static String targetParentProjectResp = "%s.resp";
    /**
     * 项目req  1
     */
    public static String targetParentProjectReq = "%s.req";
    /**
     * 项目entity  2
     */
    public static String targetParentProjectEntity = "%s.entity";
    /**
     * 项目dao  3
     */
    public static String targetParentProjectDao = "%s.mapper";
    /**
     * 项目repository 4
     */
    public static String targetParentProjectService = "%s.repository";
    /**
     * 项目repository.impl 5
     */
    public static String targetParentProjectServiceImpl = "%s.repository.impl";
    /**
     * 项目web 6
     */
    public static String targetParentProjectWeb = "%s.web";
    /**
     * 项目domain 7
     */
    public static String targetParentProjectDomain = "%s.service";
    /**
     * 项目domain.Impl 8
     */
    public static String targetParentProjectDomainImpl = "%s.service.impl";
    /**
     * 项目app 9
     */
    public static String targetParentProjectApp = "%s.app";
    /**
     * 项目app.impl 10
     */
    public static String targetParentProjectAppImpl = "%s.app.impl";
}
