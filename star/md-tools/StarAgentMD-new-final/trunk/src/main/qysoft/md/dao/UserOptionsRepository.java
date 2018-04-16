package main.qysoft.md.dao;

import main.qysoft.md.entity.UserOptions;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by kevin on 2017/10/25.
 */
public interface UserOptionsRepository extends JpaRepository<UserOptions, Long> {
    UserOptions findByGroupNameAndOptionName(String groupName, String optionName);
}


