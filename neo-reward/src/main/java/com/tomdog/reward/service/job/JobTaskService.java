package com.tomdog.reward.service.job;

import cn.hutool.cron.CronUtil;
import com.spring.ioc.Bean;
import com.spring.ioc.Injector;
import com.tomdog.reward.service.TaskExecutor;
import com.tomdog.reward.service.task.GenShinSignService;
import com.tomdog.reward.service.task.JingDongService;
import com.tomdog.reward.service.task.PaoLuYunService;
import com.tomdog.reward.util.ClockUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

import static com.tomdog.reward.constant.Constant.executorService;

@Bean
@Slf4j
public class JobTaskService {

    private final TaskExecutor genShinSignService;
    private final TaskExecutor jingDongService;
    private final TaskExecutor paoLuYunService;

    public JobTaskService() {
        Injector injector = Injector.getInstance();
        this.jingDongService = injector.getBean(JingDongService.class);
        this.genShinSignService = injector.getBean(GenShinSignService.class);
        this.paoLuYunService = injector.getBean(PaoLuYunService.class);
    }

    private static final AtomicInteger atomicInteger = new AtomicInteger(0);

    public void doJob() {
        log.info("job task starting");
        CronUtil.schedule("5 0 * * *", (Runnable) this::job);
        CronUtil.start();
        log.info("job task started");
    }

    public void job() {

        log.info("run times is {}", atomicInteger.getAndIncrement());

        executorService.execute(jingDongService::doJob);

        executorService.execute(() -> {
            // 0-20之间的随机数
            int sleepTime = (int) (Math.random() * 21);
            log.info("sleep time is {} minutes", sleepTime);
            ClockUtil.sleep(60 * sleepTime);
            genShinSignService.doJob();
        });

        executorService.execute(paoLuYunService::doJob);
    }
}
