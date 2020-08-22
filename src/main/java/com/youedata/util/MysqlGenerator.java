package com.youedata.util;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.youedata.model.CodeGeneratorEntity;


public class MysqlGenerator {

    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();

        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("zw");   // 作者
        gc.setOpen(false);      //生成代码后是否打开文件夹
        gc.setFileOverride(true); // 是否覆盖已有文件
        gc.setSwagger2(true);  // 实体属性 Swagger2 注解
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setBaseResultMap(true); // mapper.xml中生成BaseResultMap
        gc.setActiveRecord(true);
        gc.setBaseColumnList(true);

        mpg.setGlobalConfig(gc);

        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/mybatis-plus-demo?useUnicode=true&serverTimezone=GMT&useSSL=false&characterEncoding=utf8");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("sys"); // 模块名称, 这里可以根据不同模块来写
        pc.setParent("com.lcy.demo"); // 父包名
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperControllerClass("com.lcy.demo.sys.controller.BaseController");
        strategy.setSuperEntityClass("com.lcy.demo.sys.entity.BaseEntity");
        strategy.setEntityLombokModel(true);
        strategy.setInclude("user");  // 如果要生成多个,这里可以传入String[]
        mpg.setStrategy(strategy);
        mpg.execute();
    }
}
