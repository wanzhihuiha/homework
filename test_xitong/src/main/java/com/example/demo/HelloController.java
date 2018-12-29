package com.example.demo;

import com.example.demo.Mapper.test_report_mapper;
import com.example.demo.Model.test_report;
import dontai.AutoCodeConstant;
import dontai.AutoCompiler;
import dontai.CodeGenerate;
import dontai.copyfile;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.expression.Lists;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.testng.*;


@Controller
public class HelloController {

    @Autowired
    private test_report_mapper AA;

    @RequestMapping("/updata")
    public String index() {
        System.out.println("begin");
        return "updata";
    }


    //AJAX操作，读取数据库的内容
    @RequestMapping(value = "/sqldata",method = RequestMethod.POST)
    @ResponseBody
    public void searchsql(HttpServletResponse resp){
        System.out.println("AJAX-search data from mysql");
        List<test_report> list = new ArrayList<>();
        list=AA.gettest_report();
        try {
            /*将list集合装换成json对象*/
            JSONArray data = JSONArray.fromObject(list);
            //接下来发送数据
            /*设置编码，防止出现乱码问题*/
            resp.setCharacterEncoding("utf-8");
            /*得到输出流*/
            PrintWriter respWritter = resp.getWriter();
            /*将JSON格式的对象toString()后发送*/
            respWritter.append(data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //运行本地测试文件并把报告复制到文件夹中，然后把路径加入数据库
    @RequestMapping(value = "/run3", method = RequestMethod.GET)
    public String run3(@RequestParam("PAT") String PAT) throws Exception{
        System.out.println(PAT);
        String READER_name = StringUtils.substringBefore(PAT, ".");
        String READER_PATH1 = this.getClass().getResource("/").getPath() + "tss/"+ PAT;
        //文件输出路径
        String WRITER_PATH = this.getClass().getResource("/").getPath() + "tss/"+READER_name+".java";
        //包路径
        String PACK_PATH = READER_name;
        //
        String clas_PATH = this.getClass().getResource("/").getPath() + "tss/";
        CodeGenerate code = new CodeGenerate();
        AutoCompiler AutoCompiler=new AutoCompiler();
        //读文本文件
        BufferedReader br = code.fileReader(READER_PATH1);
        //生成java类
        code.fileWriter(br,WRITER_PATH);
        //编译java类
        AutoCompiler.compileFile(WRITER_PATH,clas_PATH);
        String japath="E:\\TOMCAT8\\apache-tomcat-8.0.43\\apache-tomcat-8.0.43\\webapps\\demo\\WEB-INF\\lib\\testng-7.0.0-beta1.jar";
        String japath1="E:\\TOMCAT8\\apache-tomcat-8.0.43\\apache-tomcat-8.0.43\\webapps\\demo\\WEB-INF\\lib\\jcommander-1.72.jar";
        //加载jar包
        code.loadJar(japath);
        Class<?> aClass = Class.forName("org.testng.TestNG");
        code.loadJar(japath1);
        String japath2="E:\\TOMCAT8\\apache-tomcat-8.0.43\\apache-tomcat-8.0.43\\webapps\\demo\\WEB-INF\\lib\\";
        code.loadJar(japath2+"selenium-api-3.14.0.jar");
        code.loadJar(japath2+"selenium-chrome-driver-3.14.0.jar");
        code.loadJar(japath2+"selenium-edge-driver-3.14.0.jar");
        code.loadJar(japath2+"selenium-firefox-driver-3.14.0.jar");
        code.loadJar(japath2+"selenium-htmlunit-driver-2.52.0.jar");
        code.loadJar(japath2+"selenium-ie-driver-3.14.0.jar");
        code.loadJar(japath2+"selenium-java-2.52.0.jar");
        code.loadJar(japath2+"selenium-leg-rc-2.52.0.jar");
        code.loadJar(japath2+"selenium-remote-driver-3.14.0.jar");
        code.loadJar(japath2+"selenium-safari-driver-3.14.0.jar");
        code.loadJar(japath2+"selenium-support-3.14.0.jar");
        code.loadJar(japath2+"commons-exec-1.3.jar");
        code.loadJar(japath2+"guava-25.0-jre.jar");
        code.loadJar(japath2+"okhttp-3.10.0.jar");
        code.loadJar(japath2+"okio-1.14.1.jar");

        //运行java类
        code.java(READER_name);
        copyfile copyf =new copyfile();
        String mkdir_PATH=this.getClass().getResource("/").getPath()+"\\templates\\"+READER_name;

        String aa=this.getClass().getResource("/").getPath();

        copyf.mkdir(mkdir_PATH);//创建以测试名为名的文件夹
        copyf.copyFile(aa+"\\index.html",mkdir_PATH);//复制报告页面
        String sta_PAth="E:\\TOMCAT8\\apache-tomcat-8.0.43\\apache-tomcat-8.0.43\\webapps\\demo\\";
        copyf.copyFile(aa+"\\collapseall.gif",sta_PAth);
        copyf.copyFile(aa+"\\jquery-1.7.1.min.js",sta_PAth);
        copyf.copyFile(aa+"\\testng-reports.css",sta_PAth);
        copyf.copyFile(aa+"\\testng-reports.js",sta_PAth);


        test_report test_report=new test_report();
        //数据库地址
        String insert_PATH=this.getClass().getResource("/").getPath()+"templates/"+READER_name+"/index.html";
        test_report.setTestname(PAT);
        test_report.setReportpath(insert_PATH);//数据库操作
        AA.updata_report(test_report);
        return "updata";
    }

    //查看报告
    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public String lookreport(@RequestParam("name") String name)throws Exception{
        String READER_name = StringUtils.substringBefore(name, ".");
        return READER_name+"/index";
    }


    //xml配置mybatis
    @RequestMapping(value="/get")
    @ResponseBody
    public List<test_report> getUsers() {
        List<test_report> userList = AA.gettest_report();
        return userList;

    }

}
