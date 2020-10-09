package com.test.aop.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Aspect para a execu��o de logs dos servi�os e reposit�rios dos componentes
 * Spring.
 *
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

	/**
	 * Pointcut que corresponde a todos os reposit�rios, servi�os e endpoints REST
	 */
	@Pointcut("within(@org.springframework.stereotype.Repository *)"
			+ " || within(@org.springframework.stereotype.Service *)"
			+ " || within(@org.springframework.web.bind.annotation.RestController *)")
	public void springBeanPointcut() {
		// O m�todo est� vazio, pois este � apenas um ponto de corte (Pointcut)
		// as implementa��es est�o nos advices.
	}

	/**
	 * Pointcut que corresponde a todos os beans Spring nos pacotes principais do
	 * aplicativo.
	 */
	@Pointcut("within(com.test.aop..*)" + " || within(com.test.aop.service..*)"
			+ " || within(com.test.aop.controller..*)")
	public void applicationPackagePointcut() {
		// O m�todo est� vazio, pois este � apenas um ponto de corte (Pointcut)
		// as implementa��es est�o nos advices.
	}

	/**
	 * Advice que registra m�todos lan�ando exce��es.
	 *
	 * @param joinPoint ponto de jun��o para advices
	 * @param e         exce��o
	 */
	@AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
		log.error("Exce��o em {}.{}() | com a causa = {}", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
	}

	/**
	 * Advice que registra quando um m�todo � inserido e encerrado.
	 *
	 * @param joinPoint ponto de jun��o para advices
	 * @return resultado
	 * @throws Throwable lan�a IllegalArgumentException
	 */
	@Around("applicationPackagePointcut() && springBeanPointcut()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//		if (log.isDebugEnabled()) {
		if (true) {
			log.info("Entrada: {}.{}() | com argumento[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
					joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
		}
		try {
			Object result = joinPoint.proceed();
//			if (log.isDebugEnabled()) {
			if (true) {
				log.info("Sa�da: {}.{}() | com resultado = {}", joinPoint.getSignature().getDeclaringTypeName(),
						joinPoint.getSignature().getName(), result);
			}
			return result;
		} catch (IllegalArgumentException e) {
			log.error("Argumento ilegal: {} | em {}.{}()", Arrays.toString(joinPoint.getArgs()),
					joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
			throw e;
		}
	}
}