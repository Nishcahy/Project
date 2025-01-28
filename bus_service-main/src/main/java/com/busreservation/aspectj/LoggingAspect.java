package com.busreservation.aspectj;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	//logger before all method
    @Before("execution(* com.busreservation.service.BusServiceImpl.*(..))")
    public void logBeforeAllMethods(JoinPoint joinPoint) {
        logger.info("*********LoggingAspect.logBeforeAllMethods() in BusService   : {}" ,joinPoint.getSignature().getName());
    }
    
    
    //logging before findBusByID method
    @Before("execution(* com.busreservation.service.BusServiceImpl.findBusById(..))")
    public void logBeforefindBusById(JoinPoint joinPoint) {
        logger.info("*********LoggingAspect.logBeforefindBusById() :{} " ,joinPoint.getSignature().getName());
    }
    
    //logging After add employee
    @After("execution(* com.busreservation.service.BusServiceImpl.*(..))")
    public void logAfterAddEmployee(JoinPoint joinPoint) {
        logger.info("*********LoggingAspect.logAfterMethod () : {}", joinPoint.getSignature().getName());
    }

}
