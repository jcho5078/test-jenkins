package com.rds.testrds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rds.testrds.entity.OrderEntity;
import com.rds.testrds.entity.UserEntity;
import com.rds.testrds.repository.UserRepository;
import com.rds.testrds.vo.RequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "getUsers")
    public List<UserEntity> getUsers(){
        return userRepository.findAll();
    }

    @RequestMapping(value = "getUser")
    public UserEntity getUser(UserEntity user){
        return userRepository.findByUserName(user.getUserName());
    }

    @RequestMapping(value = "setUser")
    public Map<String, Object> setUser(@RequestBody RequestVO requestVO) throws Exception{

        UserEntity user = requestVO.getUser();
        List<OrderEntity> orders = requestVO.getOrders();
        //user.setOrders(orders);

        Map<String, Object> result = new HashMap<>();

        try{
            UserEntity userEntity = userRepository.save(user);
            result.put("result", "SUCCESS");
            result.put("user", userEntity);
        }catch (DataIntegrityViolationException e){
            result.put("result", "FAIL");
        }

        return result;
    }
}
