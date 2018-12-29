package dontai;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.URI;

public class copyfile {

    /** */
    /**
     * 复制文件
     *
     * @param destDirName 要创建的文件夹  格式如："F:\\ideadata\\test_xitong_report\\tuidj"
     *                    其中F:\\ideadata\\test_xitong_report为它的父文件夹，tuidj为要创建的文件夹名称
     */
    public void mkdir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");

        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            System.out.println("创建目录" + destDirName + "成功！");

        } else {
            System.out.println("创建目录" + destDirName + "失败！");
        }

    }

    /** */
    /**
     * 复制文件
     *
     * @param srcPathStr 原文件具体地址
     * @param desPathStr 目标文件夹
     */
    public void copyFile(String srcPathStr, String desPathStr) {
        //1.获取源文件的名称
        String newFileName = srcPathStr.substring(srcPathStr.lastIndexOf("\\") + 1); //目标文件地址
        //System.out.println(newFileName);
        desPathStr = desPathStr + File.separator + newFileName; //源文件地址
        //System.out.println(desPathStr);

        try {
            //2.创建输入输出流对象
            FileInputStream fis = new FileInputStream(srcPathStr);
            FileOutputStream fos = new FileOutputStream(desPathStr);

            //创建搬运工具
            byte datas[] = new byte[1024 * 8];
            //创建长度
            int len = 0;
            //循环读取数据
            while ((len = fis.read(datas)) != -1) {
                fos.write(datas, 0, len);
            }
            //3.释放资源
            fis.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** */
    /**
     * 文件重命名
     *
     * @param path    文件目录
     * @param oldname 原来的文件名
     * @param newname 新文件名
     */
    public void renameFile(String path, String oldname, String newname) {
        if (!oldname.equals(newname)) {//新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(path + "/" + oldname);
            File newfile = new File(path + "/" + newname);
            System.out.println(oldname + newname);
            if (!oldfile.exists()) {
                return;//重命名文件不存在
            }
            if (newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                System.out.println(newname + "已经存在！");
            else {
                oldfile.renameTo(newfile);
            }
        } else {
            System.out.println("新文件名和旧文件名相同...");
        }
    }


    /** */
    /**
     * 打开本地的HTML页面
     */
    public void openhtml(String url) throws Exception {
        String osName = System.getProperty("os.name", "");
        if (osName.startsWith("Mac OS")) {
            //苹果的打开方式
            Class fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
            openURL.invoke(null, new Object[] { url });
        } else if (osName.startsWith("Windows")) {
            //windows的打开方式。
            //System.out.println("111");
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        } else {
            // Unix or Linux的打开方式
            String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
            String browser = null;
            for (int count = 0; count < browsers.length && browser == null; count++)
                //执行代码，在brower有值后跳出，
                //这里是如果进程创建成功了，==0是表示正常结束。
                if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0)
                    browser = browsers[count];
            if (browser == null)
                throw new Exception("Could not find web browser");
            else
                //这个值在上面已经成功的得到了一个进程。
                Runtime.getRuntime().exec(new String[] { browser, url });
        }
    }

}

