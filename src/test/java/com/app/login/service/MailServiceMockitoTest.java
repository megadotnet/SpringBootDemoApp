package com.app.login.service;

import com.app.TestBase;
import com.app.login.service.Impl.MailServiceImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

@RunWith(SpringRunner.class)
public class MailServiceMockitoTest extends TestBase {

    @MockBean
    private MailServiceImpl mailService;

    @Test
    public void baseUrlInit() throws Exception {
        mailService.baseUrlInit();
    }


    @Test
    public void sendEmail() throws Exception {
        mailService.sendEmail("d","xd","asf",false,false);
    }

    @Test
    public void sendEmailFromTemplate()
    {
        mailService.sendEmailFromTemplate(createUser(), "activationEmail", "email.activation.title");
    }



}