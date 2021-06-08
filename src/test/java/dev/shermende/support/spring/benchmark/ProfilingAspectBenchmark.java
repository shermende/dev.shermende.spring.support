package dev.shermende.support.spring.benchmark;

import dev.shermende.support.spring.aop.profiling.ProfilingAspect;
import dev.shermende.support.spring.aop.profiling.annotation.Profiling;
import dev.shermende.support.spring.jmx.JmxControl;
import dev.shermende.support.spring.jmx.impl.ToggleJmxControlImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.Aspects;
import org.openjdk.jmh.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
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
public class ProfilingAspectBenchmark {

    private ConfigurableApplicationContext context;

    @Setup(Level.Trial)
    public synchronized void benchmarkSetup() {
        context = SpringApplication.run(ProfilingAspectTestConfiguration.class);
    }

    @Benchmark
    public void benchmark() {
        context.getBean(ProfilingAspectTestComponent.class).action();
    }

    @ComponentScan
    @EnableAspectJAutoProxy(proxyTargetClass = true)
    public static class ProfilingAspectTestConfiguration {
        @Bean
        public JmxControl jmxControl() {
            return new ToggleJmxControlImpl(true);
        }
        @Bean
        public ProfilingAspect profilingAspect() {
            return Aspects.aspectOf(ProfilingAspect.class);
        }
    }

    @Component
    public static class ProfilingAspectTestComponent {
        @Profiling
        void action() {
        }
    }

}