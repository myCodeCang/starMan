/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.user.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.user.entity.UserWithdraw;

import java.util.List;

/**
 * 用户提现DAO接口
 * @author kevin
 * @version 2017-03-23
 */
@MyBatisDao
public interface UserWithdrawDao extends CrudDao<UserWithdraw> {

    public void updateStatus(UserWithdraw userWithdraw);
    public void updateRemittanceStatus(UserWithdraw userWithdraw);

}