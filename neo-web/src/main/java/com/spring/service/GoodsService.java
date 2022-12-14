package com.spring.service;

import com.spring.dao.GoodsDao;
import com.spring.dto.Goods;
import org.springframework.stereotype.Service;

/**
 * GoodsService
 *
 * @author william
 **/

@Service
public class GoodsService {

    private final GoodsDao goodsDao;

    public GoodsService(GoodsDao goodsDao) {
        this.goodsDao = goodsDao;
    }

    public void update(Long type, Long id) {
        goodsDao.update(type, id);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int num = 1 / 0;
    }

    public Long select(Long type, Long id) {
        return goodsDao.selectById(id);
    }

    public void insert(Goods goods) {
        goodsDao.insert(goods);
    }

}
