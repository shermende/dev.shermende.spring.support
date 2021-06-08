package dev.shermende.support.spring.aop.intercept;

import dev.shermende.support.spring.aop.intercept.annotation.Intercept;
import dev.shermende.support.spring.aop.intercept.annotation.InterceptArgument;
import org.aspectj.lang.Aspects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {InterceptAspectWrongSupportTest.InterceptAspectWrongSupportTestConfiguration.class})
public class InterceptAspectWrongSupportTest {

    @Autowired
    private InterceptAspectWrongSupportTestComponent component;

    /**
     * wrong support
     */
    @Test(expected = IllegalArgumentException.class)
    public void interceptWrongSupportTest() {
        component.convert(new Object());
    }

    @ComponentScan
    @EnableAspectJAutoProxy(proxyTargetClass = true)
    public static class InterceptAspectWrongSupportTestConfiguration {
        @Bean
        public InterceptAspect interceptAspect() {
            return Aspects.aspectOf(InterceptAspect.class);
        }

        @Bean
        public InterceptResultAspect interceptResultAspect() {
            return Aspects.aspectOf(InterceptResultAspect.class);
        }
    }

    @Component
    public static class InterceptAspectWrongSupportTestComponent {
        @Intercept
        public Object convert(
            @InterceptArgument(InterceptAspectWrongSupportTestInterceptor.class) Object payload
        ) {
            return payload;
        }
    }

    @Component
    public static class InterceptAspectWrongSupportTestInterceptor implements Interceptor {
        @Override
        public boolean supports(
            Class<?> aClass
        ) {
            return Exception.class.isAssignableFrom(aClass);
        }

        @Override
        public void intercept(
            Object o
        ) {
        }
    }
}