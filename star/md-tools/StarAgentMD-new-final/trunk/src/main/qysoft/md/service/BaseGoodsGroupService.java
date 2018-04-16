package main.qysoft.md.service;

import com.google.common.collect.Lists;
import main.qysoft.md.dao.BaseGoodsGroupRepository;
import main.qysoft.md.entity.BaseGoodsGroup;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackForClassName = {"RuntimeException", "Exception"})
public class BaseGoodsGroupService {
    @Autowired
    BaseGoodsGroupRepository baseGoodsGroupRepository;

    public List<BaseGoodsGroup> findByIdIn(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Lists.newArrayList();
        }

        return baseGoodsGroupRepository.findByIdIn(ids);
    }

    public List<BaseGoodsGroup> findAllGoodsOnTrading() {
        return baseGoodsGroupRepository.findBaseGoodsGroupsByStatus("1");
    }
}
