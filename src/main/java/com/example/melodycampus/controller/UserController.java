package com.example.melodycampus.controller;

import com.example.melodycampus.mapper.UserMapper;
import com.example.melodycampus.model.User;
import com.example.melodycampus.common.Constant;
import com.example.melodycampus.common.MD5Util;
import com.example.melodycampus.common.ResponseBodyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @projectName: MelodyCampus
 * @package: com.example.melodycampus.controller
 * @author: Fenghuiyu
 * @date: 2024/2/22 23:34
 * @description:
 */
@RestController
@RequestMapping("/user")
public class UserController
{
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/reg")
    public ResponseBodyMessage<User> register(@RequestParam String username, @RequestParam String password, HttpServletRequest request){
        User user = userMapper.selectByName(username);

        // 判断用户名是否存在
        if (user != null) {
            return new ResponseBodyMessage<>(-2,"用户名已存在，请重新输入！", null);
        }

        // 添加用户，并返回用户信息
        int result = userMapper.add(username, MD5Util.encrypt(password));
        if(result==1){
            System.out.println(userMapper.selectByName(username));
            return new ResponseBodyMessage<>(0,"注册成功", userMapper.selectByName(username));
        }else {
            return new ResponseBodyMessage<>(-1,"注册失败", null);
        }
    }

    @RequestMapping("/login")
    public ResponseBodyMessage<User> login(@RequestParam String username, @RequestParam String password,
                                           HttpServletRequest request) {

        User user = userMapper.selectByName(username);

        // 判断用户名是否存在
        if(user == null || !MD5Util.decrypt(password,user.getPassword())) {
            System.out.println("登录失败！");
            return new ResponseBodyMessage<>(-1,"用户名或者密码错误！请重新输入",null);
        }else {
            // 将用户信息存入session中
            request.getSession().setAttribute(Constant.USERINFO_SESSION_KEY,user);
            System.out.println(user);
            return new ResponseBodyMessage<>(0,"登录成功！",user);
        }
    }

}
