package dev.shermende.support.spring.aop.profiling;

import dev.shermende.support.spring.aop.profiling.annotation.Profiling;
import dev.shermende.support.spring.jmx.JmxControl;
import dev.shermende.support.spring.jmx.impl.ToggleJmxControlImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.Aspects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ProfilingAspectTest.ProfilingAspectTestConfiguration.class})
public class ProfilingAspectTest {

    @SpyBean
    private JmxControl jmxControl;

    @Autowired
    private ProfilingAspectTest.ProfilingAspectTestComponent component;

    @Test
    public void profiling() throws Throwable {
        component.convert(new Object());
        then(jmxControl).should(times(1)).isEnabled();
    }

    @ComponentScan
    @EnableAspectJAutoProxy(proxyTargetClass = true)
    public static class ProfilingAspectTestConfiguration {
        @Bean
        public JmxControl jmxControl() {
            return new ToggleJmxControlImpl(true);
        }

        @Bean
        public ProfilingAspect interceptAspect() {
            return Aspects.aspectOf(ProfilingAspect.class);
        }
    }

    @Slf4j
    @Component
    public static class ProfilingAspectTestComponent {
        @Profiling
        public Object convert(
            Object payload
        ) {
            return payload;
        }
    }
}