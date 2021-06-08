package dev.shermende.support.spring.benchmark;

import dev.shermende.support.spring.aop.logging.LoggingAspect;
import dev.shermende.support.spring.aop.logging.annotation.Logging;
import dev.shermende.support.spring.jmx.JmxControl;
import dev.shermende.support.spring.jmx.impl.ToggleJmxControlImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.Aspects;
import org.openjdk.jmh.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Fork(1)
@Threads(10)
@Warmup(iterations = 3)
@Measurement(iterations = 3)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class LoggingAspectDisabledBenchmark {

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
    public static class LoggingAspectBenchmarkConfiguration {
        @Bean
        public JmxControl jmxControl() {
            return new ToggleJmxControlImpl(false);
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