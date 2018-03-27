package com.app.login.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/3/26 0026.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceMockitoTest {

    @MockBean
    private MailService mailService;


    @Ignore
    @Test
    public void sendEmail() throws Exception {
        mailService.sendEmail("d","xd","asf",false,false);
    }

}