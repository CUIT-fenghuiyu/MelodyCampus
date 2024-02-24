package com.example.melodycampus.common;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @projectName: MelodyCampus
 * @package: com.example.melodycampus.common
 * @author: Fenghuiyu
 * @date: 2024/2/23 22:03
 * @description:    检查登录状态的拦截器*/


@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if (session!=null && session.getAttribute(Constant.USERINFO_SESSION_KEY)!=null){
            //已经登录就放行
            return true;
        }else {
            //未登录就返回状态码401没有权限访问
            response.setStatus(401);
            return false;
        }
    }
}
