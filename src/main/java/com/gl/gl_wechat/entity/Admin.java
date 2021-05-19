package com.gl.gl_wechat.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: zhaoCong
 * @date: 2020-04-08 11:03
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Admin implements Serializable {
    private String phone;
    private String name;
    private String company;
    private String number;
    private String create_by;
    private Date create_time;
    private String update_by;
    private Date update_time;
}
