package com.tomdog.gateway.servlet;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class SlowRequest {

    private AtomicInteger slowTimes;

    private AtomicInteger throughTimes;

    public SlowRequest() {
        slowTimes = new AtomicInteger(1);
        throughTimes = new AtomicInteger(3);
    }

    public int addSlowTimes() {
        // 半开开关关闭
        if (throughTimes.get() < 3) {
            throughTimes.getAndSet(3);
        }
        return slowTimes.incrementAndGet();
    }

    public boolean isThrough() {
        return slowTimes.get() <= 3;
    }

    public int addThroughTimes() {
        return throughTimes.decrementAndGet();
    }

}
