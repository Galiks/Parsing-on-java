package com.turchenkov.parsing.customannotation;

import com.turchenkov.parsing.domains.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Date;

@Aspect
public class ConsoleTimerAspect {

    @Around("@annotation(ConsoleTimer)")
    public Object loadTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Long startTime = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        Long endTime = System.currentTimeMillis();

        System.out.println("TIME!!!!!!!!!!!!" + new Timer(joinPoint.getTarget().toString(), (endTime-startTime)/1000, new Date().toString()));

        return proceed;
    }
}
