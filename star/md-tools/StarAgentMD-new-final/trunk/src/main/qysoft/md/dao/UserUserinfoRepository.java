package main.qysoft.md.dao;

import main.qysoft.md.entity.UserUserinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;

/**
 * Created by kevin on 2017/10/23.
 */
public interface UserUserinfoRepository extends JpaRepository<UserUserinfo, Long> {
    UserUserinfo findById(long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select u from UserUserinfo u where u.id = :userId")
    //@Query(value = "select * from user_userinfo u where u.id = ?1 limit 1 for update", nativeQuery = true)
    UserUserinfo findLockById(@Param("userId") long userId);
}
