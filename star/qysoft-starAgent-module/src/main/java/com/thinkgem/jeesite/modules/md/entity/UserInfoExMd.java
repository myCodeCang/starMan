package com.thinkgem.jeesite.modules.md.entity;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by kevin on 2017/10/1.
 */
public class UserInfoExMd extends UserUserinfo{
    private MdTradeLog mdTradeLog;

    public MdTradeLog getMdTradeLog() {
        return mdTradeLog;
    }

    public void setMdTradeLog(MdTradeLog mdTradeLog) {
        this.mdTradeLog = mdTradeLog;
    }
}
