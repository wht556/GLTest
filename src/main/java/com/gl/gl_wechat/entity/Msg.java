package com.gl.gl_wechat.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: zhaoCong
 * @date: 2020-04-07 17:23
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Msg implements Serializable {

    private String responseMsg;

    private String token;

    private String purePhoneNumber;

    private String countryCode;

    private List<Admin> admin;

    private List<UserList>  userList;

    private List<BindList>  bindList;
}
