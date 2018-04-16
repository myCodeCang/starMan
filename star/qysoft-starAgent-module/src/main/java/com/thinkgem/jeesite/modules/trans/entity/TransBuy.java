/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.entity;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

/**
 * 艺术品交易大厅Entity
 *
 * @author luo
 * @version 2017-05-10
 */
public class TransBuy extends DataEntity<TransBuy> {
    @ExcelField(title="交易编号", align=2, sort=1,value="id")
    private static final long serialVersionUID = 1L;

    @ExcelField(title="售卖人用户名", align=2, sort=2)
    private String sellUserName;        // 售卖人用户名
    @ExcelField(title="艺术品编号", align=2, sort=4)
    private String groupId;        // 分组编号
    @ExcelField(title="艺术品名称", align=2, sort=5)
    private String goodsName;        // 商品名称
    @ExcelField(title="单价", align=2, sort=8)
    private BigDecimal money;        // 单价
    @ExcelField(title="商品状态", align=2, sort=7,dictType = "qy_goods_status")
    private String status;        // 状态
    @ExcelField(title="交易类型", align=2, sort=6,dictType = "qy_trans_buy")
    private String type;
    @ExcelField(title="剩余数量", align=2, sort=10)
    private Integer nowNum;  //剩余数量
    @ExcelField(title="下架数量", align=2, sort=11)
    private Integer downNum; //下架数量
    @ExcelField(title="售卖数量", align=2, sort=9)
    private Integer sellNum;        // 发布的售卖数量



    //扩展字段
    @ExcelField(title="真实姓名", align=2, sort=3)
    private String trueName;
    @ExcelField(title="创建时间", align=2, sort=12,value="createDate")
    private String[] groupIdArray; //查询

    /**
     * 插入之前执行方法，需要手动调用
     */
    @Override
    public void preInsert() throws ValidationException {

        this.downNum = 0;

        validateModule(true);
        super.preInsert();
        // 不限制ID为UUID，调用setIsNewRecord()使用自定义ID
        if (!this.isNewRecord) {
            setId(IdGen.uuid("seq_transBuy"));


        }
}

	@Override
	public void preUpdate() throws ValidationException {
		validateModule(false);
		super.preUpdate();
	}




    /**
     * 验证模型字段
     */
    public void validateModule(boolean isInsert) throws ValidationException {


        if (StringUtils2.isBlank(sellUserName)) {
            throw new ValidationException("售卖人用户名不能为空!");
        }
        if (StringUtils2.isBlank(groupId)) {
            throw new ValidationException("交易品编号不能为空!");
        }
        if (StringUtils2.isBlank(goodsName)) {
            throw new ValidationException("商品名称不能为空!");
        }


        if (money == null) {
            throw new ValidationException("商品单价不能为空!");
        }
        if (null == sellNum) {
            throw new ValidationException("售卖数量不能为空!");
        }
        if (StringUtils2.isBlank(status)) {
            throw new ValidationException("商品状态不能为空!");
        }
        if (StringUtils2.isBlank(type)) {
            throw new ValidationException("账单类型不能为空!");
        }


    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public TransBuy() {
        super();
    }

    public TransBuy(String id) {
        super(id);
    }



    @Length(min = 0, max = 255, message = "售卖人用户名长度必须介于 0 和 255 之间")
    public String getSellUserName() {
        return sellUserName;
    }

    public void setSellUserName(String sellUserName) {
        this.sellUserName = sellUserName;
    }

    @Length(min = 0, max = 11, message = "分组编号长度必须介于 0 和 11 之间")
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Length(min = 0, max = 255, message = "商品名称长度必须介于 0 和 255 之间")
    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }


    public String[] getGroupIdArray() {
        return groupIdArray;
    }

    public void setGroupIdArray(String[] groupIdArray) {
        this.groupIdArray = groupIdArray;
    }

    @Length(min = 0, max = 255, message = "单价长度必须介于 0 和 255 之间")
    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Length(min = 0, max = 11, message = "售卖数量长度必须介于 0 和 11 之间")
    public Integer getSellNum() {
        return sellNum;
    }

    public void setSellNum(Integer sellNum) {
        this.sellNum = sellNum;
    }

    @Length(min = 0, max = 1, message = "状态长度必须介于 0 和 1 之间")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Integer getNowNum() {
        return nowNum;
    }

    public void setNowNum(Integer nowNum) {
        this.nowNum = nowNum;
    }

    public Integer getDownNum() {
        return downNum;
    }

    public void setDownNum(Integer downNum) {
        this.downNum = downNum;
    }
}