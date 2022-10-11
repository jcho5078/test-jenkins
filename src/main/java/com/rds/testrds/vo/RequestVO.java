package com.rds.testrds.vo;

import com.rds.testrds.entity.OrderEntity;
import com.rds.testrds.entity.UserEntity;
import lombok.Data;

import java.util.List;

@Data
public class RequestVO {

    private UserEntity user;
    private OrderEntity orderEntity;
    private List<OrderEntity> orders;
}
