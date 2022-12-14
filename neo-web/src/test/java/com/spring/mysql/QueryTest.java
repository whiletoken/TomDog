package com.spring.mysql;

import com.spring.Application;
import com.spring.service.GoodsService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * 查询测试
 *
 * 1、loop 查库不加事务会比较慢，因为每次查库都需要重新从连接池中获取新的连接，但是一次事务内会持有连接
 *
 * @author william
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class QueryTest {

    @Autowired
    private GoodsService goodsService;

    @Test
    public void loopQueryWithOutTraction() {

        long startTime = System.currentTimeMillis();

        for (int i = 0; i <= 9999; i++) {
            goodsService.select(1L, (long) i);
        }

        System.out.println("cost time is " + (System.currentTimeMillis() - startTime));
    }

    @Test
    @Transactional
    public void loopQueryWithTraction() {

        long startTime = System.currentTimeMillis();

        for (int i = 0; i <= 9999; i++) {
            goodsService.select(1L, (long) i);
        }

        System.out.println("cost time is " + (System.currentTimeMillis() - startTime));
    }

}
