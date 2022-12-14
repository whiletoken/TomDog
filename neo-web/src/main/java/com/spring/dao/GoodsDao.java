package com.spring.dao;

import com.spring.dto.Goods;
import org.apache.ibatis.annotations.Param;

/**
 * GoodsDao
 *
 * @author liujunjie
 */
public interface GoodsDao {

    /**
     * update
     *
     * @param type type
     * @param id   id
     */
    void update(@Param("type") Long type, @Param("id") Long id);

    /**
     * selectById
     *
     * @param id id
     * @return long
     */
    Long selectById(@Param("id") Long id);

    /**
     * insert
     *
     * @param goods goods
     */
    void insert(Goods goods);
}
