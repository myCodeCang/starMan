package com.thinkgem.jeesite.modules.trans.entity;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.utils.VerifyUtils;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/6/12.
 */
public class AdminApplyTransGoods extends DataEntity<AdminApplyTransGoods> {
    private String userName;
    private String groupId;
    private BigDecimal money;
    private int num;
    private BigDecimal cost;


    /**
     * 验证模型字段
     */
    public  void validateModule() throws ValidationException {
        if(StringUtils2.isBlank(groupId)){
            throw new ValidationException("艺术品编号不能为空!");
        }
        if(StringUtils2.isBlank(userName)){
            throw new ValidationException("用户名不能为空!");
        }
        if(money.compareTo(BigDecimal.ZERO)<0){
            throw new ValidationException("请输入正确的单价");
        }
        if(!VerifyUtils.isInteger(String.valueOf(num))){
            throw new ValidationException("请输入正确的数量");
        }
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
