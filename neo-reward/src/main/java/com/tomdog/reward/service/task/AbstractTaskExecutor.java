package com.tomdog.reward.service.task;

import com.tomdog.reward.hystrix.HystrixCommand;
import com.tomdog.reward.service.TaskExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

import static com.tomdog.reward.util.ClockUtil.sleep;

@Slf4j
public abstract class AbstractTaskExecutor implements TaskExecutor {

    private final AtomicInteger failNum = new AtomicInteger(0);

    @HystrixCommand(timeout = 30000, fallback = "failBack")
    public void doJob() {
        this.job();
    }

    abstract void job();

    public void failBack() {
        log.info("第{}次失败重试", failNum.getAndIncrement());
        if (failNum.get() > 5) {
            log.error("重试超过上限，停止重试");
            failNum.set(0);
            return;
        }
        try {
            job();
        } catch (Exception e) {
            sleep(60 + failNum.get() * 2);
            failBack();
        }
    }


}
