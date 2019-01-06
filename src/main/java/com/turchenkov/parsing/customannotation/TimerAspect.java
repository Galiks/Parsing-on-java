package com.turchenkov.parsing.customannotation;

import com.turchenkov.parsing.domains.Timer;
import com.turchenkov.parsing.repository.TimerRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Aspect
public class TimerAspect {

    @Autowired
    private TimerRepository timerRepository;

    @Around("@annotation(Timer)")
    public Object loadTime(ProceedingJoinPoint joinPoint) throws Throwable {

        Long startTime = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        Long endTime = System.currentTimeMillis();

        timerRepository.save(new Timer(joinPoint.getTarget().toString(), (endTime-startTime)/1000, new Date().toString()));

        return proceed;

    }
}
