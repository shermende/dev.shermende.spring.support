package dev.shermende.support.spring.aop.intercept;

import dev.shermende.support.spring.aop.intercept.annotation.Intercept;
import dev.shermende.support.spring.aop.intercept.annotation.InterceptArgument;
import dev.shermende.support.spring.aop.intercept.annotation.InterceptResult;
import org.aspectj.lang.Aspects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {InterceptAspectTest.InterceptAspectTestConfiguration.class})
public class InterceptAspectTest {

    @SpyBean
    private InterceptAspectTestInterceptor interceptor;

    @Autowired
    private InterceptAspectTestComponent component;

    @Test
    public void intercept() {
        final Object object = new Object();
        component.convert(object);
        verify(interceptor, times(2)).doIntercept(object);
        verify(interceptor, times(2)).supports(object.getClass());
        verify(interceptor, times(2)).intercept(object);
    }

    @ComponentScan
    public static class InterceptAspectTestConfiguration {
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
    public static class InterceptAspectTestComponent {
        @Intercept
        @InterceptResult(InterceptAspectTestInterceptor.class)
        public Object convert(
            @InterceptArgument(InterceptAspectTestInterceptor.class) Object payload
        ) {
            return payload;
        }
    }

    @Component
    public static class InterceptAspectTestInterceptor implements Interceptor {
        @Override
        public boolean supports(
            Class<?> aClass
        ) {
            return Object.class.isAssignableFrom(aClass);
        }

        @Override
        public void intercept(
            Object payload
        ) {
        }
    }
}