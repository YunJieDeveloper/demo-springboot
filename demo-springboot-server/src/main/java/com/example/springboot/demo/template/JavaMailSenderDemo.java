package com.example.springboot.demo.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;

/***
 * @Date 2017/11/29
 *@Description spring 邮件发送案例
 *             生产环境时，可通过定时任务实现定时发送监控邮件等功能
 * @author zhanghesheng
 * */

@Component //开启spring扫描
@Slf4j
public class JavaMailSenderDemo {
    private final static String CHARSET_UTF8 = "UTF-8";

    @Autowired
    private JavaMailSender javaMailSender;


    @Autowired
    Environment env;


    @Autowired
    //thymeleaf 模板引擎
    private TemplateEngine templateEngine;

    /**
     * @param isHtml 是否是html格式(发送html时使用utf-8编码)
     * @throws MessagingException 发送发生了异常
     * @方法名: sendMail
     * @参数名：@param subject 邮件主题
     * @参数名：@param content 邮件主题内容
     * @参数名：@param to 收件人Email地址
     * @描述语: 发送邮件
     */
    public void sendMail(String subject, String content, boolean isHtml, String[] to) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper messageHelper = null;
        if (isHtml) {
            messageHelper = new MimeMessageHelper(mimeMessage, true, CHARSET_UTF8);
        } else {
            messageHelper = new MimeMessageHelper(mimeMessage, true);
        }
        messageHelper.setFrom(env.getProperty("mail.from", String.class, "934606199@qq.com")); // 设置发件人Email
        messageHelper.setSubject(subject); // 设置邮件主题
        if (isHtml) {
            messageHelper.setText(content, true); // 设置邮件主题内容(html格式)
        } else {
            messageHelper.setText(content); // 设置邮件主题内容
        }

        messageHelper.setTo(to); // 设定收件人Email

        javaMailSender.send(mimeMessage);
    }


    /**
     * @param subject      邮件主题
     * @param templateName 前端页面文件名
     * @param datas        前后台交互的数据
     * @param isHtml       是否是html格式(发送html时使用utf-8编码)
     * @throws MessagingException 发送发生了异常
     * @方法名: sendMail
     * @参数名：@param to 收件人Email地址
     * @描述语: 使用模板引擎发送邮件
     */
    public void sendMailwithTemplate(String subject, String templateName, Map<String, Object> datas, boolean isHtml, String[] to) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper messageHelper = null;
        if (isHtml) {
            messageHelper = new MimeMessageHelper(mimeMessage, true, CHARSET_UTF8);
        } else {
            messageHelper = new MimeMessageHelper(mimeMessage, true);
        }
        messageHelper.setFrom(env.getProperty("mail.from", String.class, "934606199@qq.com"));
        messageHelper.setSubject(subject);

        // /**spring 5以上版本不支持velocity*/
        //VelocityEngine velocityEngine = new VelocityEngine();
        //model：变量参数传递给前端页面
        //String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "data-verify.vm","UTF-8", model)
        // 设置邮件主题内容(html格式)
        messageHelper.setText(buildMessage(templateName, datas), true);
        messageHelper.setTo(to);

        javaMailSender.send(mimeMessage);
    }


    /**
     * 发送邮件 （包含附件）
     *
     * @param subject        主题
     * @param content        内容
     * @param fileAttachment 附件文件(外部文件，譬如C:/myfile/filename)
     * @param isHtml         内容是否是html格式
     * @param to             发送的邮箱地址
     * @throws MessagingException 发送邮件异常（失败）
     */
    public void sendMail(String subject, String content, boolean isHtml, File fileAttachment, String to)
            throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        messageHelper.setFrom(env.getProperty("mail.from", String.class, "15939630623@163.com")); // 设置发件人Email
        messageHelper.setSubject(subject); // 设置邮件主题

        if (isHtml) {
            messageHelper.setText(content, true); // 设置邮件主题内容(html格式)
        } else {
            messageHelper.setText(content); // 设置邮件主题内容
        }
        messageHelper.setTo(to); // 设定收件人Email
        FileSystemResource file = new FileSystemResource(fileAttachment);

        messageHelper.addAttachment(file.getFilename(), file); // 添加附件
        javaMailSender.send(mimeMessage);
    }

    /**
     * 发送邮件 （包含附件）
     *
     * @param subject           主题
     * @param content           内容
     * @param classPathResource 附件文件(附加在项目内部时候 resources/filename)
     * @param isHtml            内容是否是html格式
     * @param to                发送的邮箱地址
     * @throws MessagingException
     */
    public void sendMail(String subject, String content, boolean isHtml, ClassPathResource classPathResource, String to)
            throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        /**
         * new MimeMessageHelper(mimeMessage,true)之true个人见解：
         * 关于true参数,官方文档是这样解释的： use the true flag to indicate you need a
         * multipart message 翻译过来就是：使用true,以表明你需要多个消息
         * 再去翻一下MimeMessageHelper的API,找到这样一句话： supporting alternative texts,
         * inline elements and attachments 也就是说,如果要支持内联元素和附件就必须给定第二个参数为true 否则抛出
         * java.lang.IllegalStateException 异常
         */
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(env.getProperty("mail.from", String.class, "15939630623@163.com")); // 设置发件人Email
        messageHelper.setSubject(subject); // 设置邮件主题
        if (isHtml) {
            messageHelper.setText(content, true); // 设置邮件主题内容(html格式)
        } else {
            messageHelper.setText(content); // 设置邮件主题内容
        }
        messageHelper.setTo(to); // 设定收件人Email
        /**
         * FileSystemResource file = new FileSystemResource(fileAttachment);
         *
         * ClassPathResource：很明显就是类路径资源,我这里的附件是在项目里的,所以需要用ClassPathResource
         * 如果是系统文件资源就不能用ClassPathResource,而要用FileSystemResource,例：
         *
         *
         * ClassPathResource file = new
         * ClassPathResource("attachment/Readme.txt");
         */
        /**
         * MimeMessageHelper的addAttachment方法： addAttachment(String
         * attachmentFilename, InputStreamSource inputStreamSource)
         * InputStreamSource是一个接口,ClassPathResource和FileSystemResource都实现了这个接口
         *
         *
         * //发送附件邮件
         */
        ClassPathResource file = classPathResource;
        messageHelper.addAttachment(file.getFilename(), file); // 添加附件
        javaMailSender.send(mimeMessage);

    }


    private String buildMessage(String templateName, Map<String, Object> datas) {
        Context context = new Context();
        for (Map.Entry<String, Object> entry : datas.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        return templateEngine.process(templateName, context);
    }

}


