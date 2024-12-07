package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoty extends JpaRepository<User,Long> {
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.userId = :userId")
    int updatePassword(@Param("userId") Long userId, @Param("password") String password);

}
