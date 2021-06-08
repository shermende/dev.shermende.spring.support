package dev.shermende.support.spring.aop.intercept;

import dev.shermende.support.spring.aop.intercept.annotation.InterceptArgument;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 *
 */
@Aspect
public class InterceptAspect implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(InterceptAspect.class);

    @Autowired
    private BeanFactory beanFactory;

    @Before("@annotation(dev.shermende.support.spring.aop.intercept.annotation.Intercept) && execution(public * *(..))")
    public void intercept(
        JoinPoint joinPoint
    ) throws NoSuchMethodException {
        final MethodSignature signature =
            (MethodSignature) joinPoint.getSignature();
        final Annotation[][] annotations = joinPoint.getTarget().getClass()
            .getMethod(signature.getMethod().getName(), signature.getMethod().getParameterTypes())
            .getParameterAnnotations();

        for (int i = 0; i < annotations.length; i++) {
            for (int j = 0; j < annotations[i].length; j++) {
                final Object arg = joinPoint.getArgs()[i];
                Optional.of(annotations[i][j])
                    .filter(annotation -> annotation.annotationType().equals(InterceptArgument.class))
                    .map(InterceptArgument.class::cast)
                    .map(interceptArgument -> beanFactory.getBean(interceptArgument.value()))
                    .ifPresent(interceptor -> interceptor.doIntercept(arg));
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.warn("Attention!!! @dev.shermende.support.spring.aop.intercept.annotation.Intercept annotation enabled");
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

}