package org.springframework.boot.actuate.metrics.web;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.metrics.MetricsConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures instrumentation of Spring Webflux MVC annotation-based programming model request mappings.
 *
 * @since 2.0.0
 * @author Jon Schneider
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Configuration
public class MetricsWebfluxRequestConfiguration {
    @Bean
    @ConditionalOnMissingBean(WebfluxTagConfigurer.class)
    WebfluxTagConfigurer webfluxTagConfigurer() {
        return new WebfluxTagConfigurer();
    }

    @Bean
    public MetricsWebFilter webfluxMetrics(MeterRegistry registry,
                                           WebfluxTagConfigurer tagConfigurer,
                                           MetricsConfigurationProperties properties) {
        return new MetricsWebFilter(registry, tagConfigurer, properties.getWeb().getServerRequestsName());
    }
}