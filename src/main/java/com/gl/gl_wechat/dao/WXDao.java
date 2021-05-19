package com.gl.gl_wechat.dao;

import com.gl.gl_wechat.entity.Admin;
import com.gl.gl_wechat.entity.BindList;
import com.gl.gl_wechat.entity.UserList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: zhaoCong
 * @date: 2020-04-03 16:59
 */
@Repository
public interface WXDao {
    String selectSuperAdmin(@Param("phone") String phone);

    List<UserList> selectUserList(@Param("phone") String phone);

    int addUser(@Param("admin") Admin admin);

    int delUser(@Param("phone") String phone);

    int updateUser(@Param("adminPhone") String adminPhone,@Param("phone")String phone,@Param("name")String name,
                   @Param("company")String company,@Param("number")String number,@Param("update_time")String update_time);

    List<Admin> selUser(@Param("phone")String phone);

    List<Admin> selUserList();

    String selectAdmin(@Param("phone") String phone);

    String selAdmin(@Param("phone") String phone);

    String selBindDevice(@Param("bindList") BindList bindList);

    String selExists(@Param("bindLatLongitude") String bindLatLongitude);

    int unBind(@Param("bindList") BindList bindList);

    int bindDevice(@Param("bindList") BindList bindList);

    List<BindList> selBindInfo(@Param("phone")String phone,@Param("NO")String NO,@Param("cardNo")String cardNo,@Param("page")int page,@Param("pageSize")int pageSize);

    List<BindList> selBindByDate(@Param("phone")String phone,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("page")int page,@Param("pageSize")int pageSize);

}
