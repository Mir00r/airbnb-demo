package com.airbnb.common.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

/**
 * @author mir00r on 22/6/21
 * @project IntelliJ IDEA
 */
@Service
public class MailServiceBean implements MailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailServiceBean(@Qualifier("javaMailSenderBean") JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public boolean sendEmail(String email, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        try {
            new Thread(() -> javaMailSender.send(mailMessage)).start();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    @Override
    public boolean sendEmail(String to, String from, String sub, String msgBody, List<File> attachments) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            if (from != null)
                helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(sub);
            helper.setText("\n" + msgBody + "\n");
            if (attachments != null)
                for (File a : attachments)
                    helper.addAttachment(a.getName(), new FileSystemResource(a));

            new Thread(() -> javaMailSender.send(message)).start();
        } catch (MessagingException e) {
            System.out.println(e.toString());
            return false;
        }
        return true;
    }

    /**
     * Multipart HTML email, supports attachments
     *
     * @param to
     * @param from
     * @param subject
     * @param content
     * @param mimeType    currently supported mimetype: "text/html"
     * @param attachments
     * @return
     */
    @Override
    public boolean sendEmail(String to, String from, String subject, String content, String mimeType, List<File> attachments) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
        mimeType = mimeType + "; charset=\"utf-8\"";
        try {
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessage.setContent(content, mimeType);
            if (attachments != null) {
                attachments.forEach(a -> {
                    try {
                        mimeMessageHelper.addAttachment(a.getName(), new FileSystemResource(a));
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                });
            }
            new Thread(() -> javaMailSender.send(mimeMessage)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
