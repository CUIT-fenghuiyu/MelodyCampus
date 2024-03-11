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
public class MusicEvent {
    /**
     * CREATE TABLE MusicEvent (
     *     id INT PRIMARY KEY AUTO_INCREMENT, -- 活动ID，主键，自增
     *     EventName VARCHAR(255) NOT NULL, -- 活动名称，不为空
     *     Description TEXT NOT NULL, -- 活动描述，不为空
     *     imageUrl VARCHAR(255) NOT NULL, -- 活动图片链接
     *     status INT NOT NULL, -- 活动状态，0为已删除，1为正常
     *     createdAt DATETIME NOT NULL, -- 创建时间
     *     updatedAt DATETIME NOT NULL -- 更新时间
     * );
     */

    private int id;
    private String EventName;
    private String Description;
    private String imageUrl;
    private int status;
    private java.sql.Timestamp createdAt;
    private java.sql.Timestamp updatedAt;
}
