package com.gl.gl_wechat.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gl.gl_wechat.common.AESUtil;
import com.gl.gl_wechat.common.HttpClientUtil;
import com.gl.gl_wechat.common.HttpRequest;
import com.gl.gl_wechat.common.ZXing.QRCodeUtil;
import com.gl.gl_wechat.entity.Admin;
import com.gl.gl_wechat.entity.BindList;
import com.gl.gl_wechat.entity.Msg;
import com.gl.gl_wechat.entity.UserList;
import com.gl.gl_wechat.response.Response;
import com.gl.gl_wechat.service.WXService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: zhaoCong
 * @date: 2020-04-03 14:46
 */
@RestController
@RequestMapping("/wx")
@JsonFormat(timezone = "GMT+8", pattern = "yyyyMMddHHmmss")
public class wxController {
    @Autowired
    private WXService wxService;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 生成token
     * @param phone
     * @return
     * @throws Exception
     */
    private String createToken(String phone) throws Exception {
        String AESToken = AESUtil.encrypt(System.currentTimeMillis()+phone);
        return AESToken;
    }

    /**
     *
     * @param token
     * @return
     * @throws Exception
     */
    private Boolean checkToken(String token,String phone) throws Exception {
        Boolean b = false;
        String jmtoken = AESUtil.decrypt(token);
        String decUserId = jmtoken.substring(jmtoken.length()-11,jmtoken.length());
        b = decUserId.endsWith(phone);
        return b;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(){
        return "hello";
    }

    /**
     * 2.1 微信小程序登录
     * @param code
     * @return
     */
    @RequestMapping(value = "/wxLogin", method = RequestMethod.POST)
    public Response wxLogin(@RequestParam String code,
                            @RequestParam String encryptedData,
                            @RequestParam String iv){
        Response response = new Response();
        Msg msg = new Msg();
        System.out.println("code:" + code);

        try{
            String url = "https://api.weixin.qq.com/sns/jscode2session";
            Map<String, String> param = new HashMap<>();
            param.put("appid", "wx3c909026cc44e03f");
            param.put("secret", "8bc7695678ca5199043f60ce410cc599");
            param.put("js_code", code);
            param.put("grant_type", "authorization_code");

            //发起get请求
            String wxResult = HttpClientUtil.doGet(url, param);
            JSONObject jsonObject = JSONObject.parseObject(wxResult);

            System.out.println("jsonObject:");
            System.out.println(jsonObject);
            if(jsonObject.containsKey("session_key")){
                // 获取参数返回的
                String session_key = jsonObject.get("session_key").toString();
                String open_id = jsonObject.get("openid").toString();
                JSONObject object = getPhoneNumber(encryptedData,session_key,iv);
                String purePhoneNumber = object.getString("purePhoneNumber");
                String countryCode = object.getString("countryCode");
                msg.setPurePhoneNumber(purePhoneNumber);
                msg.setCountryCode(countryCode);
                response.setCode("000");
                response.setDate("success");
                msg.setResponseMsg("微信登录成功");
                response.setMsg(msg);
            }else if(jsonObject.containsKey("errcode")){
                String errcode = jsonObject.get("errcode").toString();
                String errmsg = jsonObject.get("errmsg").toString();
                response.setCode(errcode);
                response.setDate("false");
                msg.setResponseMsg("微信登录失败");
                response.setMsg(msg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return response;
    }

    /**
     * 返回用户的手机信息
     * @param encryptedData
     * @param session_key
     * @param iv
     * @return
     */
    public static JSONObject getPhoneNumber(String encryptedData, String session_key, String iv) {
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(session_key);
        System.out.println("keyByte:   "+keyByte);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 2.2 超级管理员和管理员鉴权
     * @param phone
     * @return
     */
    @RequestMapping(value = "/authentication", method = RequestMethod.POST)
    public Response authentication(@RequestParam String phone) throws Exception {
        Response response = new Response();
        Msg msg = new Msg();
        String i = "0";
        String token = createToken(phone);
        response.setDate("false");
        response.setCode("999");
        msg.setResponseMsg("您不具备登录权限");
        response.setMsg(msg);

        try{
            i = wxService.selectSuperAdmin(phone);

            if(i!=null&&i.equals("1")){
                response.setDate("success");
                response.setCode("000");
                msg.setResponseMsg("超级管理员登录成功");
                msg.setToken(token);
                response.setMsg(msg);
            }else{
                List<UserList> userList = wxService.selectUserList(phone);
                if(userList!=null&&userList.size()>0){
                    response.setDate("success");
                    response.setCode("002");
                    msg.setResponseMsg("管理员登录成功");
                    msg.setToken(token);
                    response.setMsg(msg);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 2.3.	添加管理员
     * @param phone
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public Response addUser(@RequestParam String token,
                            @RequestParam String adminPhone,
                            @RequestParam String phone,
                            @RequestParam String name,
                            @RequestParam(required = false) String company,
                            @RequestParam(required = false) String number,
                            @RequestParam String addDate) throws Exception {
        Response response = new Response();
        Msg msg = new Msg();
        response.setDate("false");
        response.setCode("999");
        msg.setResponseMsg("失败");
        response.setMsg(msg);

        String i = "0";
        Boolean checkToken = checkToken(token, adminPhone);
        if(checkToken){
            i = wxService.selectSuperAdmin(adminPhone);
            if (i != null && i.equals("1")) {
                //查询该管理员是否存在
                String exists = wxService.selAdmin(phone);
                if(exists != null && exists.equals("1")){
                    response.setDate("false");
                    response.setCode("996");
                    msg.setResponseMsg("该管理员已存在");
                    response.setMsg(msg);
                }else{
                    Admin admin = new Admin();
                    admin.setPhone(phone);
                    admin.setName(name);
                    admin.setCompany(company);
                    admin.setNumber(number);
                    admin.setCreate_by(adminPhone);
                    admin.setCreate_time(sdf.parse(addDate));
                    int insert = wxService.addUser(admin);
                    if(insert == 1){
                        response.setDate("success");
                        response.setCode("000");
                        msg.setResponseMsg("添加管理员成功");
                        response.setMsg(msg);
                    }else{
                        response.setDate("false");
                        response.setCode("997");
                        msg.setResponseMsg("添加管理员失败");
                        response.setMsg(msg);
                    }
                }
            }else{
                response.setDate("false");
                response.setCode("998");
                msg.setResponseMsg("您不具备添加管理员的权限");
                response.setMsg(msg);
            }
        }else{
            response.setDate("false");
            response.setCode("001");
            msg.setResponseMsg("Token异常");
            response.setMsg(msg);
        }

        return response;
    }

    /**
     * 2.4.	删除管理员
     * @param token
     * @param adminPhone
     * @param phone
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delUser", method = RequestMethod.POST)
    public Response addUser(@RequestParam String token,
                            @RequestParam String adminPhone,
                            @RequestParam String phone)throws Exception {
        Response response = new Response();
        Msg msg = new Msg();
        response.setDate("false");
        response.setCode("999");
        msg.setResponseMsg("失败");
        response.setMsg(msg);

        String i = "0";
        Boolean checkToken = checkToken(token, adminPhone);
        if(checkToken){
            i = wxService.selectSuperAdmin(adminPhone);
            if (i != null && i.equals("1")) {
                int delete = wxService.delUser(phone);
                if(delete == 1){
                    response.setDate("success");
                    response.setCode("000");
                    msg.setResponseMsg("删除管理员成功");
                    response.setMsg(msg);
                }else{
                    response.setDate("false");
                    response.setCode("997");
                    msg.setResponseMsg("删除管理员失败");
                    response.setMsg(msg);
                }
            }else{
                response.setDate("false");
                response.setCode("998");
                msg.setResponseMsg("您不具备删除管理员的权限");
                response.setMsg(msg);
            }
        }else{
            response.setDate("false");
            response.setCode("001");
            msg.setResponseMsg("Token异常");
            response.setMsg(msg);
        }

        return response;
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public Response updateUser(@RequestParam String token,
                            @RequestParam String adminPhone,
                            @RequestParam String phone,
                            @RequestParam String name,
                            @RequestParam String company,
                            @RequestParam String number,
                            @RequestParam String update_time)throws Exception {
        Response response = new Response();
        Msg msg = new Msg();
        response.setDate("false");
        response.setCode("999");
        msg.setResponseMsg("失败");
        response.setMsg(msg);

        String i;
        Boolean checkToken = checkToken(token, adminPhone);
        if(checkToken){
            i = wxService.selectSuperAdmin(adminPhone);
            if (i != null && i.equals("1")) {
                int update = wxService.updateUser(adminPhone,phone,name,company,number,update_time);
                if(update == 1){
                    response.setDate("success");
                    response.setCode("000");
                    msg.setResponseMsg("修改管理员成功");
                    response.setMsg(msg);
                }else{
                    response.setDate("false");
                    response.setCode("997");
                    msg.setResponseMsg("修改管理员失败");
                    response.setMsg(msg);
                }
            }else{
                response.setDate("false");
                response.setCode("998");
                msg.setResponseMsg("您不具备修改管理员的权限");
                response.setMsg(msg);
            }
        }else{
            response.setDate("false");
            response.setCode("001");
            msg.setResponseMsg("Token异常");
            response.setMsg(msg);
        }

        return response;

    }

    /**
     * 2.6.	查找管理员
     * @param token
     * @param adminPhone
     * @param phone
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/selUser", method = RequestMethod.POST)
    public Response selUser(@RequestParam String token,
                            @RequestParam String adminPhone,
                            @RequestParam(required = false) String phone)throws Exception {
        Response response = new Response();
        Msg msg = new Msg();
        response.setDate("false");
        response.setCode("999");
        msg.setResponseMsg("失败");
        response.setMsg(msg);

        String i = "0";
        Boolean checkToken = checkToken(token, adminPhone);
        if(checkToken){
            i = wxService.selectSuperAdmin(adminPhone);
            if (i != null && i.equals("1")) {
                List<Admin> select = new ArrayList<>();
                if(phone!=null&&phone.length()>0){
                    select = wxService.selUser(phone);
                }else{
                    select = wxService.selUser();
                }
                if(select.size()>0){
                    response.setDate("success");
                    response.setCode("000");
                    msg.setResponseMsg("查找管理员成功");
                    msg.setAdmin(select);
                    response.setMsg(msg);
                }else{
                    response.setDate("false");
                    response.setCode("997");
                    msg.setResponseMsg("未查找到管理员");
                    response.setMsg(msg);
                }
            }else{
                response.setDate("false");
                response.setCode("998");
                msg.setResponseMsg("您不具备查找管理员的权限");
                response.setMsg(msg);
            }
        }else{
            response.setDate("false");
            response.setCode("001");
            msg.setResponseMsg("Token异常");
            response.setMsg(msg);
        }

        return response;
    }

    /**
     * 2.7.	绑定数据
     * 2.8.	解绑数据
     * 共用一个接口
     * @param token
     * @param phone
     * ...
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    public Response bind(@RequestParam String token,
                         @RequestParam String phone,
                         @RequestParam(required = false) String cardNo,
                         @RequestParam(required = false) String productName,
                         @RequestParam(required = false) String model,
                         @RequestParam(required = false) String NO,
                         @RequestParam(required = false) String bindDate,
                         @RequestParam(required = false) String bindLatLongitude,
                         @RequestParam(required = false) String bindAddress,
                         @RequestParam(required = false) String detailAddress,
                         @RequestParam(required = false) String manufacturer,
                         @RequestParam(required = false) String deviceNO,
                         @RequestParam(required = false) String ID)throws Exception {
        Response response = new Response();
        Msg msg = new Msg();
        response.setDate("false");
        response.setCode("999");
        msg.setResponseMsg("失败");
        response.setMsg(msg);

        BindList bindList = new BindList();
        bindList.setPhone(phone);
        bindList.setCardNo(cardNo);
        bindList.setProductName(productName);
        bindList.setModel(model);
        bindList.setNO(NO);
        bindList.setBindDate(bindDate);
        bindList.setBindLatLongitude(bindLatLongitude);
        bindList.setBindAddress(bindAddress);
        bindList.setDetailAddress(detailAddress);
        bindList.setManufacturer(manufacturer);
        bindList.setDeviceNO(deviceNO);
        bindList.setID(ID);

        String i = "0";
        Boolean checkToken = checkToken(token, phone);
        if(checkToken){
            i = wxService.selectAdmin(phone);
            if (i != null && i.equals("1")) {
                //查询是否绑定
                String isBind = wxService.selBindDevice(bindList);
                if(isBind !=null ){
                    //已绑定，执行解绑操作
                    int unBind = wxService.unBind(bindList);
                    String deleteThing = HttpRequest.sendGet("http://119.3.142.105/Thingworx/Things/GuanLongDeviceWeChat/Services/DeleteThing" ,
                            "method=post&ThingName="+cardNo+"&userid=chenxiaohan&password=1111111111");
                    if(unBind>0){
                        response.setDate("success");
                        response.setCode("000");
                        msg.setResponseMsg("解绑成功");
                        response.setMsg(msg);
                    }else{
                        response.setDate("false");
                        response.setCode("999");
                        msg.setResponseMsg("解绑失败");
                        response.setMsg(msg);
                    }
                }else{
                    //未绑定，执行绑定操作
                    //生成对应二维码
                    String NOPicture = "C:/ThingworxStorage/repository/GLDevice.Images/"+System.currentTimeMillis()+"_"+NO+".jpg";
                    String cardNoPicture = "C:/ThingworxStorage/repository/GLDevice.Images/"+System.currentTimeMillis()+"_"+cardNo+".jpg";
                    String NOText = "@"+productName+"@"+model+"@"+NO;
                    String cardNoText = "@"+ID+"@"+cardNo;
                    QRCodeUtil.encode(NOText, null, NOPicture, true);
                    QRCodeUtil.encode(cardNoText, null, cardNoPicture, true);
                    bindList.setCardNoPicture(cardNoPicture);
                    bindList.setNOPicture(NOPicture);

                    //判断该经纬度是否存在
                    String selExists = wxService.selExists(bindLatLongitude);
                    if(selExists != null && selExists.length() != 0){
                        response.setDate("success");
                        response.setCode("997");
                        msg.setResponseMsg("该位置已有绑定设备");
                        response.setMsg(msg);
                    }else{
                        int bindDevice = wxService.bindDevice(bindList);
                        String createThing = HttpRequest.sendGet("http://119.3.142.105/Thingworx/Things/GuanLongDeviceWeChat/Services/CreateThing" ,
                                "method=post&ThingName="+cardNo+"&userid=chenxiaohan&password=1111111111");
                        if(bindDevice>0){
                            response.setDate("success");
                            response.setCode("000");
                            msg.setResponseMsg("绑定成功");
                            response.setMsg(msg);
                        }else{
                            response.setDate("false");
                            response.setCode("999");
                            msg.setResponseMsg("绑定失败");
                            response.setMsg(msg);
                        }
                    }
                }
            }else{
                response.setDate("false");
                response.setCode("002");
                msg.setResponseMsg("您没有操作设备的权限");
                response.setMsg(msg);
            }
        }else{
            response.setDate("false");
            response.setCode("001");
            msg.setResponseMsg("Token异常");
            response.setMsg(msg);
        }

        return response;
    }

    /**
     * 2.9.	查询绑定信息
     * @param token
     * @param phone
     * ...
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/selBindInfo", method = RequestMethod.POST)
    public Response selBindInfo(@RequestParam String token,
                            @RequestParam String phone,
                            @RequestParam(required = false) String NO,
                            @RequestParam(required = false) String cardNo,
                            @RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize)throws Exception {
        Response response = new Response();
        Msg msg = new Msg();
        response.setDate("false");
        response.setCode("999");
        msg.setResponseMsg("失败");
        response.setMsg(msg);

        String i = "0";
        Boolean checkToken = checkToken(token, phone);
        if(checkToken){
            //超级管理员可以查询所有信息
            String supAdmin = wxService.selectSuperAdmin(phone);
            if(supAdmin!=null&&supAdmin.equals("1")){
                phone = null;
            }

            page = (page-1)*pageSize;
            //sql里面做过处理，将安装位置和详细位置拼接成安装位置发送给前端
            List<BindList> bindList= wxService.selBindInfo(phone,NO,cardNo,page,pageSize);
            if(bindList!=null&&bindList.size()>0){
                response.setDate("success");
                response.setCode("000");
                msg.setResponseMsg("查询成功");
                msg.setBindList(bindList);
                response.setMsg(msg);
            }else{
                response.setDate("false");
                response.setCode("995");
                msg.setResponseMsg("未查询到绑定信息");
                response.setMsg(msg);
            }
        }else{
            response.setDate("false");
            response.setCode("001");
            msg.setResponseMsg("Token异常");
            response.setMsg(msg);
        }

        return response;
    }

    /**
     * 3.1.	根据日期查询绑定的设备
     * 设备安装位置信息查询功能，在设备详情页面，能根据日期查询绑定的设备
     * 超管能查询该日期内所有的绑定设备，管理员能查询该日期内本公司所有的绑定设备
     * @param token
     * @param phone
     * @param startDate
     * @param endDate
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/selBindByDate", method = RequestMethod.POST)
    public Response selBindByDate(@RequestParam String token,
                                  @RequestParam String phone,
                                  @RequestParam(required = false) String startDate,
                                  @RequestParam(required = false) String endDate,
                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize)throws Exception {
        Response response = new Response();
        Msg msg = new Msg();
        response.setDate("false");
        response.setCode("999");
        msg.setResponseMsg("失败");
        response.setMsg(msg);

        String i = "0";
        Boolean checkToken = checkToken(token, phone);
        if(checkToken){
            //超级管理员可以查询所有信息
            String supAdmin = wxService.selectSuperAdmin(phone);
            if(supAdmin!=null&&supAdmin.equals("1")){
                phone = null;
            }

            page = (page-1)*pageSize;
            List<BindList> bindList= wxService.selBindByDate(phone,startDate,endDate,page,pageSize);
            if(bindList!=null&&bindList.size()>0){
                response.setDate("success");
                response.setCode("000");
                msg.setResponseMsg("查询成功");
                msg.setBindList(bindList);
                response.setMsg(msg);
            }else{
                response.setDate("false");
                response.setCode("995");
                msg.setResponseMsg("未查询到绑定信息");
                response.setMsg(msg);
            }
        }else{
            response.setDate("false");
            response.setCode("001");
            msg.setResponseMsg("Token异常");
            response.setMsg(msg);
        }

        return response;
    }

    /**
     * 3.2.	判断设备是否已绑定
     * 通过小程序扫描二维码获得的阀门类型信息与采集器信息或者手输这两个信息上传进行绑定和解绑操作之前，
     * 先用该接口验证该设备是否已绑定，已绑定则跳转到设备详情页面。
     * （未绑定则继续执行绑定操作。）
     * @param token
     * @param phone
     * @param NO
     * @param cardNo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/judgeBind", method = RequestMethod.POST)
    public Response judgeBind(@RequestParam String token,
                         @RequestParam String phone,
                         @RequestParam(required = false) String NO,
                         @RequestParam(required = false) String cardNo)throws Exception {
        Response response = new Response();
        Msg msg = new Msg();
        response.setDate("false");
        response.setCode("999");
        msg.setResponseMsg("查询失败");
        response.setMsg(msg);

        String i = "0";
        Boolean checkToken = checkToken(token, phone);
        if(checkToken){
            BindList bindList = new BindList();
            bindList.setPhone(phone);
            bindList.setCardNo(cardNo);
            bindList.setNO(NO);

            i = wxService.selectAdmin(phone);
            if (i != null && i.equals("1")) {
                //查询是否绑定
                String isBind = wxService.selBindDevice(bindList);
                if(isBind !=null && isBind.equals("1")){
                    //已绑定，返回绑定信息
                    response.setDate("success");
                    response.setCode("002");
                    msg.setResponseMsg("已绑定");
                    response.setMsg(msg);
                }else{
                    //未绑定，返回未绑定信息
                    response.setDate("success");
                    response.setCode("003");
                    msg.setResponseMsg("未绑定");
                    response.setMsg(msg);
                }
            }else{
                response.setDate("false");
                response.setCode("002");
                msg.setResponseMsg("您没有操作设备的权限");
                response.setMsg(msg);
            }
        }else{
            response.setDate("false");
            response.setCode("001");
            msg.setResponseMsg("Token异常");
            response.setMsg(msg);
        }

        return response;
    }

}
