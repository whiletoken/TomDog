package com.tomdog.reward.request.api;

import com.spring.ioc.Injector;
import com.tomdog.netty.annotation.GetMapping;
import com.tomdog.netty.annotation.RequestMapping;
import com.tomdog.netty.annotation.RestController;
import com.tomdog.reward.service.DomainResolveService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

@RestController
@RequestMapping(value = "/domain")
@Slf4j
public class DomainResolveApi {

    private final DomainResolveService resolveService;

    public DomainResolveApi() {
        Injector injector = Injector.getInstance();
        this.resolveService = injector.getBean(DomainResolveService.class);
    }

    @GetMapping(value = "/getHostName")
    public String getHostName() {
        return resolveService.getHostName();
    }

    @GetMapping(value = "/resolve")
    public RandomAccessFile resolve() {
        File file = resolveService.resolve();
        try {
            log.info("resolve path is {}", file.getPath());
            return new RandomAccessFile(file, "rwd");
        } catch (FileNotFoundException e) {
            log.error("RandomAccessFile create failed, path is {}", file.getPath());
            throw new RuntimeException(e);
        }
    }

}
