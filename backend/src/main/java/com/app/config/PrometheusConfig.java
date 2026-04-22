package com.app.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class PrometheusConfig {

    private final MeterRegistry registry;

    public PrometheusConfig(MeterRegistry registry) {
        this.registry = registry;
    }

    @PostConstruct
    public void init() {
        registry.counter("app.start").increment();
    }
}