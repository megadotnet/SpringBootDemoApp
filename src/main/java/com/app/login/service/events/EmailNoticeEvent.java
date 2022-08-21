package com.app.login.service.events;


import com.app.login.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;

/**
 * EmailNoticeEvent
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 * </p>
 * @author Megdotnet
 */
public class EmailNoticeEvent extends ApplicationEvent {

    private static final Logger logger = LoggerFactory.getLogger(EmailNoticeEvent.class);

    /**
     * 接受信息
     */
    private User mailboxUser;

    public EmailNoticeEvent(User mailboxuser) {
        super(mailboxuser);
        this.mailboxUser = mailboxuser;
        logger.info("add event success! message: {}", mailboxuser);
    }

    public User getUser() {
        return mailboxUser;
    }
}

