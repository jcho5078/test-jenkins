package com.rds.testrds.repository;

import com.rds.testrds.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public UserEntity findByUserName(String userName);
}
