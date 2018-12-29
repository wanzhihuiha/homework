package dontai;

public class AutoCodeConstant {
    /**
     * 登录模板名称
     */
    public static String LOGIN_TEMPLATE_FTL = "LoginClientTemplate.ftl";
    /**
     * 项目根路径
     */
    public final static String BASE_PATH = AutoCodeConstant.class.getResource("/").toString().replaceAll("^file:/", "");

    /**
     * Jar包所在目录
     */
    public final static String JARS_PATH ="E:\\TOMCAT8\\apache-tomcat-8.0.43\\apache-tomcat-8.0.43\\webapps\\demo\\WEB-INF\\lib";

            //BASE_PATH.replace("classes","lib");
    /**
     * 模板根路径
     */
    public final static String BASE_TEMPLATE_PATH = BASE_PATH + "template";
    /**
     * client目录
     */
    //public final static String LOCAL_CLIENT_BASE_PATH = "src/main/java/com/lkb/sb/client/";
    /**
     * client目录
     */
    //public final static String CLIENT_BASE_PATH = "com/lkb/sb/client/";
    /**
     * client类名后缀
     */
    public final static String LOGIN_CLIENT_SUFFIX = "LoginClient.java";
    /**
     * 默认编译输出路径
     */
    public final static String DEFULT_OUTPUT_PATH = "/";
    /**
     * 编码格式
     */
    public final static String CONTENT_ENCODING = "UTF-8";


}
