package com.rds.testrds.mapper;

import com.rds.testrds.entity.OrderEntity;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

public interface OrderMapper {

    @Select("SELECT * FROM t_order WHERE ID = #{id}")
    public Optional<OrderEntity> findById(long id);

    public OrderEntity findById2(long id);
}
