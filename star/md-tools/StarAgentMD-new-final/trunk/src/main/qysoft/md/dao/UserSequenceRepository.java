package main.qysoft.md.dao;

import main.qysoft.jpa.support.CustomRepository;
import main.qysoft.md.entity.MdTrade;

/**
 * Created by kevin on 2017/10/19.
 */
public interface UserSequenceRepository  extends CustomRepository<MdTrade, Long> {
}
