package main.qysoft.md.service;

import main.qysoft.md.dao.TransGoodsRepository;
import main.qysoft.md.entity.TransGoods;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by kevin on 2017/11/4.
 */
@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackForClassName = {"RuntimeException", "Exception"})
public class TransGoodsService {
    @Autowired
    TransGoodsRepository transGoodsRepository;

    public Optional<TransGoods> findLockByUserNameAndGroupId(String userName, String groupId) {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(groupId)) {
            return Optional.empty();
        }

        return Optional.ofNullable(transGoodsRepository.findLockByUserNameAndGroupId(userName, groupId));
    }

    /**
     * 获取已发布的商品ID
     * @return
     */
    public List<Long> findPublishedGoodsId() {
        return transGoodsRepository.findPublishedGoodsId();
    }

    public void save(TransGoods transGoods) {
        if (transGoods == null) {
            return;
        }

        transGoodsRepository.save(transGoods);
    }
}
