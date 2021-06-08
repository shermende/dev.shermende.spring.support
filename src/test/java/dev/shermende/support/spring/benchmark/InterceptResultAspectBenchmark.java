package dev.shermende.support.spring.benchmark;

import dev.shermende.support.spring.aop.intercept.InterceptResultAspect;
import dev.shermende.support.spring.aop.intercept.Interceptor;
import dev.shermende.support.spring.aop.intercept.annotation.InterceptResult;
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
public class InterceptResultAspectBenchmark {

    private static final Logger log = LoggerFactory.getLogger(InterceptResultAspectBenchmark.class);

    private ConfigurableApplicationContext context;

    @Setup(Level.Trial)
    public synchronized void benchmarkSetup() {
        context = SpringApplication.run(InterceptResultAspectBenchmarkConfiguration.class);
    }

    @Benchmark
    public void benchmark() {
        context.getBean(InterceptResultAspectBenchmarkComponent.class).convert(new Object());
    }

    @ComponentScan
    @EnableAspectJAutoProxy(proxyTargetClass = true)
    public static class InterceptResultAspectBenchmarkConfiguration {
        @Bean
        public InterceptResultAspect interceptResultAspect() {
            return Aspects.aspectOf(InterceptResultAspect.class);
        }
    }

    @Slf4j
    @Component
    public static class InterceptResultAspectBenchmarkComponent {
        @InterceptResult(InterceptResultAspectBenchmark.InterceptResultAspectBenchmarkInterceptor.class)
        public Object convert(
            Object payload
        ) {
            return payload;
        }
    }

    @Slf4j
    @Component
    public static class InterceptResultAspectBenchmarkInterceptor implements Interceptor {
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