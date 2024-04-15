package com.example.melodycampus.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @projectName: MelodyCampus
 * @package: com.example.melodycampus.common
 * @author: Fenghuiyu
 * @date: 2024/2/23 22:06
 * @description:
 */
@Configuration
public class APPConfig implements WebMvcConfigurer {
    // 不拦截的 url 集合
    List<String> excludes = new ArrayList<String>(){{
        add("/js/**");  // 放行 static/js 下的所有文件
        add("/css/**"); // 放行 static/css 下的所有文件
        add("/img/**"); // 放行 static/img 下的所有文件
        add("/user/login"); // 放行登录接口
        add("/user/reg"); // 放行注册接口
        add("/reg.html"); //放行注册页面
        add("/login.html"); //放行登录页面
    }};
    @Autowired
    private LoginInterceptor loginInterceptor;

    //添加登录拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**") //拦截所有接口
                .excludePathPatterns(excludes); //排除的接口和页面

    }
}
