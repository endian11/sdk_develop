package com.travelrely.v2;

import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Transport;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import sdk.travelrely.lib.util.ToastUtil;

/**
 * ＊ LvXin_V2
 * Created by weihaichao on 16/3/5.
 * ＊ com.travelrely.v2
 * <p/>
 * 126邮箱 POP3服务器:pop.126.com    SMTP服务器:smtp.126.com
 * 163邮箱 POP3服务器:pop.163.com    SMTP服务器:smtp.163.com
 * yahoo邮箱 POP3服务器：pop.mail.yahoo.com.cn SMTP服务器：smtp.mail.yahoo.com.cn
 * Sohu邮箱 POP3服务器：pop3.sohu.com  SMTP服务器：smtp.sohu.com
 * Gmail邮箱  POP3服务器是pop.gmail.com SMTP服务器是smtp.gmail.com
 * Q邮箱      POP3服务器：pop.qq.com SMTP服务器：smtp.qq.com
 * <p/>
 * ＊ 17:20
 */

public class MailUtil {

    public static final String MAIL_126_POP3 = "pop.126.com";
    public static final String MAIL_126_SMTP = "smtp.126.com";
    public static final String MAIL_163_POP3 = "pop.163.com";
    public static final String MAIL_163_SMTP = "smtp.163.com";
    public static final String MAIL_YAHOO_POP3 = "pop.mail.yahoo.com.cn";
    public static final String MAIL_YAHOO_SMTP = "smtp.mail.yahoo.com.cn";
    public static final String MAIL_SOHU_POP3 = "pop3.sohu.com";
    public static final String MAIL_SOHU_SMTP = "smtp.sohu.com";
    public static final String MAIL_GMAIL_POP3 = "pop.gmail.com";
    public static final String MAIL_GMAIL_SMTP = "smtp.gmail.com";
    public static final String MAIL_QQ_POP3 = "pop.qq.com";
    public static final String MAIL_QQ_SMTP = "smtp.qq.com";

    private int port = 25;  //smtp协议使用的是25号端口
    private String server; // 发件人邮件服务器
    private String user;   // 使用者账号
    private String password; //使用者密码

    //构造发送邮件帐户的服务器，端口，帐户，密码
    public MailUtil(String server, int port, String user, String passwd) {
        this.port = port;
        this.user = user;
        this.password = passwd;
        this.server = server;
    }

    public interface SendResult {
        void success();

        void faild();

        void start();
    }

    /**
     * email  手机人电子邮箱
     * subject 邮件标题
     * body 正文内容
     * paths  发送的附件路径集合
     **/
    public void sendEmail(String email, String subject, String body, SendResult result) {
        if (result != null) result.start();
        Properties props = new Properties();
        props.put("mail.smtp.host", server);
        props.put("mail.smtp.port", String.valueOf(port));
        props.put("mail.smtp.auth", "true");
        Transport transport = null;
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage msg = new MimeMessage(session);
        try {
            transport = session.getTransport("smtp");
            transport.connect(server, user, password);    //建立与服务器连接
            msg.setSentDate(new Date());
            InternetAddress fromAddress = null;
            fromAddress = new InternetAddress(user);
            msg.setFrom(fromAddress);
            InternetAddress[] toAddress = new InternetAddress[1];
            toAddress[0] = new InternetAddress(email);
            msg.setRecipients(Message.RecipientType.TO, toAddress);
            msg.setSubject(subject, "UTF-8");            //设置邮件标题
            MimeMultipart multi = new MimeMultipart();   //代表整个邮件邮件
            BodyPart textBodyPart = new MimeBodyPart();  //设置正文对象
            textBodyPart.setText(body);                  //设置正文
            multi.addBodyPart(textBodyPart);             //添加正文到邮件
            msg.setContent(multi);                      //将整个邮件添加到message中
            msg.saveChanges();
            transport.sendMessage(msg, msg.getAllRecipients());  //发送邮件
            transport.close();

            if (result != null) result.success();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            if (result != null) result.faild();
        } catch (MessagingException e) {
            e.printStackTrace();
            if (result != null) result.faild();
        }
    }
}