package com.youedata.util;

import com.alibaba.fastjson.JSONObject;
import com.youedata.model.EmailDto;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MailUtil {

//    @Value("${email.sender}")
//    private String sender; //发件人邮箱
//
////    发件人邮箱密码
//    @Value("${email.password}")
//    private String password;
//
//    //   定发送邮件的主机
//    @Value("${email.host}")
//    private String host;
//
//    @Value("${email.url}")
//    private String url;
//
////    邮件主题
//    @Value("${email.emailSubject}")
//    private String emailSubject;
//    //    邮件开头
//    @Value("${email.emailNickname}")
//    private String emailNickname;

    private static Logger logger = LoggerFactory.getLogger(MailUtil.class);

    final static OkHttpClient client = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.MINUTES).readTimeout(20, TimeUnit.MINUTES).build();
    final static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

//    public void sendVerificationCode(String code,String email,String activateStatus) {
//
//        Properties properties = System.getProperties();// 获取系统属性
//
//        properties.setProperty("mail.smtp.host", host);// 设置邮件服务器
//        properties.setProperty("mail.smtp.auth", "true");// 打开认证
//
//        try {
//            //QQ邮箱需要下面这段代码，163邮箱不需要
////			MailSSLSocketFactory sf = new MailSSLSocketFactory();
////			sf.setTrustAllHosts(true);
////			properties.put("mail.smtp.ssl.enable", "true");
////			properties.put("mail.smtp.ssl.socketFactory", sf);
//            // 1.获取默认session对象
//            Session session = Session.getInstance(properties, new Authenticator() {
//                public PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(sender, password); // 发件人邮箱账号、授权码
//                }
//            });
//            // 2.创建邮件对象
//            Message message = new MimeMessage(session);
//            // 2.1设置发件人
//            message.setFrom(new InternetAddress(sender));
//            // 2.2设置接收人
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
//            // 2.3设置邮件主题
//            message.setSubject("修改密码");
//            // 2.4设置邮件内容
//            String content;
//            if ("0".equals(activateStatus)) {
//                content = "<html><head></head><body><h1>这是一封修改密码邮件,2分钟内有效,验证码为</h1><h3>" + code +"</h3></body></html>";
//            }else {
//                content = "<html><head></head><body><h1>这是一封激活邮件,30分钟内有效,激活请点击以下链接</h1><h3><a href='http://"+ url +"/dsap/v1/user/activeServlet?code="
//                        + code + "&email="+ email +"'>http://" + url + "/dsap/v1/user/activeServlet?code=" + Base64.getEncoder().encodeToString(code.getBytes("utf-8"))+ "&email=" + email + "</href></h3></body></html>";
//            }
//            message.setContent(content, "text/html;charset=UTF-8");
//            // 3.发送邮件
//            Transport.send(message);
//            logger.info("邮件成功发送!");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 发邮件信息
     *
     * @param accountEmail  不能为空 用户邮箱
     * @param emailUrl      不能为空 在pass上发邮件的请求地址
     * @param emailHost     不能为空 发件箱服务器
     * @param emailFrom     不能为空 发件箱地址
     * @param emailPaasword 不能为空 发件箱密码
     * @param emailSubject  不能为空 邮件主题
     * @param emailNickname 不能为空 邮件开头
     * @throws Exception
     */
    public static boolean sendEmail(String accountEmail, String emailUrl,
                                    String emailHost, String emailFrom, String emailPaasword,
                                    String emailSubject, String emailNickname, String context){
        EmailDto emailDto = new EmailDto();
        emailDto.setFrom(emailFrom);
        emailDto.setHost(emailHost);
        emailDto.setContext(context);
        emailDto.setPassword(emailPaasword);
        List list = new ArrayList();
        list.add(accountEmail);
        emailDto.setSendTo(list);
        emailDto.setNickName(emailNickname);
        emailDto.setSubject(emailSubject);
        List list1 = new ArrayList();
        emailDto.setRecipientCc(list1);
        okhttp3.RequestBody body = okhttp3.RequestBody.create(JSON, JSONObject.toJSONString(emailDto));
        Request okReq = new Request.Builder().url(emailUrl).post(body).build();
        try {
            Response okResp = client.newCall(okReq).execute();
            String msg = okResp.body().string().toString();
            JSONObject jsonObject = JSONObject.parseObject(msg);
            Integer code = jsonObject.getInteger("code");
            if (code.equals(0)) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;

        }
    }

    public static void main(String[] args) {
        try {
            String context = "<h3>尊敬的"+ "mmmmmm"  +
                    ":</h3><h3>你好！</h3><h3>你与平台合作已生效，你的平台登录账号及密码如下：账号：" + "mmmmmm"  + "，密码：" + "123456。</h3>" +
                    "<h3>请尽快登录平台：" + "mmmmmm"  + " 修改密码及绑定的联系人信息。</h3>" +
                    "<h3>若有问题，可联系负责与你签订合同的平台工作人员</h3><h3>厦门价值评价平台</h3>";
            System.out.println(sendEmail("164683866@qq.com",
                    "http://172.16.4.83:1880/communications/api/v1/email", "smtp.youedata.com",
                    "info-paas@youedata.com", "Youedata2018",
                    "厦门价值评价系统用户服务", "用户服务", context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
