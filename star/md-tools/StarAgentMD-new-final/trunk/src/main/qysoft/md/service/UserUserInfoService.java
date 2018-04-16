package main.qysoft.md.service;

import main.qysoft.md.dao.UserAccountchangeRepository;
import main.qysoft.md.dao.UserUserinfoRepository;
import main.qysoft.md.entity.UserAccountchange;
import main.qysoft.md.entity.UserUserinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by kevin on 2017/11/2.
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception"})
public class UserUserInfoService {
    @Autowired
    UserUserinfoRepository userUserinfoRepository;

    @Autowired
    UserAccountchangeRepository userAccountchangeRepository;

    Optional<UserUserinfo> findById(Long userId) {
        if (null == userId) {
            return Optional.empty();
        }

        return Optional.ofNullable(userUserinfoRepository.findById(userId));
    }

    /**
     * 根据ID批量查询用户
     * @param ids
     * @return
     */
    List<UserUserinfo> findUsersById(Iterable<Long> ids) {
        return userUserinfoRepository.findAll(ids);
    }

    /**
     * 是否是承销商
     * @param userInfo
     * @param goodsGroupId
     * @return
     */
    public boolean isUserCenter(UserUserinfo userInfo, String goodsGroupId) {
        if (null == userInfo || StringUtils.isEmpty(goodsGroupId)) {
            return false;
        }

       return userInfo.getIsUserCenter() == UserUserinfo.IS_USER_CENTER && goodsGroupId.equals(userInfo.getUserCenterAddress());
    }

    /**
     * 修改用户余额
     * @param userId
     * @param money
     * @param isUserCenter
     * @param changeType
     * @param message
     */
    public void updateUserMoney(Long userId, BigDecimal money, boolean isUserCenter, String changeType, String message) {
        if (null == userId || null == money || BigDecimal.ZERO.equals(money)) {
            return;
        }

        UserUserinfo user = userUserinfoRepository.findLockById(userId);
        if (null == user) {
            return;
        }

        BigDecimal beforeMoney;
        if (isUserCenter) {
            beforeMoney = user.getMoney2();
            user.setMoney2(beforeMoney.add(money));
        } else {
            beforeMoney = user.getMoney();
            user.setMoney(beforeMoney.add(money));
        }

        userUserinfoRepository.save(user);

        // 插入帐变
        UserAccountchange accountchange = new UserAccountchange();
        if (isUserCenter) {
            accountchange.setBeforeMoney(beforeMoney);
            accountchange.setAfterMoney(user.getMoney2());
            accountchange.setMoneyType("3");
        } else {
            accountchange.setBeforeMoney(beforeMoney);
            accountchange.setAfterMoney(user.getMoney());
            accountchange.setMoneyType("1");
        }

        accountchange.setChangeMoney(money);
        accountchange.setCommt(message);
        accountchange.setCreateDate(new Date());
        accountchange.setIsCheck("0");
        accountchange.setUserName(user.getUserName());
        accountchange.setStatus("1");
        accountchange.setChangeType(changeType);
        userAccountchangeRepository.save(accountchange);
    }

    /**
     * 根据Id获取用户，并锁定行
     * @param userId
     * @return
     */
    public Optional<UserUserinfo> findLockById(Long userId) {
        if (null == userId) {
            return Optional.empty();
        }

        return Optional.ofNullable(userUserinfoRepository.findLockById(userId));
    }
}
