package com.soong.utils.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 使用 smtp 协议发邮件工具类，使用此工具类需要提供一个邮箱配置文件，具体内容为
 * #发件方服务器
 * mail.smtp.host=smtp.126.com
 * #发件人的账号
 * mail.user=jiandongsong@126.com
 * #发件人的密码
 * mail.password=464099ya198512
 * #需要验证用户名密码
 * mail.smtp.auth=true
 */
public final class MailUtils {
    public static Properties prop = new Properties();
    //读取配置文件
    static {
        InputStream is = MailUtils.class.getClassLoader().getResourceAsStream("mail.properties");
        try {
            assert is != null;
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param to    收件人邮箱
     * @param text  邮件正文
     * @param title 标题
     */
    /* 发送验证信息的邮件 */
    public static boolean sendMail(String to, String text, String title) {
        try {
            // 构建授权信息，用于进行SMTP进行身份验证
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    // 用户名、密码
                    String userName = prop.getProperty("mail.user");
                    String password = prop.getProperty("mail.password");
                    return new PasswordAuthentication(userName, password);
                }
            };
            // 使用环境属性和授权信息，创建邮件会话
            Session mailSession = Session.getInstance(prop, authenticator);
            // 创建邮件消息
            MimeMessage message = new MimeMessage(mailSession);
            // 设置发件人
            String username = prop.getProperty("mail.user");
            InternetAddress form = new InternetAddress(username);
            message.setFrom(form);

            // 设置收件人
            InternetAddress toAddress = new InternetAddress(to);
            message.setRecipient(Message.RecipientType.TO, toAddress);

            // 设置邮件标题
            message.setSubject(title);

            // 设置邮件的内容体
            message.setContent(text, "text/html;charset=UTF-8");
            // 发送邮件
            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
