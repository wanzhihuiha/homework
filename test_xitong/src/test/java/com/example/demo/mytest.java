package com.example.demo;


import com.example.demo.Mapper.test_report_mapper;
import com.example.demo.Model.test_report;
import dontai.copyfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class mytest {
    copyfile co =new copyfile();
    @Autowired
    private test_report_mapper AA;
    @Test
    public void find(){
        List<test_report> list = AA.gettest_report();
        System.out.println(list);
    }

    @Test
    //新建文件夹测试
    public void mkdir(){
        String aa="F:\\ideadata\\test_xitong_report\\";
        String bb="tuidj";
        String a=aa+bb;
        co.mkdir(a);
    }

    @Test
    //复制文件测试
    public void copy(){

        String aa="F:\\ideadata\\test_xitong\\test-output\\index.html";
        String bb="F:\\ideadata\\test_xitong_report\\tuidj";
        co.copyFile(aa,bb);
    }

    @Test
    //问价重命名测试（不能改后缀为html的文件，所以没啥用）
    public void rename(){
        String bb="F:\\ideadata\\test_xitong_report";
        String cc="index";
        String dd="con";
        co.renameFile(bb,cc,dd);
    }

    @Test
    //打开本地html页面的测试
    public void openhtml()throws Exception{
        String aa="F:\\ideadata\\test_xitong_report\\CodeText\\index.html";
        co.openhtml(aa);
    }
}
