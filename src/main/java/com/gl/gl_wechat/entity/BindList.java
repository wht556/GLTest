package com.gl.gl_wechat.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: zhaoCong
 * @date: 2020-04-08 9:28
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BindList  implements Serializable {
    private String productName;
    private String model;
    private String NO;
    private String bindDate;
    private String bindLatLongitude;
    private String ID;
    private String deviceNO;
    private String cardNo;
    private String manufacturer;
    private String phone;
    private String bindAddress;
    private String detailAddress;
    private String NOPicture;
    private String cardNoPicture;
}
