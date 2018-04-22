package com.app.login.service;

import com.app.login.domain.User;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.PostConstruct;
import java.net.UnknownHostException;

/**
 * Created by
 * @authr Administrator
 * @date 2018/4/22 0022.
 */
public interface IMailService {

    @PostConstruct
    void baseUrlInit() throws UnknownHostException;

    /**
     *  sendEmail
     * @param to mail address
     * @param subject subject
     * @param content content
     * @param isMultipart isMultipart
     * @param isHtml is html format
     */
    @Async
    void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml);

    @Async
    void sendEmailFromTemplate(User user, String templateName, String titleKey);

    @Async
    void sendActivationEmail(User user);

    @Async
    void sendCreationEmail(User user);

    @Async
    void sendPasswordResetMail(User user);
}
