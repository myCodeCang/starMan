package com.thinkgem.jeesite.modules.user.service;


import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.modules.user.dao.UserAccountchangeDao;
import com.thinkgem.jeesite.modules.user.dao.UserUserinfoDao;
import com.thinkgem.jeesite.modules.user.entity.UserAddress;
import com.thinkgem.jeesite.modules.user.entity.UserTeamLevel;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
import com.thinkgem.jeesite.modules.user.entity.WorkProject;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * ,用户业务层
 */
@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackForClassName = {"RuntimeException", "Exception", "ValidationException"})
public class UserEjfxService {
    @Resource
    private UserUserinfoService userUserinfoService;
    @Resource
    private UserUserinfoDao userinfoDao;
    @Resource
    private WorkProjectService workProjectService;

    @Resource
    private UserAddressService userAddressService;


    @Resource
    private UserAccountchangeDao userAccountchangeDao;
    @Resource
    private UserTeamLevelService userTeamLevelService;
    private ApplicationContext context;


    public void buyUpLevelGoods(String userName,String projectId,String addressId) throws ValidationException{

        UserUserinfo userinfo = userUserinfoService.getByNameLock(userName);
        WorkProject workProject = workProjectService.get(projectId);
        UserAddress userAddress = userAddressService.get(addressId);
        if(userinfo == null){
            throw new ValidationException("操作的用户不存在!");
        }
//        if(EnumUtil.YesNO.YES.toString().equals(userinfo.getActiveStatus())){
//            throw new ValidationException("您已购买成功,请勿重复购买!");
//        }
        if(workProject == null){
            throw new ValidationException("所购买的商品不存在!");
        }
        if(userAddress == null){
            throw new ValidationException("未找到收货地址!");
        }
        if(!userinfo.getUserName().equals(userAddress.getUserName())){
            throw new ValidationException("请填写本人的收货地址!");
        }
        if(userinfo.getMoney().compareTo(workProject.getMoney())<0){
            throw new ValidationException("余额不足,请先去充值",1001);
        }
        userUserinfoService.updateUserMoney(userName,workProject.getMoney().multiply(BigDecimal.valueOf(-1)),"购买升级商品", EnumUtil.MoneyChangeType.BuyWorkProject);


    }

    private void grantTeamAward(String userName, BigDecimal money) {
        UserUserinfo userUserinfo = userUserinfoService.getByNameLock(userName);
        String parentListStr = userUserinfo.getParentList();
        String[] parentListArray = parentListStr.split("/");
        List<UserTeamLevel> teamList = userTeamLevelService.findList(new UserTeamLevel());
        int userLevelNo =0 ;
        BigDecimal teamMoney;
        for (String parentStr : parentListArray) {
            if (StringUtils2.isBlank(parentStr)) {
                continue;
            }
            UserUserinfo parentUser = userUserinfoService.get(parentStr);
            if (null == parentUser) {
                continue;
            }
            userLevelNo++;
            int parentOldLevel = 0;
            try {
                parentOldLevel = Integer.parseInt(parentUser.getUserType());
            } catch (NumberFormatException e) {
                throw new ValidationException("用户团队等级错误");
            }
            //下级列表
            UserUserinfo userinfo = new UserUserinfo();
            userinfo.setParentName(parentUser.getUserName());
            userinfo.setActiveStatus(EnumUtil.YesNO.YES.toString());
            List<UserUserinfo> zhituiList = userUserinfoService.findList(userinfo);

            UserUserinfo allUserinfo = new UserUserinfo();
            allUserinfo.setParentListLike(parentStr);
            allUserinfo.setActiveStatus(EnumUtil.YesNO.YES.toString());
            List<UserUserinfo> allUserinfoList = userUserinfoService.findList(allUserinfo);

            UserUserinfo twoUserinfo = new UserUserinfo();
            twoUserinfo.setLevelNo(parentUser.getLevelNo()+2);
            twoUserinfo.setParentListLike(parentStr);
            twoUserinfo.setActiveStatus(EnumUtil.YesNO.YES.toString());
            List<UserUserinfo> TwoUserinfoList = userUserinfoService.findList(twoUserinfo);

            //本日收益
           BigDecimal mixBonus =  userAccountchangeDao.sumMoneyByChangeTypebydDay(parentUser.getUserName(),EnumUtil.MoneyChangeType.bonus.toString(),new Date());

            //返利算法
            if(parentUser.getActiveStatus().equals(EnumUtil.YesNO.YES.toString()) && userLevelNo>=3){

                for (UserTeamLevel levelLists : teamList) {
                    int levelCodes;
                    try {
                        levelCodes = Integer.parseInt(levelLists.getTeamCode());
                    } catch (NumberFormatException e) {
                        throw new ValidationException("团队等级配置错误");
                    }
                    if (levelCodes <= 0) {
                        continue;
                    }
                    if (levelCodes == parentOldLevel) {
                        teamMoney = money.multiply(new BigDecimal(levelLists.getDirectEarnings())).setScale(2,BigDecimal.ROUND_HALF_UP);
                        if(teamMoney.add(mixBonus).compareTo(new BigDecimal(levelLists.getIndirectEarnings()))>0 && teamMoney.compareTo(BigDecimal.ZERO)>0){
                            teamMoney = new BigDecimal(levelLists.getIndirectEarnings()).subtract(mixBonus);
                        }
                        userUserinfoService.updateUserMoney(parentUser.getUserName(),teamMoney,"团队奖励", EnumUtil.MoneyChangeType.bonus);
                        break;
                    }
                }
            }
            int userNewLevel = 0;
            //用户升级
            if (zhituiList.size() > 0) {
                for (UserTeamLevel levelList : teamList) {
                    int levelCode = Integer.parseInt(levelList.getTeamCode());
                    int zhiUserNum = levelList.getDirectPeopleNum(); //直推人数
                    int jianUserNum = levelList.getIndirectLevelno();//二代人数
                    int countUserNum = levelList.getTeamPeopleNum(); //总人数
                    if (parentOldLevel < levelCode && zhituiList.size() >= zhiUserNum && allUserinfoList.size() >= countUserNum && TwoUserinfoList.size()>= jianUserNum) {
                        List<UserUserinfo> teamByUserType = userUserinfoService.findTeamByUserType(parentUser.getId(), (levelCode - 1));
                        if(teamByUserType.size()>=1){
                            userNewLevel = levelCode;
                        }
                    }
                }
            }
            if(parentOldLevel<userNewLevel){
                userUserinfoService.updateUserType(parentUser.getUserName(),String.valueOf(userNewLevel));
            }
        }
    }

    private void grantPromotionAward(String userName, BigDecimal money) {
        UserUserinfo userinfo = userUserinfoService.getByNameLock(userName);
        //获取配置
        BigDecimal performanceOne;
        BigDecimal performanceTwo;
        try {
            performanceOne = new BigDecimal(Global.getOption("system_ejfx","performance_one","0.1"));
            performanceTwo = new BigDecimal(Global.getOption("system_ejfx","performance_two","0.15"));
        } catch (Exception e) {
            throw new ValidationException("直推奖金配置错误");
        }

        //给一代发奖
        UserUserinfo oneUser = userUserinfoService.getByNameLock(userinfo.getParentName());
        if(oneUser == null){
            throw new ValidationException("一代用户未找到");
        }
        userUserinfoService.updateUserMoney(oneUser.getUserName(),money.multiply(performanceOne).setScale(2,BigDecimal.ROUND_HALF_UP),"直属推广奖励(一代),用户:"+userinfo.getUserName(), EnumUtil.MoneyChangeType.ZhituiMoney);
        //给二代发奖
        if(StringUtils2.isNotBlank(oneUser.getParentName())){
            userUserinfoService.updateUserMoney(oneUser.getParentName(),money.multiply(performanceTwo).setScale(2,BigDecimal.ROUND_HALF_UP),"推广奖励(二代),用户:"+userinfo.getUserName(), EnumUtil.MoneyChangeType.JiantuiMoney);
        }
    }

    public void shopBuyTeamBouns(String mobile, String orderMoney) throws ValidationException {
        UserUserinfo userUserinfo = userUserinfoService.getByMobile(mobile);
        String parentListStr = userUserinfo.getParentList();
        String[] parentListArray = parentListStr.split("/");
        List<UserTeamLevel> teamList = userTeamLevelService.findList(new UserTeamLevel());
        BigDecimal money = new BigDecimal(orderMoney);
        BigDecimal teamMoney;
        for (String parentName :parentListArray){
            if (StringUtils2.isBlank(parentName)) {
                continue;
            }
            UserUserinfo parentUser = userUserinfoService.get(parentName);
            if (null == parentUser) {
                continue;
            }
            int parentLevel = Integer.parseInt(parentUser.getUserType());
            //返利算法(一星,二星)
            if(parentUser.getActiveStatus().equals(EnumUtil.YesNO.YES.toString())){
                for (UserTeamLevel levelLists : teamList) {
                    int levelCodes;
                    try {
                        levelCodes = Integer.parseInt(levelLists.getTeamCode());
                    } catch (NumberFormatException e) {
                        throw new ValidationException("团队等级配置错误");
                    }
                    if(levelCodes == 3){
                        continue;
                    }
                    if (levelCodes <= 0) {
                        continue;
                    }
                    if (levelCodes == parentLevel) {
                        teamMoney = money.multiply(new BigDecimal(levelLists.getMessage())).setScale(2,BigDecimal.ROUND_HALF_UP);
                        userUserinfoService.updateUserScore(parentUser.getUserName(),teamMoney,"商城消费团队奖励", EnumUtil.MoneyChangeType.CONSUMPTION);
                        break;
                    }
                }
            }
        }
        //董事返利
        UserUserinfo dongshi = new UserUserinfo();
        dongshi.setActiveStatus(EnumUtil.YesNO.YES.toString());
        dongshi.setUserType("3");
        List<UserUserinfo> dongshiList = userUserinfoService.findList(dongshi);
        UserTeamLevel dongTeam =  userTeamLevelService.getTeamNameByTeamCode("3");
        if(dongTeam == null){
            throw new ValidationException("董事消费奖励配置错误");
        }
        for(UserUserinfo dongshiUser : dongshiList){
            userUserinfoService.updateUserScore(dongshiUser.getUserName(),money.multiply(new BigDecimal(dongTeam.getMessage())),"商城消费,董事返利", EnumUtil.MoneyChangeType.CONSUMPTION);
        }
    }

    public void shopBuyBouns(String mobile, String orderMoney) {
        UserUserinfo userinfo = userUserinfoService.getByMobile(mobile);
        BigDecimal money = new BigDecimal(orderMoney);
        BigDecimal oneBonus;
        BigDecimal twoBonus;
        try {
            oneBonus = new BigDecimal(Global.getOption("system_ejfx","shop_one"));
            twoBonus =  new BigDecimal(Global.getOption("system_ejfx","shop_two"));
        } catch (Exception e) {
            throw new ValidationException("商城消费奖励,配置错误");
        }
        //给一代发奖
        UserUserinfo oneUser = userUserinfoService.getByNameLock(userinfo.getParentName());
        if(oneUser == null){
            throw new ValidationException("一代用户未找到");
        }
        userUserinfoService.updateUserScore(oneUser.getUserName(),money.multiply(oneBonus).setScale(2,BigDecimal.ROUND_HALF_UP),"商城消费奖励(一代),用户:"+userinfo.getUserName(), EnumUtil.MoneyChangeType.PROMOTE_CONSUMPTION);
        //给二代发奖
        if(StringUtils2.isNotBlank(oneUser.getParentName())){
            userUserinfoService.updateUserScore(oneUser.getParentName(),money.multiply(twoBonus).setScale(2,BigDecimal.ROUND_HALF_UP),"商城消费奖励(二代),用户:"+userinfo.getUserName(), EnumUtil.MoneyChangeType.PROMOTE_BUSINESS);
        }
    }
}
