package dev.shermende.support.spring.benchmark;

import dev.shermende.support.spring.aop.profiling.ProfilingAspect;
import dev.shermende.support.spring.aop.profiling.annotation.Profiling;
import dev.shermende.support.spring.jmx.JmxControl;
import dev.shermende.support.spring.jmx.impl.ToggleJmxControlImpl;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Threads(10)
@Warmup(iterations = 3)
@Measurement(iterations = 3)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ProfilingAspectDisabledBenchmark {

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
    @Configuration
    @EnableAspectJAutoProxy(proxyTargetClass = true)
    public static class ProfilingAspectTestConfiguration {
        @Bean
        public JmxControl jmxControl() {
            return new ToggleJmxControlImpl(false);
        }

        @Bean
        public ProfilingAspect profilingAspect() {
            return new ProfilingAspect();
        }
    }

    @Component
    public static class ProfilingAspectTestComponent {
        @Profiling
        public void action() {
        }
    }

}