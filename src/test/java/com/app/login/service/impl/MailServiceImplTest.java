package com.app.login.service.impl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.login.domain.User;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashSet;


import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.thymeleaf.spring6.SpringTemplateEngine;


/**
 * MailServiceImplTest
 */
@ContextConfiguration(classes = {MailServiceImpl.class})
@ExtendWith(SpringExtension.class)
class MailServiceImplTest {
    @MockBean
    private JavaMailSender javaMailSender;

    @Autowired
    private MailServiceImpl mailServiceImpl;

    @MockBean
    private MessageSource messageSource;

    @MockBean
    private SpringTemplateEngine springTemplateEngine;

    /**
     * Method under test: {@link MailServiceImpl#baseUrlInit()}
     */
    @Test
    void testBaseUrlInit() throws UnknownHostException {
        // TODO: Complete this test.
        //   Reason: R002 Missing observers.
        //   Diffblue Cover was unable to create an assertion.
        //   Add getters for the following fields or make them package-private:
        //     MailServiceImpl.baseUrlValue
        //     MailServiceImpl.javaMailSender
        //     MailServiceImpl.mailFromDisplay
        //     MailServiceImpl.messageSource
        //     MailServiceImpl.serverPort
        //     MailServiceImpl.serverProtocol
        //     MailServiceImpl.templateEngine

        mailServiceImpl.baseUrlInit();
    }

    /**
     * Method under test: {@link MailServiceImpl#sendEmail(String, String, String, boolean, boolean)}
     */
    @Test
    void testSendEmail() throws MailException {
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        mailServiceImpl.sendEmail("alice.liddell@example.org", "Hello from the Dreaming Spires",
                "Not all who wander are lost", true, true);
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }

    /**
     * Method under test: {@link MailServiceImpl#sendEmailFromTemplate(User, String, String)}
     */
    @Test
    void testSendEmailFromTemplate() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at org.thymeleaf.TemplateEngine.setMessageResolver(TemplateEngine.java:896)
        //       at org.thymeleaf.spring5.SpringTemplateEngine.initializeSpecific(SpringTemplateEngine.java:301)
        //       at org.thymeleaf.TemplateEngine.initialize(TemplateEngine.java:328)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1079)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1059)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1048)
        //       at com.app.login.service.impl.MailServiceImpl.sendEmailFromTemplate(MailServiceImpl.java:103)
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R002 Missing observers.
        //   Diffblue Cover was unable to create an assertion.
        //   Add getters for the following fields or make them package-private:
        //     MailServiceImpl.baseUrlValue
        //     MailServiceImpl.javaMailSender
        //     MailServiceImpl.mailFromDisplay
        //     MailServiceImpl.messageSource
        //     MailServiceImpl.serverPort
        //     MailServiceImpl.serverProtocol
        //     MailServiceImpl.templateEngine

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        AnnotationConfigReactiveWebApplicationContext messageSource = new AnnotationConfigReactiveWebApplicationContext();
        MailServiceImpl mailServiceImpl = new MailServiceImpl(javaMailSender, messageSource, new SpringTemplateEngine());

        User user = new User();
        user.setActivated(true);
        user.setActivationKey("Activation Key");
        user.setAuthorities(new HashSet<>());
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImageUrl("https://example.org/example");
        user.setIpAddress("42 Main St");
        user.setLangKey("Lang Key");
        user.setLastName("Doe");
        user.setLogin("Login");
        user.setPassword("iloveyou");
        user.setResetDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setResetKey("Reset Key");
        mailServiceImpl.sendEmailFromTemplate(user, "Template Name", "Dr");
    }

    /**
     * Method under test: {@link MailServiceImpl#sendEmailFromTemplate(User, String, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testSendEmailFromTemplate2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at org.thymeleaf.TemplateEngine.setMessageResolver(TemplateEngine.java:896)
        //       at org.thymeleaf.spring5.SpringTemplateEngine.initializeSpecific(SpringTemplateEngine.java:301)
        //       at org.thymeleaf.TemplateEngine.initialize(TemplateEngine.java:328)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1079)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1059)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1048)
        //       at com.app.login.service.impl.MailServiceImpl.sendEmailFromTemplate(MailServiceImpl.java:103)
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at com.app.login.service.impl.MailServiceImpl.sendEmail(MailServiceImpl.java:78)
        //       at com.app.login.service.impl.MailServiceImpl.sendEmailFromTemplate(MailServiceImpl.java:104)
        //   See https://diff.blue/R013 to resolve this issue.

        AnnotationConfigReactiveWebApplicationContext messageSource = new AnnotationConfigReactiveWebApplicationContext();
        MailServiceImpl mailServiceImpl = new MailServiceImpl(null, messageSource, new SpringTemplateEngine());

        User user = new User();
        user.setActivated(true);
        user.setActivationKey("Activation Key");
        user.setAuthorities(new HashSet<>());
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImageUrl("https://example.org/example");
        user.setIpAddress("42 Main St");
        user.setLangKey("Lang Key");
        user.setLastName("Doe");
        user.setLogin("Login");
        user.setPassword("iloveyou");
        user.setResetDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setResetKey("Reset Key");
        mailServiceImpl.sendEmailFromTemplate(user, "Template Name", "Dr");
    }

    /**
     * Method under test: {@link MailServiceImpl#sendEmailFromTemplate(User, String, String)}
     */
    @Test
    void testSendEmailFromTemplate3() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at org.thymeleaf.TemplateEngine.setMessageResolver(TemplateEngine.java:896)
        //       at org.thymeleaf.spring5.SpringTemplateEngine.initializeSpecific(SpringTemplateEngine.java:301)
        //       at org.thymeleaf.TemplateEngine.initialize(TemplateEngine.java:328)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1079)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1059)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1048)
        //       at com.app.login.service.impl.MailServiceImpl.sendEmailFromTemplate(MailServiceImpl.java:103)
        //   See https://diff.blue/R013 to resolve this issue.

        JavaMailSenderImpl javaMailSender = mock(JavaMailSenderImpl.class);
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        AnnotationConfigReactiveWebApplicationContext messageSource = new AnnotationConfigReactiveWebApplicationContext();
        MailServiceImpl mailServiceImpl = new MailServiceImpl(javaMailSender, messageSource, new SpringTemplateEngine());

        User user = new User();
        user.setActivated(true);
        user.setActivationKey("Activation Key");
        user.setAuthorities(new HashSet<>());
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImageUrl("https://example.org/example");
        user.setIpAddress("42 Main St");
        user.setLangKey("Lang Key");
        user.setLastName("Doe");
        user.setLogin("Login");
        user.setPassword("iloveyou");
        user.setResetDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setResetKey("Reset Key");
        mailServiceImpl.sendEmailFromTemplate(user, "Template Name", "Dr");
        verify(javaMailSender).createMimeMessage();
    }

    /**
     * Method under test: {@link MailServiceImpl#sendActivationEmail(User)}
     */
    @Test
    void testSendActivationEmail() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at org.thymeleaf.TemplateEngine.setMessageResolver(TemplateEngine.java:896)
        //       at org.thymeleaf.spring5.SpringTemplateEngine.initializeSpecific(SpringTemplateEngine.java:301)
        //       at org.thymeleaf.TemplateEngine.initialize(TemplateEngine.java:328)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1079)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1059)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1048)
        //       at com.app.login.service.impl.MailServiceImpl.sendEmailFromTemplate(MailServiceImpl.java:103)
        //       at com.app.login.service.impl.MailServiceImpl.sendActivationEmail(MailServiceImpl.java:112)
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R002 Missing observers.
        //   Diffblue Cover was unable to create an assertion.
        //   Add getters for the following fields or make them package-private:
        //     MailServiceImpl.baseUrlValue
        //     MailServiceImpl.javaMailSender
        //     MailServiceImpl.mailFromDisplay
        //     MailServiceImpl.messageSource
        //     MailServiceImpl.serverPort
        //     MailServiceImpl.serverProtocol
        //     MailServiceImpl.templateEngine

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        AnnotationConfigReactiveWebApplicationContext messageSource = new AnnotationConfigReactiveWebApplicationContext();
        MailServiceImpl mailServiceImpl = new MailServiceImpl(javaMailSender, messageSource, new SpringTemplateEngine());

        User user = new User();
        user.setActivated(true);
        user.setActivationKey("Activation Key");
        user.setAuthorities(new HashSet<>());
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImageUrl("https://example.org/example");
        user.setIpAddress("42 Main St");
        user.setLangKey("Lang Key");
        user.setLastName("Doe");
        user.setLogin("Login");
        user.setPassword("iloveyou");
        user.setResetDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setResetKey("Reset Key");
        mailServiceImpl.sendActivationEmail(user);
    }

    /**
     * Method under test: {@link MailServiceImpl#sendActivationEmail(User)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testSendActivationEmail2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at org.thymeleaf.TemplateEngine.setMessageResolver(TemplateEngine.java:896)
        //       at org.thymeleaf.spring5.SpringTemplateEngine.initializeSpecific(SpringTemplateEngine.java:301)
        //       at org.thymeleaf.TemplateEngine.initialize(TemplateEngine.java:328)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1079)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1059)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1048)
        //       at com.app.login.service.impl.MailServiceImpl.sendEmailFromTemplate(MailServiceImpl.java:103)
        //       at com.app.login.service.impl.MailServiceImpl.sendActivationEmail(MailServiceImpl.java:112)
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at com.app.login.service.impl.MailServiceImpl.sendEmail(MailServiceImpl.java:78)
        //       at com.app.login.service.impl.MailServiceImpl.sendEmailFromTemplate(MailServiceImpl.java:104)
        //       at com.app.login.service.impl.MailServiceImpl.sendActivationEmail(MailServiceImpl.java:112)
        //   See https://diff.blue/R013 to resolve this issue.

        AnnotationConfigReactiveWebApplicationContext messageSource = new AnnotationConfigReactiveWebApplicationContext();
        MailServiceImpl mailServiceImpl = new MailServiceImpl(null, messageSource, new SpringTemplateEngine());

        User user = new User();
        user.setActivated(true);
        user.setActivationKey("Activation Key");
        user.setAuthorities(new HashSet<>());
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImageUrl("https://example.org/example");
        user.setIpAddress("42 Main St");
        user.setLangKey("Lang Key");
        user.setLastName("Doe");
        user.setLogin("Login");
        user.setPassword("iloveyou");
        user.setResetDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setResetKey("Reset Key");
        mailServiceImpl.sendActivationEmail(user);
    }

    /**
     * Method under test: {@link MailServiceImpl#sendActivationEmail(User)}
     */
    @Test
    void testSendActivationEmail3() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at org.thymeleaf.TemplateEngine.setMessageResolver(TemplateEngine.java:896)
        //       at org.thymeleaf.spring5.SpringTemplateEngine.initializeSpecific(SpringTemplateEngine.java:301)
        //       at org.thymeleaf.TemplateEngine.initialize(TemplateEngine.java:328)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1079)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1059)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1048)
        //       at com.app.login.service.impl.MailServiceImpl.sendEmailFromTemplate(MailServiceImpl.java:103)
        //       at com.app.login.service.impl.MailServiceImpl.sendActivationEmail(MailServiceImpl.java:112)
        //   See https://diff.blue/R013 to resolve this issue.

        JavaMailSenderImpl javaMailSender = mock(JavaMailSenderImpl.class);
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        AnnotationConfigReactiveWebApplicationContext messageSource = new AnnotationConfigReactiveWebApplicationContext();
        MailServiceImpl mailServiceImpl = new MailServiceImpl(javaMailSender, messageSource, new SpringTemplateEngine());

        User user = new User();
        user.setActivated(true);
        user.setActivationKey("Activation Key");
        user.setAuthorities(new HashSet<>());
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImageUrl("https://example.org/example");
        user.setIpAddress("42 Main St");
        user.setLangKey("Lang Key");
        user.setLastName("Doe");
        user.setLogin("Login");
        user.setPassword("iloveyou");
        user.setResetDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setResetKey("Reset Key");
        mailServiceImpl.sendActivationEmail(user);
        verify(javaMailSender).createMimeMessage();
    }

    /**
     * Method under test: {@link MailServiceImpl#sendCreationEmail(User)}
     */
    @Test
    void testSendCreationEmail() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at org.thymeleaf.TemplateEngine.setMessageResolver(TemplateEngine.java:896)
        //       at org.thymeleaf.spring5.SpringTemplateEngine.initializeSpecific(SpringTemplateEngine.java:301)
        //       at org.thymeleaf.TemplateEngine.initialize(TemplateEngine.java:328)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1079)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1059)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1048)
        //       at com.app.login.service.impl.MailServiceImpl.sendEmailFromTemplate(MailServiceImpl.java:103)
        //       at com.app.login.service.impl.MailServiceImpl.sendCreationEmail(MailServiceImpl.java:119)
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R002 Missing observers.
        //   Diffblue Cover was unable to create an assertion.
        //   Add getters for the following fields or make them package-private:
        //     MailServiceImpl.baseUrlValue
        //     MailServiceImpl.javaMailSender
        //     MailServiceImpl.mailFromDisplay
        //     MailServiceImpl.messageSource
        //     MailServiceImpl.serverPort
        //     MailServiceImpl.serverProtocol
        //     MailServiceImpl.templateEngine

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        AnnotationConfigReactiveWebApplicationContext messageSource = new AnnotationConfigReactiveWebApplicationContext();
        MailServiceImpl mailServiceImpl = new MailServiceImpl(javaMailSender, messageSource, new SpringTemplateEngine());

        User user = new User();
        user.setActivated(true);
        user.setActivationKey("Activation Key");
        user.setAuthorities(new HashSet<>());
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImageUrl("https://example.org/example");
        user.setIpAddress("42 Main St");
        user.setLangKey("Lang Key");
        user.setLastName("Doe");
        user.setLogin("Login");
        user.setPassword("iloveyou");
        user.setResetDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setResetKey("Reset Key");
        mailServiceImpl.sendCreationEmail(user);
    }

    /**
     * Method under test: {@link MailServiceImpl#sendCreationEmail(User)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testSendCreationEmail2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at org.thymeleaf.TemplateEngine.setMessageResolver(TemplateEngine.java:896)
        //       at org.thymeleaf.spring5.SpringTemplateEngine.initializeSpecific(SpringTemplateEngine.java:301)
        //       at org.thymeleaf.TemplateEngine.initialize(TemplateEngine.java:328)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1079)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1059)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1048)
        //       at com.app.login.service.impl.MailServiceImpl.sendEmailFromTemplate(MailServiceImpl.java:103)
        //       at com.app.login.service.impl.MailServiceImpl.sendCreationEmail(MailServiceImpl.java:119)
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at com.app.login.service.impl.MailServiceImpl.sendEmail(MailServiceImpl.java:78)
        //       at com.app.login.service.impl.MailServiceImpl.sendEmailFromTemplate(MailServiceImpl.java:104)
        //       at com.app.login.service.impl.MailServiceImpl.sendCreationEmail(MailServiceImpl.java:119)
        //   See https://diff.blue/R013 to resolve this issue.

        AnnotationConfigReactiveWebApplicationContext messageSource = new AnnotationConfigReactiveWebApplicationContext();
        MailServiceImpl mailServiceImpl = new MailServiceImpl(null, messageSource, new SpringTemplateEngine());

        User user = new User();
        user.setActivated(true);
        user.setActivationKey("Activation Key");
        user.setAuthorities(new HashSet<>());
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImageUrl("https://example.org/example");
        user.setIpAddress("42 Main St");
        user.setLangKey("Lang Key");
        user.setLastName("Doe");
        user.setLogin("Login");
        user.setPassword("iloveyou");
        user.setResetDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setResetKey("Reset Key");
        mailServiceImpl.sendCreationEmail(user);
    }

    /**
     * Method under test: {@link MailServiceImpl#sendCreationEmail(User)}
     */
    @Test
    void testSendCreationEmail3() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at org.thymeleaf.TemplateEngine.setMessageResolver(TemplateEngine.java:896)
        //       at org.thymeleaf.spring5.SpringTemplateEngine.initializeSpecific(SpringTemplateEngine.java:301)
        //       at org.thymeleaf.TemplateEngine.initialize(TemplateEngine.java:328)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1079)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1059)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1048)
        //       at com.app.login.service.impl.MailServiceImpl.sendEmailFromTemplate(MailServiceImpl.java:103)
        //       at com.app.login.service.impl.MailServiceImpl.sendCreationEmail(MailServiceImpl.java:119)
        //   See https://diff.blue/R013 to resolve this issue.

        JavaMailSenderImpl javaMailSender = mock(JavaMailSenderImpl.class);
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        AnnotationConfigReactiveWebApplicationContext messageSource = new AnnotationConfigReactiveWebApplicationContext();
        MailServiceImpl mailServiceImpl = new MailServiceImpl(javaMailSender, messageSource, new SpringTemplateEngine());

        User user = new User();
        user.setActivated(true);
        user.setActivationKey("Activation Key");
        user.setAuthorities(new HashSet<>());
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImageUrl("https://example.org/example");
        user.setIpAddress("42 Main St");
        user.setLangKey("Lang Key");
        user.setLastName("Doe");
        user.setLogin("Login");
        user.setPassword("iloveyou");
        user.setResetDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setResetKey("Reset Key");
        mailServiceImpl.sendCreationEmail(user);
        verify(javaMailSender).createMimeMessage();
    }

    /**
     * Method under test: {@link MailServiceImpl#sendPasswordResetMail(User)}
     */
    @Test
    void testSendPasswordResetMail() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at org.thymeleaf.TemplateEngine.setMessageResolver(TemplateEngine.java:896)
        //       at org.thymeleaf.spring5.SpringTemplateEngine.initializeSpecific(SpringTemplateEngine.java:301)
        //       at org.thymeleaf.TemplateEngine.initialize(TemplateEngine.java:328)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1079)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1059)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1048)
        //       at com.app.login.service.impl.MailServiceImpl.sendEmailFromTemplate(MailServiceImpl.java:103)
        //       at com.app.login.service.impl.MailServiceImpl.sendPasswordResetMail(MailServiceImpl.java:126)
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R002 Missing observers.
        //   Diffblue Cover was unable to create an assertion.
        //   Add getters for the following fields or make them package-private:
        //     MailServiceImpl.baseUrlValue
        //     MailServiceImpl.javaMailSender
        //     MailServiceImpl.mailFromDisplay
        //     MailServiceImpl.messageSource
        //     MailServiceImpl.serverPort
        //     MailServiceImpl.serverProtocol
        //     MailServiceImpl.templateEngine

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        AnnotationConfigReactiveWebApplicationContext messageSource = new AnnotationConfigReactiveWebApplicationContext();
        MailServiceImpl mailServiceImpl = new MailServiceImpl(javaMailSender, messageSource, new SpringTemplateEngine());

        User user = new User();
        user.setActivated(true);
        user.setActivationKey("Activation Key");
        user.setAuthorities(new HashSet<>());
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImageUrl("https://example.org/example");
        user.setIpAddress("42 Main St");
        user.setLangKey("Lang Key");
        user.setLastName("Doe");
        user.setLogin("Login");
        user.setPassword("iloveyou");
        user.setResetDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setResetKey("Reset Key");
        mailServiceImpl.sendPasswordResetMail(user);
    }

    /**
     * Method under test: {@link MailServiceImpl#sendPasswordResetMail(User)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testSendPasswordResetMail2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at org.thymeleaf.TemplateEngine.setMessageResolver(TemplateEngine.java:896)
        //       at org.thymeleaf.spring5.SpringTemplateEngine.initializeSpecific(SpringTemplateEngine.java:301)
        //       at org.thymeleaf.TemplateEngine.initialize(TemplateEngine.java:328)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1079)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1059)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1048)
        //       at com.app.login.service.impl.MailServiceImpl.sendEmailFromTemplate(MailServiceImpl.java:103)
        //       at com.app.login.service.impl.MailServiceImpl.sendPasswordResetMail(MailServiceImpl.java:126)
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at com.app.login.service.impl.MailServiceImpl.sendEmail(MailServiceImpl.java:78)
        //       at com.app.login.service.impl.MailServiceImpl.sendEmailFromTemplate(MailServiceImpl.java:104)
        //       at com.app.login.service.impl.MailServiceImpl.sendPasswordResetMail(MailServiceImpl.java:126)
        //   See https://diff.blue/R013 to resolve this issue.

        AnnotationConfigReactiveWebApplicationContext messageSource = new AnnotationConfigReactiveWebApplicationContext();
        MailServiceImpl mailServiceImpl = new MailServiceImpl(null, messageSource, new SpringTemplateEngine());

        User user = new User();
        user.setActivated(true);
        user.setActivationKey("Activation Key");
        user.setAuthorities(new HashSet<>());
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImageUrl("https://example.org/example");
        user.setIpAddress("42 Main St");
        user.setLangKey("Lang Key");
        user.setLastName("Doe");
        user.setLogin("Login");
        user.setPassword("iloveyou");
        user.setResetDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setResetKey("Reset Key");
        mailServiceImpl.sendPasswordResetMail(user);
    }

    /**
     * Method under test: {@link MailServiceImpl#sendPasswordResetMail(User)}
     */
    @Test
    void testSendPasswordResetMail3() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at org.thymeleaf.TemplateEngine.setMessageResolver(TemplateEngine.java:896)
        //       at org.thymeleaf.spring5.SpringTemplateEngine.initializeSpecific(SpringTemplateEngine.java:301)
        //       at org.thymeleaf.TemplateEngine.initialize(TemplateEngine.java:328)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1079)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1059)
        //       at org.thymeleaf.TemplateEngine.process(TemplateEngine.java:1048)
        //       at com.app.login.service.impl.MailServiceImpl.sendEmailFromTemplate(MailServiceImpl.java:103)
        //       at com.app.login.service.impl.MailServiceImpl.sendPasswordResetMail(MailServiceImpl.java:126)
        //   See https://diff.blue/R013 to resolve this issue.

        JavaMailSenderImpl javaMailSender = mock(JavaMailSenderImpl.class);
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        AnnotationConfigReactiveWebApplicationContext messageSource = new AnnotationConfigReactiveWebApplicationContext();
        MailServiceImpl mailServiceImpl = new MailServiceImpl(javaMailSender, messageSource, new SpringTemplateEngine());

        User user = new User();
        user.setActivated(true);
        user.setActivationKey("Activation Key");
        user.setAuthorities(new HashSet<>());
        user.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImageUrl("https://example.org/example");
        user.setIpAddress("42 Main St");
        user.setLangKey("Lang Key");
        user.setLastName("Doe");
        user.setLogin("Login");
        user.setPassword("iloveyou");
        user.setResetDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        user.setResetKey("Reset Key");
        mailServiceImpl.sendPasswordResetMail(user);
        verify(javaMailSender).createMimeMessage();
    }
}

