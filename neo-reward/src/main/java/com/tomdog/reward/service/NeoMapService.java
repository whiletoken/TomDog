package com.tomdog.reward.service;

import com.spring.ioc.Bean;
import com.spring.sql.HibernateUtil;
import com.tomdog.reward.dto.NeoMapDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

@Bean
public class NeoMapService {

    public NeoMapService() {
    }

    public Optional<NeoMapDto> selectByName(String name) {
        EntityManager entityManager = HibernateUtil.getEntityManger();
        return Optional.of((NeoMapDto) entityManager.createQuery("select p from NeoMapDto p where p.name = ?1")
                .setParameter(1, name)
                .getSingleResult());
    }

    public Optional<List<NeoMapDto>> listByName(String name) {
        EntityManager entityManager = HibernateUtil.getEntityManger();
        return Optional.of(entityManager.createQuery("select p from NeoMapDto p where p.name = ?1")
                .setParameter(1, name).getResultList());
    }

    public int updateValueByName(String name, String value) {
        int updatedEntities;
        EntityManager entityManager = HibernateUtil.getEntityManger();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        String jpqlUpdate = "update NeoMapDto c set c.value = :newValue"
                + " where c.name = :oldName";
        updatedEntities = entityManager.createQuery(jpqlUpdate)
                .setParameter("newValue", value)
                .setParameter("oldName", name)
                .executeUpdate();

        tx.commit();
        return updatedEntities;
    }

    public void insert(String name, String value) {
        EntityManager entityManager = HibernateUtil.getEntityManger();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        NeoMapDto neoMapDto = new NeoMapDto();
        neoMapDto.setName(name);
        neoMapDto.setValue(value);
        entityManager.persist(neoMapDto);

        tx.commit();
    }

}
