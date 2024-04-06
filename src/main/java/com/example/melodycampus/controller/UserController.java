package com.example.melodycampus.controller;

import com.example.melodycampus.common.SessionUtil;
import com.example.melodycampus.mapper.UserMapper;
import com.example.melodycampus.model.User;
import com.example.melodycampus.common.Constant;
import com.example.melodycampus.common.MD5Util;
import com.example.melodycampus.common.ResponseBodyMessage;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    /**
     * 获取全部用户账号密码信息
     * @return
     */
    @RequestMapping("/all")
    public ResponseBodyMessage<List<User>> all(HttpServletRequest request){
        // 判断登录用户是否为admin
        int loginUserType = SessionUtil.getLoginUser(request).getUserType();
        if(loginUserType != 1){
            return new ResponseBodyMessage<>(-1,"无权访问",null);
        }

        // 获取全部注册用户
        List<User> users = userMapper.selectAll();
        return new ResponseBodyMessage<>(0,"获取用户信息成功",users);
    }

    /**
     * 删除用户
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/delete")
    public ResponseBodyMessage<Boolean> delete(@RequestParam int id, HttpServletRequest request){
        // 判断登录用户是否有权限删除
        int loginUserType = SessionUtil.getLoginUser(request).getUserType();
        if(loginUserType == 0){
            return new ResponseBodyMessage<>(-1,"无权限删除",false);
        }

        int res = userMapper.delete(id);
        if(res == 1){
            return new ResponseBodyMessage<>(0,"删除成功",true);
        }else {
            return new ResponseBodyMessage<>(-1,"删除失败",false);
        }
    }

    /**
     * 更新用户密码
     * @param userId
     * @param newPassword
     * @param request
     * @return
     */
    @RequestMapping("/updatePassword")
    public ResponseBodyMessage<Boolean> updatePassword(@RequestParam int userId, @RequestParam String newPassword, HttpServletRequest request){
        // 判断用户权限
        int loginUserType = SessionUtil.getLoginUser(request).getUserType();
        if(loginUserType == 0){
            return new ResponseBodyMessage<>(-1,"无权限修改",false);
        }

        // 加密新密码
        String newEncryptPassword = MD5Util.encrypt(newPassword);
        int res = userMapper.updatePassword(userId,newEncryptPassword);
        if(res == 1){
            return new ResponseBodyMessage<>(0,"修改成功",true);
        }else {
            return new ResponseBodyMessage<>(-1,"修改失败",false);
        }
    }

    /**
     * 获取登录用户信息
     */
    @RequestMapping("/getLoginUser")
    public ResponseBodyMessage<User> getLoginUser(HttpServletRequest request){
        User user = SessionUtil.getLoginUser(request);
        return new ResponseBodyMessage<>(0,"获取登录用户信息成功",user);
    }
}
