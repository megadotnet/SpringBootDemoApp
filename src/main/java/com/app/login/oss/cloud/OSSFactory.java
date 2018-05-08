package com.app.login.oss.cloud;

import com.app.login.config.CloudStorageConfig;
import com.app.login.config.CloudStorageService;
import com.app.login.exception.RRException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.thymeleaf.spring5.context.SpringContextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * 文件上传Factory
 * @author  liu.junyuan
 * @email liu.junyuan@vpclub.cn
 * @date 2018-04-26 10:18
 */
public class OSSFactory {



    public CloudStorageService build() throws FileNotFoundException {
        //获取云存储配置信息
        CloudStorageConfig config = getConfigObject(ConfigConstant.CLOUD_STORAGE_CONFIG_KEY, CloudStorageConfig.class);

        if(config.getType() == Constant.CloudService.QINIU.getValue()){
            return new QiniuCloudStorageService(config);
        }

        return null;
    }

    public  <T> T getConfigObject(String key, Class<T> clazz) throws FileNotFoundException {
        String value = getConfigFromJsonfile();
        if(StringUtils.isNotBlank(value)){
            return new Gson().fromJson(value, clazz);
        }

        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RRException("获取参数失败");
        }
    }

    public  String getConfigFromJsonfile() throws FileNotFoundException {
        //ClassPathResource res = new ClassPathResource("oss-config.json");
        //File file = new File(res.getPath());
       // return new Scanner(file).useDelimiter("\\Z").next();

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("oss-config.json").getFile());

        return new Scanner(file).useDelimiter("\\Z").next();


    }

}
