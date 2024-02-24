package com.example.melodycampus.common;

import lombok.Data;

/**
 * @projectName: MelodyCampus
 * @package: com.example.melodycampus.tools
 * @author: Fenghuiyu
 * @date: 2024/2/23 15:44
 * @description:    统一封装返回数据
 */
@Data
public class ResponseBodyMessage <T>{
    private int status;//状态码

    private String message;//返回的信息【出错的原因？  没错的原因？】

    private T data;//返回给前端的数据

    public ResponseBodyMessage(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
