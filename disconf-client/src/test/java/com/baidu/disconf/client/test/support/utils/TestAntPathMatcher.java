package com.baidu.disconf.client.test.support.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * TestAntPathMatcher
 * 两种较为通用的spring 资源访问方式
 */
public class TestAntPathMatcher {

    @Test
    public void ResourceLoaderTest() throws Exception {
        /* 资源地址表达式
         * classpath:相对于类的根路径，可访问jar或zip中的资源哦
         * classpath*:和上面类似，只不过上面是加载找到的第一个资源，这个是全部加载
         * file:文件系统目录中加载，可以是绝对，也可以是相对
         * http:// 不用多说了吧
         * ftp:// 不用多说了吧
         *
         * ant风格：可以使用通配符
         *  ?:匹配一个字符
         *  *:匹配多个字符
         *  **:匹配多层路径
         * */
        ResourcePatternResolver rpr = new PathMatchingResourcePatternResolver();
        Resource[] rs = rpr.getResources("classpath:testXml.xml");
        for (Resource one : rs) {
            showResourceInfo(one, true);
        }
        System.out.println("=============================");

        //file:访问文件系统（绝对 和 相对路径方式）
        //绝对路径 类似于FileSystemResource
        rs = rpr.getResources("file:src/test/resources/res/testXml.xml");
        //相对路径 相对于当前项目路径
        //rs=rpr.getResources("file:src/aop.xml");
        for (Resource one : rs) {
            showResourceInfo(one, true);
        }
        System.out.println("=============================");

        //http:方式
        rs = rpr.getResources("http://www.baidu.com/img/bdlogo.gif");
        //为了测试的简便，这里直接取第一个资源
        byte[] gifByte = IOUtils.toByteArray(rs[0].getInputStream());
        FileUtils.writeByteArrayToFile(new File("tmp/bdlogo1.gif"), gifByte);
    }

    /**
     * 公共的现实文件信息方法
     *
     * @param one           spring resource对象
     * @param isShowContent 是否打印文件内容（非文本下不建议使用）
     */
    private void showResourceInfo(Resource one, boolean isShowContent) {
        try {
            System.out.println("文件名称:" + one.getFilename());
            System.out.println("是否存在:" + one.exists());
            if (isShowContent) {
                System.out.println("文件绝对路径:" + one.getFile().getAbsolutePath());
                System.out.println("文件内容:");
                System.out.println(IOUtils.toString(one.getInputStream(), "GB2312"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
