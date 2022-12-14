//package com.spring.controller;
//
//import com.alibaba.csp.sentinel.annotation.SentinelResource;
//import com.alibaba.csp.sentinel.slots.block.BlockException;
//import com.alibaba.csp.sentinel.slots.block.RuleConstant;
//import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
//import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
//import com.spring.service.TestService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.PostConstruct;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * Create Time: 2020/9/24
// *
// * @author <a href="mailto:liujj@shinemo.com">liujunjie</a>
// */
//@RestController
//@RequestMapping(value = "/sentinel")
//public class SentinelController {
//
//    private final static Logger log = LoggerFactory.getLogger(SentinelController.class);
//
//    private static final String KEY = "abc";
//
//    private static AtomicInteger pass = new AtomicInteger();
//
//    /**
//     * SentinelResource 不生效的原因
//     * https://juejin.im/post/6844903999976505357
//     */
//    @SentinelResource(value = KEY, blockHandler = "degradeMethod")
//    @RequestMapping(value = "test")
//    public void test() {
//        pass.addAndGet(1);
//        System.out.println("______________ is " + pass.get());
//    }
//
//    @Autowired
//    private TestService service;
//
//    @GetMapping("/foo")
//    public String apiFoo(@RequestParam(required = false) Long t) throws Exception {
//        if (t == null) {
//            t = System.currentTimeMillis();
//        }
//        service.test();
//        return service.hello(t);
//    }
//
//    @GetMapping("/baz/{name}")
//    public String apiBaz(@PathVariable("name") String name) {
//        return service.helloAnother(name);
//    }
//
//    @PostConstruct
//    private static void initDegradeRule() {
//        List<FlowRule> rules = new ArrayList<>();
//        FlowRule rule = new FlowRule();
//        rule.setResource(KEY);
//        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
//        // Set limit QPS to 20.
//        rule.setCount(5);
//        rules.add(rule);
//        FlowRuleManager.loadRules(rules);
//    }
//
//    public static void degradeMethod(BlockException blockException) {
//        log.error("请求被熔断,触发熔断规则 {}", blockException.getRule().getResource());
//        return;
//    }
//
//}
