package com.example.demo;

import com.example.demo.Mapper.test_report_mapper;
import com.example.demo.Model.test_report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class FileController {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private test_report_mapper BB;
    @RequestMapping(value = "/upload")
    public String upload(@RequestParam("file") MultipartFile file, ModelMap map) {
        try {
            if (file.isEmpty()) {
                return "文件为空";
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();
            log.info("上传的文件名为：" + fileName);
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            log.info("文件的后缀名为：" + suffixName);
            // 设置文件存储路径
            String filePath = this.getClass().getResource("/").getPath() + "\\tss\\";
                    //"F:\\ideadata\\test_xitong_download\\";
            String path = filePath + fileName;
            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            map.addAttribute("tip", "上传成功");
            test_report test_report=new test_report();
            test_report.setTestname(fileName);
            BB.new_insert_report(test_report);//把文件名写入数据库
            return "tip";
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.addAttribute("tip", "上传失败");
        return "tip";
    }
}
