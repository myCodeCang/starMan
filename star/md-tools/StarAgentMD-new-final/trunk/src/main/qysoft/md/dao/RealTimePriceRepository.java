package main.qysoft.md.dao;

import main.qysoft.md.entity.RealTimePrice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by kevin on 2017/11/3.
 */
public interface RealTimePriceRepository extends JpaRepository<RealTimePrice, Long> {
}
