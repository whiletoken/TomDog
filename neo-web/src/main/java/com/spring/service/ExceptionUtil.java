//package com.spring.service;
//
//import com.alibaba.csp.sentinel.slots.block.BlockException;
//
///**
// * Create Time: 2020/9/24
// *
// * @author <a href="mailto:liujj@shinemo.com">liujunjie</a>
// */
//public class ExceptionUtil {
//
//    public static void handleException(BlockException ex) {
//        // Handler method that handles BlockException when blocked.
//        // The method parameter list should match original method, with the last additional
//        // parameter with type BlockException. The return type should be same as the original method.
//        // The block handler method should be located in the same class with original method by default.
//        // If you want to use method in other classes, you can set the blockHandlerClass
//        // with corresponding Class (Note the method in other classes must be static).
//        System.out.println("Oops: " + ex.getClass().getCanonicalName());
//    }
//
//}
