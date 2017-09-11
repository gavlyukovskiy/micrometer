/**
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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