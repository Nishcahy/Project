package com.feedback.aspectj;


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
	//logger before feedback method
    @Before("execution(* com.feedback.service.FeedbackServiceImpl.*(..))")
    public void logBeforeAllMethods(JoinPoint joinPoint) {
        logger.info("*********LoggingAspect.logBeforeMethods() in Feedback  : {}" ,joinPoint.getSignature().getName());
    }
    
    
    
    //logging After Feedback service
    @After("execution(* com.feedback.service.FeedbackServiceImpl.*(..))")
    public void logAfterAddEmployee(JoinPoint joinPoint) {
        logger.info("*********LoggingAspect.logAfterMethod () : {}", joinPoint.getSignature().getName());
    }

}
