package com.aliveseven.adminmanage.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;

/**
 * mp代码生成器
 * by AliveSeven
 * @since  2022-10-13
 * **/
public class CodeGenerator {

    @Value("${spring.datasource.url}")
    private static String url;

    @Value("${spring.datasource.username}")
    private static String username;

    @Value("${spring.datasource.password}")
    private static String password;

    @Value("${code.generator.path}")
    private static String path;

    @Value("${code.generator.mapper.path}")
    private static String mapperPath;

    public static void main(String[] args){
        generate();
    }

    public static void generate(){
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/admin_manager?serverTimezone=Asia/Shanghai", "root", "root")
                .globalConfig(builder -> {
                    builder.author("AliveSeven") // 设置作者
                            // .fileOverride() // 覆盖已生成文件
                            .outputDir("C:\\Program\\MyProject\\AdminManage\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.aliveseven.adminmanage") // 设置父包名
                            .moduleName("") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "C:\\Program\\MyProject\\AdminManage\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok();
                    builder.mapperBuilder().enableMapperAnnotation().build();
                    builder.controllerBuilder().enableHyphenStyle()  // 开启驼峰转连字符
                            .enableRestStyle();  // 开启生成@RestController 控制器
                    builder.addInclude("sys_todolist") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_" , "sys_"); // 设置过滤表前缀
                })
                 //.templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }


}
