package com.template.security.util;

import cn.hutool.crypto.digest.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author qiuqfang
 * @date 2021/6/13
 */
@Component
public class EncryptionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionUtil.class);
    /**
     * 加盐位置，即将盐值字符串放置在数据的index数
     */
    private int saltPosition = 1024;
    /**
     * 摘要次数，当此值小于等于1,默认为1。
     */
    private int digestCount = 1024;

    /**
     * MD5加密
     *
     * @param password 明文密码
     * @return 加密后的密文
     */
    public String encodePassword(Date date, String password) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String salt = simpleDateFormat.format(date);
        LOGGER.info("当前密码盐：{}", salt);
        return new MD5(salt.getBytes(), saltPosition, digestCount).digestHex(password);
    }

    /**
     * 校验密码
     *
     * @param source 数据库密文
     * @param target 明文
     * @return 相同返回true, 不同返回false
     */
    public boolean verifyPassword(Date date, String source, String target) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String salt = simpleDateFormat.format(date);
        LOGGER.info("当前密码盐：{}", salt);
        String password = new MD5(salt.getBytes(), saltPosition, digestCount).digestHex(target);
        return source.equals(password);
    }

    public static void main(String[] args) {
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        String encodePassword = encryptionUtil.encodePassword(new Date(), "123456");
        System.out.println(encodePassword);
    }

}
