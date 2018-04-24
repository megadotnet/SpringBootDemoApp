package com.app.login.service.Impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;

import com.app.login.service.IMailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.app.login.domain.User;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 * </p>
 * @author Megdotnet
 */
@Service
@Slf4j
@ConfigurationProperties(prefix = "mailservice")
public class MailServiceImpl implements IMailService {

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    @Value("${mailservice.mailfromdisplay}}")
    private String mailFromDisplay;

    @Value("${server.port}}")
    private String serverPort;

    @Value("${server.protocol}}")
    private String serverProtocol;

    private String baseUrlValue;

    public MailServiceImpl(JavaMailSender javaMailSender, MessageSource messageSource, SpringTemplateEngine templateEngine) {

        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Override
    @PostConstruct
    public void baseUrlInit() throws UnknownHostException {
        this.baseUrlValue = serverProtocol.concat("://").concat(InetAddress.getLocalHost()
           .getHostAddress()).concat(":").concat(serverPort);
    }

    @Override
    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}", isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(mailFromDisplay);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }

    @Override
    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, baseUrlValue);
        String content = templateEngine.process(templateName, context);
        sendEmail(user.getEmail(), "sample title", content, false, true);

    }

    @Override
    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "activationEmail", "email.activation.title");
    }

    @Override
    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "creationEmail", "email.activation.title");
    }

    @Override
    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "passwordResetEmail", "email.reset.title");
    }
}
