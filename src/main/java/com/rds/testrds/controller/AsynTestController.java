package com.rds.testrds.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rds.testrds.entity.OrderEntity;
import com.rds.testrds.msg.AsynTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@RestController
public class AsynTestController {

    @Autowired
    private AsynTest asynTest;

    @RequestMapping("testAsync1")
    @ResponseBody
    public DeferredResult<List<String>> test1() throws Exception {

        DeferredResult<List<String>> deferredResult = new DeferredResult<>();
        List<String> result = new ArrayList<>();

        result.add(asynTest.test1("John2"));
        /*Thread thread = Thread.currentThread();
        System.out.println(thread.getId());*/
        result.add(String.valueOf(deferredResult.getResult()));
        deferredResult.setResult(result);

        return deferredResult;
    }

    @RequestMapping("testAsync2")
    @ResponseBody
    public List<String> test2() throws Exception {

        List<String> result = new ArrayList<>();

        Future<String> future = asynTest.test2("John2");
        /*Thread thread = Thread.currentThread();
        System.out.println(thread.getId());*/
        result.add(future.get());
        return result;
    }

    @RequestMapping("testAsync3")
    @ResponseBody
    public List<String> test3() throws Exception {

        List<String> result = new ArrayList<>();

        ListenableFuture<String> listenableFuture = asynTest.test3("John2");
        listenableFuture.addCallback(json ->
        {result.add(json); System.out.println(json);/*Thread thread = Thread.currentThread();
            System.out.println(thread.getId());*/}, ex -> {});
        return result;
    }

    @RequestMapping("testAsync4")
    @ResponseBody
    public CompletableFuture<OrderEntity> test4() throws Exception {

        return asynTest.test4("John2");

        /*for(int i=0; i<5; i++){
            CompletableFuture<String> completableFuture = asynTest.test4("John2");
            completableFuture.thenAccept(json -> {
                        result.add(json);
                        Thread thread = Thread.currentThread();
                        System.out.println(thread.getId());
                        System.out.println(json);
            }).exceptionally(error -> {
                        System.out.println(error.getMessage());
                        return null;
                    });
        }*/
        //CompletableFuture<String> completableFuture = asynTest.test4("John2");
        /*completableFuture*//*.thenApply(json -> {
            result.add(json);
            Thread thread = Thread.currentThread();
            System.out.println(thread.getId());
            System.out.println(json);
            return result;
        })*//*.thenAccept(json -> {
            result.add(String.valueOf(json));
            *//*Thread thread = Thread.currentThread();
            System.out.println(thread.getId());*//*
        }).exceptionally(error -> {
            System.out.println(error.getMessage());
            return null;
        });*/

        //List<OrderEntity> result = asynTest.test4("John2");

        //return asynTest.test4("John2").;
    }
}
