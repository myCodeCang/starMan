/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.trans.entity.TransGoods;
import com.thinkgem.jeesite.modules.trans.entity.TransGoodsKuiSun;
import com.thinkgem.jeesite.modules.trans.entity.TransGoodsNumReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户持有交易品表DAO接口
 *
 * @author luo
 * @version 2017-05-08
 */
@MyBatisDao
public interface TransGoodsDao extends CrudDao<TransGoods> {


    //分组统计用户交易品情况
    public List<TransGoods> findByGroup(TransGoods entity);

    //统计用户亏损列表
    public List<TransGoodsKuiSun> findKunSunRanklist(TransGoodsKuiSun transGoods);



    //锁表查询
    public List<TransGoods> findListLock(TransGoods entity);

    public TransGoods getUserStar(@Param("groupId") String groupId,@Param("userName") String userName);

    public TransGoodsNumReport getGoodsNumReport(String groupId);

    public TransGoods getByBuyId(String buyId);

    void updateNum(@Param("id") String id,@Param("num") int num);

    TransGoods getLock(String id);

    void deleteExceed(@Param("groupIdArray")String[] groupIdArray);
}