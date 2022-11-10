package com.rds.testrds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rds.testrds.entity.OrderEntity;
import com.rds.testrds.entity.QOrderEntity;
import com.rds.testrds.entity.QUserEntity;
import com.rds.testrds.entity.UserEntity;
import com.rds.testrds.mapper.OrderMapper;
import com.rds.testrds.repository.OrderRepository;
import com.rds.testrds.repository.UserRepository;
import com.rds.testrds.vo.RequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    private OrderMapper orderMapper;

    @Autowired
    public OrderController(OrderMapper orderMapper){
        this.orderMapper = orderMapper;
    }

    @Autowired
    private ObjectMapper objectMapper;

    @PersistenceContext
    EntityManager em;

    @RequestMapping(value = "setOrder")
    public Map<String, Object> setOrder(@RequestBody RequestVO requestVO) throws Exception{

        UserEntity user = requestVO.getUser();
        List<OrderEntity> orders = requestVO.getOrders();
        for (OrderEntity order:orders) {
            order.setUser(user);
        }

        Map<String, Object> result = new HashMap<>();

        try{
            orders = orderRepository.saveAll(orders);
            result.put("result", "SUCCESS");
            result.put("orders", orders);
        }catch (DataIntegrityViolationException e){
            result.put("result", "FAIL");
        }

        return result;
    }

    @RequestMapping(value = "getOrders")
    public List<OrderEntity> getOrders(){
        List<OrderEntity> orders = orderRepository.findAll();
        return orders;
    }

    @RequestMapping(value = "getOrdersByUserName")
    public List<OrderEntity> getOrdersByUserName(String userName){
        List<OrderEntity> order = orderRepository.getExistOrderInfoByUserName(userName);
        return order;
    }

    @RequestMapping(value = "testGetOrdersByUserName")
    public UserEntity testGetOrdersByUserName(String userName){
        List<OrderEntity> order = orderRepository.testGetOrdersByUserName(userName);

        System.out.println("-- 프록시 객체 접근 오류 테스트");
        String userNm = order.get(0).getUser().getUserName();
        String userPw = order.get(0).getUser().getUserPw();

        System.out.println("-- 프록시 객체 접근 오류 테스트2");
        UserEntity result = order.get(0).getUser();

        return result;
    }

    @RequestMapping(value = "getExistOrderInfoFetchJoin")
    public List<OrderEntity> getExistOrderInfoFetchJoin(String userName) throws Exception{
        List<OrderEntity> order = orderRepository.getExistOrderInfoFetchJoin(userName);

        //objectMapper.writeValueAsString(order)
        //pubSubMessagePublisher.publish(order.get(0)); //이벤트 처리 테스트

        return order;
    }

    @RequestMapping(value = "getOrdersByUserName2")
    @Transactional
    public List<OrderEntity> getOrdersByUserName2(String userName){
        em.persist(new OrderEntity());

        em.flush();
        em.clear();
        QOrderEntity qOrder = new QOrderEntity("o");
        QUserEntity qUser = new QUserEntity("u");
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        List<OrderEntity> order = (List<OrderEntity>) queryFactory.from(qOrder)
                .where(qOrder.user.userName.contains(userName)).fetch();

        List<OrderEntity> order2 = (List<OrderEntity>) queryFactory.from(qOrder)
                .join(qOrder.user, qUser)
                .where(qUser.userName.contains(userName)).fetch();

        return order;
    }

    @RequestMapping(value = "getOrdersByUserName3")
    @Transactional
    public List<OrderEntity> getOrdersByUserName3(String userName, @PageableDefault(page = 0, size = 2) Pageable pageable){
        em.persist(new OrderEntity());

        em.flush();
        em.clear();
        QOrderEntity qOrder = new QOrderEntity("o");
        QUserEntity qUser = new QUserEntity("u");
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        List<OrderEntity> order = (List<OrderEntity>) queryFactory.from(qOrder)
                .where(qOrder.user.userName.contains(userName))
                .orderBy(qOrder.id.desc())
                .offset(pageable.getOffset())
                .limit(2)
                .fetch();

        return order;
    }

    @RequestMapping(value = "testMybatis")
    public OrderEntity testMybatis(long id){
        OrderEntity order = orderMapper.findById(id).get();
        return order;
    }

    @RequestMapping(value = "testMybatis2")
    public OrderEntity testMybatis2(long id){
        OrderEntity order = orderMapper.findById2(id);
        return order;
    }
}
