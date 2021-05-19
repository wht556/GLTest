package com.gl.gl_wechat.response;

/**
 * <p>
 * 状态码
 * </p>
 * Version: 1.0 <br/>
 */
public interface StatusCode {

    /**
     * 全局系统异常错误代码
     */
    String FAILURE = "99999";

    /**
     * 全局系统成功返回码
     */
    String SUCCESS = "00000";

    /**
     * 全局系统无权限返回码
     */
    String UNAUTHORIZED = "11111";

    /**
     * 用户长时间未操作，token失效
     */
    String TOKEN_EMPTY = "22222";

    /**
     * 用户在别处登录，token失效
     */
    String TOKEN_REPLACE = "33333";

}

