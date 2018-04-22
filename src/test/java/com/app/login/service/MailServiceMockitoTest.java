package com.app.login.service;

import com.app.login.service.Impl.MailServiceImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Administrator on 2018/3/26 0026.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceMockitoTest {

    @MockBean
    private MailServiceImpl mailService;


    @Ignore
    @Test
    public void sendEmail() throws Exception {
        mailService.sendEmail("d","xd","asf",false,false);
    }

}