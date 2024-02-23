package com.example.melodycampus.tools;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @projectName: MelodyCampus
 * @package: com.example.melodycampus.tools
 * @author: Fenghuiyu
 * @date: 2024/2/23 15:59
 * @description:    对密码进行MD5加盐加密
 */
public class MD5Util {
    /**
     * 对密码进行加盐加密
     * @param password
     * @return
     */
    public static String encrypt(String password){
        //每次生成随机的32位盐值
        String salt = UUID.randomUUID().toString().replace("-","");
        // 最终密码=md5(盐值+初始密码)
        String finalPassword = DigestUtils.md5DigestAsHex((salt+password).getBytes());

        // 最后返回盐值+加密后的密码
        return salt+finalPassword;
    }

    /**
     * 验证密码是否和加密后的密码一致
     * @param password
     * @param finalPassword
     * @return
     */
    public static Boolean decrypt(String password, String finalPassword){
        // 非空效验
        if (!StringUtils.hasLength(password) || !StringUtils.hasLength(finalPassword)) {
            return false;
        }
        if (finalPassword.length() != 64) { // 最终密码不正确
            return false;
        }
        // 得到加密后密码的盐值
        String salt = finalPassword.substring(0,32);

        // 对待验证密码进行加盐加密处理
        String securityPassword = DigestUtils.md5DigestAsHex((salt+password).getBytes());

        // 返回待验证密码加盐加密后与数据库中密码是否匹配相等
        return (salt+securityPassword).equals(finalPassword);
    }
}
