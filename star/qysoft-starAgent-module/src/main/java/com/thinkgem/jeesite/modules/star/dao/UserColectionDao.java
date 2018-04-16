/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.star.entity.UserColection;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 收藏表DAO接口
 * @author luojie
 * @version 2017-09-28
 */
@MyBatisDao
public interface UserColectionDao extends CrudDao<UserColection> {

public List<UserColection> findByUserName(String userName);

public UserColection getByNameAndId(@Param("loginName") String loginName, @Param("groupId") String groupId);

    void cancelColection(@Param("userName") String userName, @Param("groupId") String groupId);
}