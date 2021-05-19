package com.gl.gl_wechat.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author: zhaoCong
 * @date: 2020-04-03 14:34
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuperAdmin  implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 录入时间
     */
    private java.util.Date createTime;

    @Override
    public String toString() {
        return "SuperAdmin{" +
                "phone='" + phone + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
