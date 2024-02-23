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
public class comment {
    private int id;
    private String content;
    private int userId;
    private int postId;
    private String commentdate;
}
