package com.app.login.config;

/**
 * Constants
 * @author Megadotnet
 * @date 2018-03-07
 */
public final class Constants {

    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";
    public static final String BEARER = "Bearer ";
    public static final Long TOKEN_VALIDITY_IN_MILLISECONDS = 1800000L;
    public static final Long TOKEN_VALIDITY_IN_MILLISECONDS_FOR_REMEMBER_ME = 604800000L;
    public static final String DEFAULT_LANGUAGE_KEY = "en";
    public static final Integer DOS_MAX_REGISTRATION_ALLOWED = 300;
    private static final String NEWLINE = "\n----------------------------------------------------------\n\t";
    private static final String APP_MSG = "Application '{}' is running! Access URLs:\n\t";
    private static final String EXT = "External: \t{}://{}:{}\n\t";
    public static  final String ANONYMOUS_USER="admin";
    public static final String STARTUP_LOG_MSG = NEWLINE + APP_MSG + EXT;
    /**
     * 云存储配置KEY
     */
    public final static String CLOUD_STORAGE_CONFIG_KEY = "CLOUD_STORAGE_CONFIG_KEY";

    /**
     * 云服务商
     */
    public enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private Constants() {
    }
}
