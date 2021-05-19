package com.gl.gl_wechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gl.gl_wechat.common.AESUtil;
import com.gl.gl_wechat.common.HttpRequest;
import com.gl.gl_wechat.entity.Msg;
import com.gl.gl_wechat.response.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhaoCong
 * @date: 2020-04-06 20:47
 */
@JsonFormat(timezone = "GMT+8", pattern = "yyyyMMddHHmmss")
public class test {
    public static void main(String[] args) throws Exception {
        String encryptedData = "vh4hp0bGTRt7/d01in7isnybZrnmf1+8cv0vFnLJfDGsGFVwBoTMubQ9u5y6a6eJZnILwbCNgp1hImIzsWHFEPS60GjHbNP+3V4kwJIkvQOkGUnOXZ8kkLNcGRJ9TPfq24jwHXasKRirm0/2oEm9z6ZaVzxnwbmCNciSnSCmcpIiuNi/MMrjCFuKA1cqp2ppMQS3QMwQtWv/KEDp7I7AoQ==";
        String session_key="liHj7jQmTCmih1OdnX4PRw==";
        String iv="XxsbVYH6BEKS0nvOjhbg9w==";

        JSONObject object = wxController.getPhoneNumber(encryptedData,session_key,iv);
        System.out.println(object);
        Response response = new Response();
        Msg msg = new Msg();
        String purePhoneNumber = object.getString("purePhoneNumber");
        System.out.println(purePhoneNumber);
    }

    public String createToken(String phone) throws Exception {
        Date date = new Date();
        String decUserId = AESUtil.encrypt(date+phone);
        return decUserId;
    }

    public static String httpPost(String urlStr, Map<String,String> params){
        URL connect;
        StringBuffer data = new StringBuffer();
        try {
            connect = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection)connect.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);//post不能使用缓存
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            OutputStreamWriter paramout = new OutputStreamWriter(
                    connection.getOutputStream(),"UTF-8");
            String paramsStr = "";   //拼接Post 请求的参数
            for(String param : params.keySet()){
                paramsStr += "&" + param + "=" + params.get(param);
            }
            if(!paramsStr.isEmpty()){
                paramsStr = paramsStr.substring(1);
            }
            paramout.write(paramsStr);
            paramout.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }

            paramout.close();
            reader.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data.toString();
    }
}
