package cz.muni.fi.pa165.currency;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import javax.inject.Named;
import java.util.concurrent.TimeUnit;

@Named
@Aspect
public class LoggingAspect {

    @Around("execution(public * *(..))")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.nanoTime();

        Object result = joinPoint.proceed();

        long endTime = System.nanoTime();
        long nanoDuration = endTime - startTime;
        long milliDuration = TimeUnit.MILLISECONDS.convert(nanoDuration, TimeUnit.NANOSECONDS);
        String method = joinPoint.getSignature().getName();
        System.out.println("Duration of method " + method + " was " + milliDuration + " ms (" + nanoDuration + " ns)");

        return result;
    }
}
