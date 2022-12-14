package com.spring.controller;

import com.spring.base.BaseController;
import com.spring.dto.Goods;
import com.spring.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * GoodsController
 *
 * @author william
 **/

@RestController
@RequestMapping(value = "goods")
public class GoodsController extends BaseController {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value = "update")
    public Map<String, Integer> update(@RequestParam(value = "id") Long id) {

        goodsService.update(2L, id);

        Map<String, Integer> jsonObject = new HashMap<>(4);
        jsonObject.put("update", 123);

        return jsonObject;
    }

    @RequestMapping(value = "insert")
    public Map<String, String> insert(@RequestParam(value = "id") Long id) {

        goodsService.insert(new Goods(id, "test", 2L));

        Map<String, String> jsonObject = new HashMap<>(4);
        jsonObject.put("insert", "insert");

        return jsonObject;
    }


}
