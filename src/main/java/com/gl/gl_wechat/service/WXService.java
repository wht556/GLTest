package com.gl.gl_wechat.service;

import com.gl.gl_wechat.entity.Admin;
import com.gl.gl_wechat.entity.BindList;
import com.gl.gl_wechat.entity.UserList;

import java.util.List;

/**
 * @author: zhaoCong
 * @date: 2020-04-03 16:40
 */
public interface WXService {

    String selectSuperAdmin(String phone);

    List<UserList> selectUserList(String phone);

    int addUser(Admin admin);

    int delUser(String phone);

    int updateUser(String adminPhone,String phone,String name,String company,String number,String update_time);

    List<Admin> selUser(String phone);

    List<Admin> selUser();

    String selectAdmin(String phone);

    String selAdmin(String phone);

    String selBindDevice(BindList bindList);

    String selExists(String bindLatLongitude);

    int unBind(BindList bindList);

    int bindDevice(BindList bindList);

    List<BindList> selBindInfo(String phone,String NO,String cardNo,int page,int pageSize);

    List<BindList> selBindByDate(String phone,String startDate,String endDate,int page,int pageSize);

}
