package com.tomdog.reward.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.spring.ioc.Bean;
import com.spring.ioc.Injector;
import com.tomdog.netty.util.ResourcesService;
import com.tomdog.reward.dto.NeoMapDto;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.tomdog.reward.constant.Constant.DomainResolve.*;
import static com.tomdog.reward.util.ClockUtil.dateTimeFormatter;

@Bean
@Slf4j
public class DomainResolveService {

    private final NeoMapService neoMapService;

    private final Cache<String, String> objectCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .softValues()
            .weakKeys()
            .build();

    public DomainResolveService() {
        Injector injector = Injector.getInstance();
        neoMapService = injector.getBean(NeoMapService.class);
    }

    public String getHostName() {
        return neoMapService.selectByName(HOST_NAME)
                .map(NeoMapDto::getValue)
                .orElseThrow(() -> new RuntimeException("host_name is blank"));
    }

    public static File getHostsFile() {
        return new File(ResourcesService.getInstance().getPath("/static") + "/hosts.txt");
    }

    public File resolve() {
        File file = getHostsFile();
        boolean tempFlag = false;
        if (!file.exists()) {
            try {
                boolean flag = file.createNewFile();
            } catch (IOException e) {
                tempFlag = true;
                file = FileUtil.createTempFile();
            }
        }
        Optional<NeoMapDto> neoMapDto = neoMapService.selectByName(DOMAIN_RESOLVE);
        if (neoMapDto.isEmpty()
                || neoMapDto.get().getModifyTime().plusHours(1).isBefore(LocalDateTime.now())) {
            // 单例，可以直接锁住当前对象
            synchronized (this) {
                // 再次check，避免重复更新
                if (!check()) {
                    return file;
                }
                String str;
                try {
                    str = this.resolveDomain(this.getRawUrl());
                } catch (UnknownHostException e) {
                    // 解析失败，返回之前结果
                    log.error("resolve failed,exception is ", e);
                    return file;
                }
                FileUtil.writeUtf8String(str, file);
                neoMapDto.ifPresentOrElse(
                        (obj) -> neoMapService.updateValueByName(DOMAIN_RESOLVE, str),
                        () -> neoMapService.insert(DOMAIN_RESOLVE, str)
                );
            }
        } else if (tempFlag) {
            FileUtil.writeUtf8String(neoMapDto.get().getValue(), file);
        }
        return file;
    }

    /**
     * 解析域名
     */
    private String resolveDomain(String domains) throws UnknownHostException {
        StringBuilder str = new StringBuilder();
        Pattern.compile(",").splitAsStream(domains).forEach(key -> {
            String value;
            try {
                value = InetAddress.getByName(key).getHostAddress();
            } catch (UnknownHostException e) {
                log.error("{} resolve failed", key);
                throw new RuntimeException(e);
            }
            str.append(value).append(" ").append(key).append("\n");
        });

        // 显示更新时间
        str.append("# ").append(LocalDateTime.now().format(dateTimeFormatter));
        return str.toString();
    }

    private boolean check() {
        Optional<NeoMapDto> neoMapDto = neoMapService.selectByName(DOMAIN_RESOLVE);
        return neoMapDto.isEmpty()
                || neoMapDto.get().getModifyTime().plusHours(1).isBefore(LocalDateTime.now());
    }

    /**
     * 获取需要解析的url集合
     */
    private String getRawUrl() {
        return Optional.ofNullable(objectCache.getIfPresent(RAW_URL))
                .orElseGet(() -> {
                    Optional<NeoMapDto> neoMapDto2 = neoMapService.selectByName(RAW_URL);
                    if (neoMapDto2.isEmpty() || StrUtil.isBlank(neoMapDto2.get().getValue())) {
                        log.error("raw_url is blank");
                        throw new RuntimeException("raw_url is blank");
                    }
                    objectCache.put(RAW_URL, neoMapDto2.get().getValue());
                    return neoMapDto2.get().getValue();
                });
    }

}
