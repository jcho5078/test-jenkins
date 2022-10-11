package com.rds.testrds.repository;

import com.rds.testrds.entity.OrderEntity;
import com.rds.testrds.entity.UserEntity;
import com.rds.testrds.vo.RequestVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    public Optional<OrderEntity> findById(Long Id);

    @Query("select od " +
            "from OrderEntity od where od.user.userName in (select u.userName from UserEntity u where u.userName = :name)")
    public List<OrderEntity> getExistOrderInfoByUserName(@Param("name") String name);
}
