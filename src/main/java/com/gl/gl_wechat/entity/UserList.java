package com.gl.gl_wechat.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: zhaoCong
 * @date: 2020-04-08 9:25
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserList implements Serializable {

    private String phone;

    private Date create_time;

    private String name;

    private String company;

    private String number;
}
