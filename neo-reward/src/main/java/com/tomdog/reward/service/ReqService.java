package com.tomdog.reward.service;

import com.spring.ioc.Bean;
import com.spring.sql.HibernateUtil;
import com.tomdog.reward.dto.ReqInfo;
import jakarta.persistence.EntityManager;

import java.util.Optional;

@Bean
public class ReqService {

    public ReqService() {
    }

    public Optional<ReqInfo> selectByName(String name) {
        EntityManager entityManager = HibernateUtil.getEntityManger();
        return Optional.of((ReqInfo) entityManager.createQuery("select p from ReqInfo p where p.name = ?1")
                .setParameter(1, name)
                .getSingleResult());

    }

}
