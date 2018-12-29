package dontai;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.tools.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoCompiler {
    private final Log log = LogFactory.getLog(AutoCompiler.class);

    /**
     * Description: Compile java source file to java class with run method
     *
     * @param fullFileName the java source file name with full path
     * @return true-compile successfully, false - compile unsuccessfully
     *
     */
    public boolean compileFile(String fullFileName) {
        boolean bRet = false;
        // get compiler
        JavaCompiler oJavaCompiler = ToolProvider.getSystemJavaCompiler();
        // compile the java source code by run method
        int iCompileRet = oJavaCompiler.run(null, null, null, fullFileName);
        // set compile result
        if (0 == iCompileRet) {
            bRet = true;
        }
        return bRet;
    }

    /**
     * Description: Compile java source file to java class with getTask
     * method, it can specify the class output path and catch diagnostic
     * information
     *
     * @param fullFileName the java source file name with full path
     * @param outputPath   the output path of java class file
     * @return true-compile successfully, false - compile unsuccessfully
     * @throws IOException
     */
    public boolean compileFile(String fullFileName, String outputPath) {
        System.out.println("源文件路径：" + fullFileName);
        System.out.println("输入路径：" + outputPath);
        boolean bRet = false;
        // get compiler
        // TODO 当运行环境是JRE是无法直接ToolProvider.getSystemJavaCompiler();这么获取
        JavaCompiler oJavaCompiler = ToolProvider.getSystemJavaCompiler();

        // define the diagnostic object, which will be used to save the diagnostic information
        DiagnosticCollector<JavaFileObject> oDiagnosticCollector = new DiagnosticCollector<JavaFileObject>();

        // get StandardJavaFileManager object, and set the diagnostic for the object
        StandardJavaFileManager oStandardJavaFileManager = oJavaCompiler
                .getStandardFileManager(oDiagnosticCollector, null, null);

        // set class output location
        JavaFileManager.Location oLocation = StandardLocation.CLASS_OUTPUT;
        try {
            File outputFile = new File(outputPath);
            if (!outputFile.exists()) {
                outputFile.mkdir();
            }
            List<File> dependencies = new ArrayList<File>();

            // 加载依赖的jar文件
            dependencies.addAll(getJarFiles(AutoCodeConstant.JARS_PATH));
            //System.out.println(getJarFiles(AutoCodeConstant.JARS_PATH));
            // 加载依赖的class文件
            dependencies.add(new File(AutoCodeConstant.BASE_PATH));
            //System.out.println(AutoCodeConstant.BASE_PATH);


            oStandardJavaFileManager.setLocation(oLocation, Arrays
                    .asList(new File[]{outputFile}));
            oStandardJavaFileManager.setLocation(StandardLocation.CLASS_PATH, dependencies);

            File sourceFile = new File(fullFileName);
            // get JavaFileObject object, it will specify the java source file.
            Iterable<? extends JavaFileObject> oItJavaFileObject = oStandardJavaFileManager
                    .getJavaFileObjectsFromFiles(Arrays.asList(sourceFile));
            // -g 生成所有调试信息
            // -g:none 不生成任何调试信息
            // -g:{lines,vars,source} 只生成某些调试信息
            // -nowarn 不生成任何警告
            // -verbose 输出有关编译器正在执行的操作的消息
            // -deprecation 输出使用已过时的 API 的源位置
            // -classpath <路径> 指定查找用户类文件的位置
            // -cp <路径> 指定查找用户类文件的位置
            // -sourcepath <路径> 指定查找输入源文件的位置
            // -bootclasspath <路径> 覆盖引导类文件的位置
            // -extdirs <目录> 覆盖安装的扩展目录的位置
            // -endorseddirs <目录> 覆盖签名的标准路径的位置
            // -d <目录> 指定存放生成的类文件的位置
            // -encoding <编码> 指定源文件使用的字符编码
            // -source <版本> 提供与指定版本的源兼容性
            // -target <版本> 生成特定 VM 版本的类文件
            // -version 版本信息
            // -help 输出标准选项的提要
            // -X 输出非标准选项的提要
            // -J<标志> 直接将 <标志> 传递给运行时系统

            // 编译选项，将编译产生的类文件放在当前目录下
            //Iterable<String> options = Arrays.asList("-encoding", AutoCodeConstant.CONTENT_ENCODING, "-classpath", getJarFiles(AutoCodeConstant.JARS_PATH));
            Iterable<String> options = Arrays.asList("-encoding", AutoCodeConstant.CONTENT_ENCODING);
            // compile the java source code by using CompilationTask's call method
            bRet = oJavaCompiler.getTask(null, oStandardJavaFileManager,
                    oDiagnosticCollector, options, null, oItJavaFileObject).call();

            //print the Diagnostic's information
            for (Diagnostic oDiagnostic : oDiagnosticCollector
                    .getDiagnostics()) {
//                log.info("Error on line: "
//                        + oDiagnostic.getLineNumber() + "; URI: "
//                        + oDiagnostic.getSource().toString());
                System.out.printf(
                        "Code: %s%n" +
                                "Kind: %s%n" +
                                "Position: %s%n" +
                                "Start Position: %s%n" +
                                "End Position: %s%n" +
                                "Source: %s%n" +
                                "Message: %s%n",
                        oDiagnostic.getCode(), oDiagnostic.getKind(),
                        oDiagnostic.getPosition(), oDiagnostic.getStartPosition(),
                        oDiagnostic.getEndPosition(), oDiagnostic.getSource(),
                        oDiagnostic.getMessage(null));
            }
        } catch (IOException e) {
            log.error("动态编译出现异常", e);
        } finally {
            //close file manager
            if (null != oStandardJavaFileManager) {
                try {
                    oStandardJavaFileManager.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("动态编译关闭文件管理器出现异常", e);
                }
            }
        }
        return bRet;
    }

    /**
     * 查找该目录下的所有的jar文件
     *
     * @param jarPath
     * @throws Exception
     */
    private List<File> getJarFiles(String jarPath) {
        final List<File> dependencyJars = new ArrayList<File>();
        File sourceFile = new File(jarPath);
        if (sourceFile.exists()) {// 文件或者目录必须存在
            if (sourceFile.isDirectory()) {// 若file对象为目录
                // 得到该目录下以.java结尾的文件或者目录
                File[] childrenFiles = sourceFile.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {
                        if (pathname.isDirectory()) {
                            return true;
                        } else {
                            String name = pathname.getName();
                            if (name.endsWith(".jar") ? true : false) {
                                //jars[0] = jars[0] + pathname.getPath() + ";";
                                dependencyJars.add(pathname);
                                return true;
                            }
                            return false;
                        }
                    }
                });
            }
        }
        return dependencyJars;
    }

    /**
     * 利用反射，实例化对象，此方法可指定class路径，放在classpath下可能会和jdk编译的文件冲突
     * @param packPath
     */
    public void java(String packPath){
        URL[] urls = null;
        try {
            //类路径,url的本地文件格式需要加file:/
            urls = new URL[] {new URL("file:\\"+AutoCodeConstant.JARS_PATH+"\\")};
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //类加载器
        URLClassLoader url = new URLClassLoader(urls);
        Class clazz = null;
        try {
            //加载到内存
            clazz = url.loadClass(packPath);
            //实例化对象
            clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AutoCompiler autoCompiler = new AutoCompiler();
        autoCompiler.compileFile("F:\\ST\\text_nn_3.java",AutoCodeConstant.JARS_PATH);
        autoCompiler.java("text_nn_3");
    }
}
