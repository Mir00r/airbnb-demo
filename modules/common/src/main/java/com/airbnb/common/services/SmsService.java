package com.airbnb.common.services;

/**
 * @author mir00r on 22/6/21
 * @project IntelliJ IDEA
 */
public interface SmsService {
    boolean sendSms(String phoneNumber, String message);
}
