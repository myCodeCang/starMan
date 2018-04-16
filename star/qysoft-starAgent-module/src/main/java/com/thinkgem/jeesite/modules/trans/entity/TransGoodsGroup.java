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
import java.util.List;

/**
 * 艺术品委托管理Entity
 *
 * @author luo
 * @version 2017-05-10
 */
public class TransGoodsGroup extends DataEntity<TransGoodsGroup> {
    @ExcelField(title="艺术品编号", align=2, sort=1,value="id")
    private static final long serialVersionUID = 1L;

    @ExcelField(title="艺术品名称", align=2, sort=2)
    private String name;          // 艺术品名称
    @ExcelField(title="标题", align=2, sort=3)
    private String title;         // 标题
    private String message;       // 简介
    private String detail;        // 详情
    private String type;          // 类型
    @ExcelField(title="状态", align=2, sort=4,dictType = "qy_trans_group")
    private String status = "2";  //默认下架
    @ExcelField(title="创建时间", align=2, sort=5,value="createDate")
    private BigDecimal startMoney;
    private String photo;
    private String num;

    //扩展字段
    private TransBuyDayTrend transBuyDayTrend;
    private List<TransBuyDayTrend> transBuyDayTrendList;




    /**
     * 验证模型字段
     */
    public void validateModule(boolean isInsert) throws ValidationException {

		if(StringUtils2.isBlank(status)){
			throw new ValidationException("状态不能为空!");
		}
        if (StringUtils2.isBlank(name)) {
            throw new ValidationException("艺术品名称不能为空!");
        }
        if (StringUtils2.isBlank(title)) {
            throw new ValidationException("标题不能为空!");
        }
        if (StringUtils2.isBlank(photo)) {
            throw new ValidationException("艺术品图片不能为空!");
        }
        if (StringUtils2.isBlank(detail)) {
            throw new ValidationException("详情不能为空!");
        }
        if (startMoney == null) {
            throw new ValidationException("初始价格不能为空!");
        }
        if (startMoney.compareTo(BigDecimal.ZERO)<0) {
            throw new ValidationException("初始价格不能为负数");
        }
        if (StringUtils2.isBlank(status)) {
            throw new ValidationException("状态不能为空!");
        }

    }


	@Override
	public void preInsert() throws ValidationException {
       /* this.status = "2";*/

        validateModule(true);
		super.preInsert();
		if (!this.isNewRecord) {
			setId(IdGen.uuid("seq_transGoods"));
		}
	}



    @Override
    public void preUpdate() throws ValidationException {
        validateModule(false);
        super.preUpdate();


    }

    public List<TransBuyDayTrend> getTransBuyDayTrendList() {
        return transBuyDayTrendList;
    }

    public void setTransBuyDayTrendList(List<TransBuyDayTrend> transBuyDayTrendList) {
        this.transBuyDayTrendList = transBuyDayTrendList;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public BigDecimal getStartMoney() {
        return startMoney;
    }

    public void setStartMoney(BigDecimal startMoney) {
        this.startMoney = startMoney;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public TransGoodsGroup() {
        super();
    }

    public TransGoodsGroup(String id) {
        super(id);
    }

    @Length(min = 0, max = 100, message = "艺术品名称长度必须介于 0 和 100 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 0, max = 500, message = "标题长度必须介于 0 和 500 之间")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Length(min = 0, max = 255, message = "简介长度必须介于 0 和 255 之间")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Length(min = 0, max = 255, message = "详情长度必须介于 0 和 255 之间")
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Length(min = 0, max = 1, message = "类型长度必须介于 0 和 1 之间")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public TransBuyDayTrend getTransBuyDayTrend() {
        return transBuyDayTrend;
    }

    public void setTransBuyDayTrend(TransBuyDayTrend transBuyDayTrend) {
        this.transBuyDayTrend = transBuyDayTrend;
    }
}