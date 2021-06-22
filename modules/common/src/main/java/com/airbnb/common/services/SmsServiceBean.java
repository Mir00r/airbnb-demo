package com.airbnb.common.services;

import com.airbnb.common.utils.AppUtil;
import com.airbnb.common.utils.NetworkUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author mir00r on 22/6/21
 * @project IntelliJ IDEA
 */
@Service
@PropertySource("classpath:sms.properties")
public class SmsServiceBean implements SmsService {

    @Value("${mimsms.apiKey}")
    String apiKey;

    @Value("${mimsms.username}")
    String username;

    @Value("${mimsms.password}")
    String password;

    @Value("${mimsms.senderId}")
    String senderId;

    @Override
    public boolean sendSms(String phoneNumber, String message) {
        try {
            String phone = AppUtil.getFormattedPhoneNumber(phoneNumber);
            String body = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());
            String url = apiKey + "?username=" + username + "&password=" + password + "&recipient=" + phone + "&from=" + senderId + "&message=" + body;

            NetworkUtil.getRequest(url, null, null);
            return true;
        } catch (IOException e) {
            System.out.println("Could not send SMS. " + e.getMessage());
            return false;
        }
    }
}
