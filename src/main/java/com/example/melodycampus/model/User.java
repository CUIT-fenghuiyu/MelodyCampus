package com.example.melodycampus.model;

import lombok.Data;

/**
 * @projectName: MelodyCampus
 * @package: com.example.melodycampus.model
 * @author: Fenghuiyu
 * @date: 2024/2/22 22:20
 * @description:    用户信息字段
 */
@Data
public class User
{
    private int id;
    private String username;
    private String password;
    private int userType;
}

