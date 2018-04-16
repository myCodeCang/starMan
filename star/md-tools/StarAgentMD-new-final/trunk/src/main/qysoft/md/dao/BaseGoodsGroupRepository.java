package main.qysoft.md.dao;

import main.qysoft.jpa.support.CustomRepository;
import main.qysoft.md.entity.BaseGoodsGroup;

import java.util.Collection;
import java.util.List;

public interface BaseGoodsGroupRepository extends CustomRepository<BaseGoodsGroup, Long> {
    List<BaseGoodsGroup> findByIdIn(Collection<Long> ids);

    List<BaseGoodsGroup> findBaseGoodsGroupsByStatus(String status);
}
