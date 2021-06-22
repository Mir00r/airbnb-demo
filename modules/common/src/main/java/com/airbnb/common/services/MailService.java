package com.airbnb.common.services;

import java.io.File;
import java.util.List;

/**
 * @author mir00r on 22/6/21
 * @project IntelliJ IDEA
 */
public interface MailService {
    boolean sendEmail(String email, String subject, String message);

    boolean sendEmail(String email, String from, String subject, String message, List<File> attachments);

    boolean sendEmail(String to, String from, String subject, String html, String mimeType, List<File> attachments);
}
