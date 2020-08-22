package com.youedata.model;

import lombok.Data;

import java.util.List;

@Data
public class EmailDto {

    /**
     * 邮件服务器
     */
    private String host;
    /**
     * 发件人
     */
    private String from;
    /**
     * 密码
     */
    private String password;
    /**
     * 发件人名称
     */
    private String nickName;
    /**
     * 收件人
     */
    private List sendTo;
    /**
     * 主题
     */
    private String subject;
    /**
     * 抄送
     */
    private List recipientCc;
    /**
     * 邮件内容
     */
    private String context;
}

