package com.app.login.oss.cloud;

import com.app.login.config.CloudStorageService;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dev on 2018/5/8.
 */
public class OSSFactoryTest {
    @Test
    public void getConfigObject() throws Exception {

        CloudStorageService cloudStorageService=new OSSFactory().build();
        assertNotNull(cloudStorageService);
        QiniuCloudStorageService qiniuCloudStorageService=(QiniuCloudStorageService)cloudStorageService;
        assertNotNull(qiniuCloudStorageService);
    }

}