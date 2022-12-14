//package com.tomdog.spring.base;
//
//
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
///**
// * 定时任务
// * <p>
// * spring task 默认是单线程执行的，那么存在一个问题，
// * 如果当时间点到达之后仍然处在上次执行中，那么该任务会在下个时间点执行
// *
// * @author william
// **/
//
//@Component
//public class ScheduledTask {
//
//    /**
//     * cron表达式配置了在哪一刻执行任务，会在配置的任务开始时间判断任务是否可以执行，
//     * 如果能则执行，不能则会跳过本次执行
//     */
//    @Scheduled(cron = "0/10 * * * * ?")
//    @Async("taskExecutorOrder")
//    public void runfirst() throws InterruptedException {
//        Thread.sleep(15000);
//        log.info("********first job is ok******");
//    }
//
//    /**
//     * fixedRate
//     * 设置的上一个任务的开始时间到下一个任务开始时间的间隔
//     * 上一个任务结束后，下一个任务立刻开始执行
//     */
//    @Scheduled(fixedRate = 1000 * 10)
//    public void runsecend() {
//        log.info("********second job is ok******");
//    }
//
//    /**
//     * fixedDelay是设定上一个任务结束后多久执行下一个任务，
//     * 也就是fixedDelay只关心上一任务的结束时间和下一任务的开始时间。
//     */
//    @Scheduled(fixedDelay = 1000)
//    public void runThird() {
//        log.info("********third job is ok******");
//    }
//
//}
