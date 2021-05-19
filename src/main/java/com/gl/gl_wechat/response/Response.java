package com.gl.gl_wechat.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gl.gl_wechat.common.MessageUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 返回前台的json数据
 * </p>
 * ClassName: ActiveUser <br/>
 * Version: 1.0 <br/>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> implements Serializable {
    private String date;
    private String code;
    private T msg;

    public Response(T msg) {
        this.msg = msg;
    }

    public Response() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getMsg() {
        return msg;
    }

    public void setMsg(T msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Response{" +
                "date='" + date + '\'' +
                ", code='" + code + '\'' +
                ", msg=" + msg +
                '}';
    }
}
