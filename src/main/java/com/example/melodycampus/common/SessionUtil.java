package com.example.melodycampus.common;

import com.example.melodycampus.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @projectName: MelodyCampus
 * @package: com.example.melodycampus.common
 * @author: Fenghuiyu
 * @date: 2024/2/23 22:13
 * @description:*/


public class SessionUtil {
    public static User getLoginUser(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null && session.getAttribute(Constant.USERINFO_SESSION_KEY) != null){
            return (User) session.getAttribute(Constant.USERINFO_SESSION_KEY);
        }

        return null;
    }
}
