package dev.shermende.support.spring.benchmark;

import dev.shermende.support.spring.aop.logging.LoggingAspect;
import dev.shermende.support.spring.aop.logging.annotation.Logging;
import dev.shermende.support.spring.jmx.JmxControl;
import dev.shermende.support.spring.jmx.impl.ToggleJmxControlImpl;
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
public class LoggingAspectBenchmark {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspectBenchmark.class);

    private ConfigurableApplicationContext context;

    @Setup(Level.Trial)
    public synchronized void benchmarkSetup() {
        context = SpringApplication.run(LoggingAspectBenchmarkConfiguration.class);
    }

    @Benchmark
    public void benchmark() {
        context.getBean(LoggingAspectBenchmarkComponent.class).action();
    }

    @ComponentScan
    @EnableAspectJAutoProxy(proxyTargetClass = true)
    public static class LoggingAspectBenchmarkConfiguration {
        @Bean
        public JmxControl jmxControl() {
            return new ToggleJmxControlImpl(true);
        }

        @Bean
        public LoggingAspect loggingAspect() {
            return Aspects.aspectOf(LoggingAspect.class);
        }
    }

    @Component
    public static class LoggingAspectBenchmarkComponent {
        @Logging
        void action() {
        }
    }

}