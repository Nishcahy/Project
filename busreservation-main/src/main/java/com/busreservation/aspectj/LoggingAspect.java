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

	// logger before all method
	@Before("execution(* com.busreservation.service.BusReservationServiceImpl.*(..))")
	public void logBeforeAllMethods(JoinPoint joinPoint) {
		logger.info("*********LoggingAspect.logBeforeMethods() in BusReservation   : {}",
				joinPoint.getSignature().getName());
	}

	// logging After reservation service
	@After("execution(* com.busreservation.service.BusReservationServiceImpl.*(..))")
	public void logAfterAddEmployee(JoinPoint joinPoint) {
		logger.info("*********LoggingAspect.logAfterMethod () : {}", joinPoint.getSignature().getName());
	}

}
