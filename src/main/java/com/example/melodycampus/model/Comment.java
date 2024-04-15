package com.example.melodycampus.model;

import lombok.Data;

/**
 * @projectName: MelodyCampus
 * @package: com.example.melodycampus.model
 * @author: Fenghuiyu
 * @date: 2024/2/22 22:51
 * @description：    评论表
 */
@Data
public class Comment {
    private int id;
    private int userId;
    private int musicId;
    private String content;
    private String commentdate;
    private String username;
}
