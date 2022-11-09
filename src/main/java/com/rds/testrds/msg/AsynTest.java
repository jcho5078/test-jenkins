package com.rds.testrds.msg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rds.testrds.entity.OrderEntity;
import com.rds.testrds.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class AsynTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Async("executor")
    public String test1(String userName) throws Exception {
        System.out.println("start");
        DeferredResult<String> stringDeferredResult = new DeferredResult<String>();
        //db I/O작업 처리
        List<OrderEntity> order = orderRepository.getExistOrderInfoFetchJoin(userName);

        //json 일괄처리
        String json = objectMapper.writeValueAsString(order);
        stringDeferredResult.setResult(json);

        //결과값 처리
        stringDeferredResult.setResult(json);

        return json;
    }

    @Async("executor")
    public Future test2(String userName) throws Exception {
        System.out.println("start");
        //db I/O작업 처리
        List<OrderEntity> order = orderRepository.getExistOrderInfoFetchJoin(userName);

        //json 일괄처리
        String json = objectMapper.writeValueAsString(order);

        //결과값 처리
        AsyncResult result = new AsyncResult(json);

        return result;
    }

    @Async("executor")
    public ListenableFuture<String> test3(String userName) throws Exception {

        System.out.println("start");
        //db I/O작업 처리
        List<OrderEntity> order = orderRepository.getExistOrderInfoFetchJoin(userName);

        //json 일괄처리
        String json = objectMapper.writeValueAsString(order);

        //결과값 처리
        AsyncResult result = new AsyncResult(json);

        return result;
    }

    @Async("executor")
    public CompletableFuture test4(String userName) throws Exception {

        //db I/O작업 처리
        //List<OrderEntity> order = orderRepository.getExistOrderInfoFetchJoin(userName);
        //결과값 처리
        //CompletableFuture result = new AsyncResult(order).completable();


        //json 일괄처리
        //String json = objectMapper.writeValueAsString(order);

        return CompletableFuture.supplyAsync(() -> {
            return orderRepository.getExistOrderInfoFetchJoin(userName);
        });

        /*return CompletableFuture.supplyAsync(() -> {
            return orderRepository.getExistOrderInfoFetchJoin(userName).parallelStream().collect(Collectors.toList());
        });*/

        //return result;
    }

    public CompletableFuture test5(String userName) throws Exception {

        //db I/O작업 처리
        List<OrderEntity> order = orderRepository.getExistOrderInfoFetchJoin(userName);
        //결과값 처리
        CompletableFuture result = new AsyncResult(order).completable();


        //json 일괄처리
        //String json = objectMapper.writeValueAsString(order);

        /*return CompletableFuture.supplyAsync(() -> {
            return orderRepository.getExistOrderInfoFetchJoin(userName);
        });*/

        /*return CompletableFuture.supplyAsync(() -> {
            return orderRepository.getExistOrderInfoFetchJoin(userName).parallelStream().collect(Collectors.toList());
        });*/

        return result;
    }
}
