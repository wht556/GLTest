package com.gl.gl_wechat.service.impl;

import com.gl.gl_wechat.dao.WXDao;
import com.gl.gl_wechat.entity.Admin;
import com.gl.gl_wechat.entity.BindList;
import com.gl.gl_wechat.entity.UserList;
import com.gl.gl_wechat.service.WXService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: zhaoCong
 * @date: 2020-04-03 16:56
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
public class WXServiceImpl implements WXService {
    @Autowired
    private WXDao wxDao;

    public String selectSuperAdmin(String phone) {
        String phone1 = wxDao.selectSuperAdmin(phone);
        return phone1;
    }

    @Override
    public List<UserList> selectUserList(String phone) {
        return wxDao.selectUserList(phone);
    }

    @Override
    public int addUser(Admin admin) {
        return wxDao.addUser(admin);
    }

    @Override
    public int delUser(String phone) {
        return wxDao.delUser(phone);
    }

    @Override
    public int updateUser(String adminPhone,String phone,String name,String company,String number,String update_time) {
        return wxDao.updateUser(adminPhone,phone,name,company,number,update_time);
    }

    @Override
    public List<Admin> selUser(String phone) {
        return wxDao.selUser(phone);
    }

    @Override
    public List<Admin> selUser() {
        return wxDao.selUserList();
    }

    @Override
    public String selectAdmin(String phone) {
        return wxDao.selectAdmin(phone);
    }

    @Override
    public String selAdmin(String phone) {
        return wxDao.selAdmin(phone);
    }

    @Override
    public String selBindDevice(BindList bindList) {
        return wxDao.selBindDevice(bindList);
    }

    @Override
    public String selExists(String bindLatLongitude) {
        return wxDao.selExists(bindLatLongitude);
    }

    @Override
    public int unBind(BindList bindList) {
        return wxDao.unBind(bindList);
    }

    @Override
    public int bindDevice(BindList bindList) {
        return wxDao.bindDevice(bindList);
    }

    @Override
    public List<BindList> selBindInfo(String phone,String NO,String cardNo,int page,int pageSize) {
        return wxDao.selBindInfo(phone,NO,cardNo,page,pageSize);
    }

    @Override
    public List<BindList> selBindByDate(String phone,String startDate,String endDate,int page,int pageSize) {
        return wxDao.selBindByDate(phone,startDate,endDate,page,pageSize);
    }
}
