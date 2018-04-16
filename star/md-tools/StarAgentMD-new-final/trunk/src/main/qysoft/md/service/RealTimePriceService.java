package main.qysoft.md.service;

import main.qysoft.md.dao.RealTimePriceRepository;
import main.qysoft.md.entity.RealTimePrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by kevin on 2017/11/3.
 */
@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackForClassName = {"RuntimeException", "Exception"})
public class RealTimePriceService {
    @Autowired
    RealTimePriceRepository realTimePriceRepository;

    public void save(RealTimePrice realTimePrice) {
        if (null == realTimePrice) {
            return;
        }

        realTimePriceRepository.save(realTimePrice);
    }
}
