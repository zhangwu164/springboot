package com.youedata.model;

import lombok.Data;

/**
 *mp 代码生成器实体类
 */
@Data
public class CodeGeneratorEntity {
    private  String[] tableName;//表名

    private String author;//作者

    private String outputDir;//输出路径

    private String url;//"jdbc:mysql://localhost:3306/dsap?useUnicode=true&useSSL=false&serverTimezone=UTC"

    private String userName;//mysql用户名

    private String password;//mysql密码

    private String packageName;//"com.youue.evaluate"

    private String dao;

    private String service;

    private String controller;

    private String entity;

    private String mapper;

    public CodeGeneratorEntity(String[] tableName, String author, String outputDir, String url, String userName, String password, String packageName, String dao, String service, String controller, String entity, String mapper) {
        this.tableName = tableName;
        this.author = author;
        this.outputDir = outputDir;
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.packageName = packageName;
        this.dao = dao;
        this.service = service;
        this.controller = controller;
        this.entity = entity;
        this.mapper = mapper;
    }
}
