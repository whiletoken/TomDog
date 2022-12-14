package com.tomdog.reward.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity // 一个基于JPA规范的实体类
@Table(name = "neo_request_info") //指定当前实体类关联的表
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ReqInfo {

    @Id // 标识为主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //指定主键生成策略
    private Integer id;

    private String name;

    private String header;

    private String body;

    @Temporal(TemporalType.TIMESTAMP) // 定义时间类型
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Temporal(TemporalType.TIMESTAMP) // 定义时间类型
    @Column(name = "modify_time")
    private LocalDateTime modifyTime;

}
