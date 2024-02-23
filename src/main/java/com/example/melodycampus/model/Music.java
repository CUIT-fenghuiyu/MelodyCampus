package com.example.melodycampus.model;

import lombok.Data;

/**
 * @projectName: MelodyCampus
 * @package: com.example.melodycampus.model
 * @author: Fenghuiyu
 * @date: 2024/2/22 22:47
 * @description:    音乐表
 */

@Data
public class Music {
    private int id;
    private String title;
    private String singer;
    private String time;
    private String url;
    private int userid;
}