package com.app.login.config;

import com.app.login.service.IMailService;
import com.app.login.service.events.EmailNoticeEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * NoticeListener
 */
@Component
@Slf4j
public class EmailNoticeListener implements ApplicationListener<EmailNoticeEvent> {

    private static final Logger logger = LoggerFactory.getLogger(EmailNoticeListener.class);

    @Autowired
    private IMailService mailService;

    @Async
    @Override
    public void onApplicationEvent(EmailNoticeEvent noticeEvent) {
       // try {
            //for mocking
            //Thread.sleep(5000);
            //logger.info("listener get event, sleep 5 second...");
            mailService.sendActivationEmail(noticeEvent.getUser());
        //} catch (InterruptedException e) {
        //   log.error(e.getMessage());
        //}
        logger.info("event message is : {}", noticeEvent.getUser());
    }
}
