package com.altera.invoice;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.altera.invoice.entity.Logging;
import com.altera.invoice.repository.LoggingRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Configuration
@Slf4j
public class AuditTrail {
	
	@Autowired 
	private HttpServletRequest request;
	
	@Autowired
	private LoggingRepository loggingRepository;
	
	public static ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

	@Before("execution(* com.altera.invoice.controller.*.*.*(..))")
	public void before(JoinPoint joinPoint) throws JsonProcessingException {
		Logging logging = new Logging();
		HashMap<String, Object> data = new HashMap<>();
		String methodName = joinPoint.getSignature().getName();
		String simpleName = joinPoint.getTarget().getClass().getSimpleName();
		log.info("Before method : " + methodName + " Class name  : "
				+ simpleName);
		Object[] objects = joinPoint.getArgs();
		
		log.info("Request URL : {}", request.getRequestURI());
		data.put("Uri", request.getRequestURI());
		for (Object object : objects) {
			String writeValueAsString = objectWriter.writeValueAsString(object);
			log.info("Request : \n" + writeValueAsString);
			data.put("Request Body", writeValueAsString);
		}
		data.put("Method", methodName);
		data.put("Class name", simpleName);
		logging.setId(UUID.randomUUID().toString());
		logging.setType("log-before request");
		logging.setData(data);
		
		loggingRepository.save(logging);
		
		log.info("BEFORE - SUCCES");
	}

	@AfterReturning(pointcut = "(execution(* com.altera.invoice.controller.*.*.*(..))) ", returning = "result")
	public void afterReturning(JoinPoint joinPoint, Object result) throws IOException {
		Logging logging = new Logging();
		HashMap<String, Object> data = new HashMap<>();
		
		String simpleName = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		log.info("AfterReturning method : " + methodName + " Class name  : "
				+ simpleName);

		String requestURI = request.getRequestURI();
		log.info("Request URL : {}", requestURI);
		String res = objectWriter.writeValueAsString(result);
		log.info("Response : " + res);
		
		data.put("Uri", requestURI);
		data.put("Response", res);
		data.put("Method", methodName);
		data.put("Class name", simpleName);
		logging.setId(UUID.randomUUID().toString());
		logging.setType("log-after request");
		logging.setData(data);
		
		loggingRepository.save(logging);

	}
}
