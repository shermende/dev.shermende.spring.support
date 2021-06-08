package dev.shermende.support.spring.benchmark;

import dev.shermende.support.spring.aop.intercept.InterceptAspect;
import dev.shermende.support.spring.aop.intercept.Interceptor;
import dev.shermende.support.spring.aop.intercept.annotation.Intercept;
import dev.shermende.support.spring.aop.intercept.annotation.InterceptArgument;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.Aspects;
import org.openjdk.jmh.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Threads(10)
@Warmup(iterations = 3)
@Measurement(iterations = 3)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class InterceptAspectBenchmark {
    private static final Logger log = LoggerFactory.getLogger(InterceptAspectBenchmark.class);

    private ConfigurableApplicationContext context;

    @Setup(Level.Trial)
    public synchronized void benchmarkSetup() {
        context = SpringApplication.run(InterceptAspectBenchmarkConfiguration.class);
    }

    @Benchmark
    public void benchmark() {
        context.getBean(InterceptAspectBenchmarkComponent.class).convert(new Object());
    }

    @ComponentScan
    @EnableAspectJAutoProxy(proxyTargetClass = true)
    public static class InterceptAspectBenchmarkConfiguration {
        @Bean
        public InterceptAspect interceptAspect() {
            return Aspects.aspectOf(InterceptAspect.class);
        }
    }

    @Slf4j
    @Component
    public static class InterceptAspectBenchmarkComponent {
        @Intercept
        public Object convert(
            @InterceptArgument(InterceptAspectBenchmark.InterceptAspectBenchmarkInterceptor.class) Object payload
        ) {
            return payload;
        }
    }

    @Slf4j
    @Component
    public static class InterceptAspectBenchmarkInterceptor implements Interceptor {
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
            log.debug("interceptor working... {}", payload);
        }
    }

}