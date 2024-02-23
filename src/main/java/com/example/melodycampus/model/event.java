package com.example.melodycampus.model;

import lombok.Data;

/**
 * @projectName: MelodyCampus
 * @package: com.example.melodycampus.model
 * @author: Fenghuiyu
 * @date: 2024/2/22 22:56
 * @description:
 */
@Data
public class event {
    /**
     * CREATE TABLE MusicEvent (
     *     id INT PRIMARY KEY AUTO_INCREMENT, -- 活动ID，主键，自增
     *     EventName VARCHAR(255) NOT NULL, -- 活动名称，不为空
     *     EventDate DATE NOT NULL, -- 活动日期，不为空
     *     Location VARCHAR(255) NOT NULL, -- 活动地点，不为空
     *     Description TEXT NOT NULL, -- 活动描述，不为空
     *     TargetWebsiteLink VARCHAR(255) NOT NULL -- 目标网站链接，不为空
     * );
     */

    private int id;
    private String EventName;
    private String EventDate;
    private String Location;
    private String Description;
    private String TargetWebsiteLink;
}
