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
 * Aspect para a execução de logs dos serviços e repositórios dos componentes
 * Spring.
 *
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

	/**
	 * Pointcut que corresponde a todos os repositórios, serviços e endpoints REST
	 */
	@Pointcut("within(@org.springframework.stereotype.Repository *)"
			+ " || within(@org.springframework.stereotype.Service *)"
			+ " || within(@org.springframework.web.bind.annotation.RestController *)")
	public void springBeanPointcut() {
		// O método está vazio, pois este é apenas um ponto de corte (Pointcut)
		// as implementações estão nos advices.
	}

	/**
	 * Pointcut que corresponde a todos os beans Spring nos pacotes principais do
	 * aplicativo.
	 */
	@Pointcut("within(com.test.aop..*)" + " || within(com.test.aop.service..*)"
			+ " || within(com.test.aop.controller..*)")
	public void applicationPackagePointcut() {
		// O método está vazio, pois este é apenas um ponto de corte (Pointcut)
		// as implementações estão nos advices.
	}

	/**
	 * Advice que registra métodos lançando exceções.
	 *
	 * @param joinPoint ponto de junção para advices
	 * @param e         exceção
	 */
	@AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
		log.error("Exceção em {}.{}() | com a causa = {}", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
	}

	/**
	 * Advice que registra quando um método é inserido e encerrado.
	 *
	 * @param joinPoint ponto de junção para advices
	 * @return resultado
	 * @throws Throwable lança IllegalArgumentException
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
				log.info("Saída: {}.{}() | com resultado = {}", joinPoint.getSignature().getDeclaringTypeName(),
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