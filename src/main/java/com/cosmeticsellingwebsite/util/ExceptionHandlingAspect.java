//package com.cosmeticsellingwebsite.util;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class ExceptionHandlingAspect {
//
//    @AfterThrowing(pointcut = "execution(* com.cosmeticsellingwebsite.*.*(..))", throwing = "ex")
//    public void logException(JoinPoint joinPoint, Exception ex) {
//        // Log the exception
//        System.err.println("Exception in method: " + joinPoint.getSignature());
//        System.err.println("Exception message: " + ex.getMessage());
//    }
//}