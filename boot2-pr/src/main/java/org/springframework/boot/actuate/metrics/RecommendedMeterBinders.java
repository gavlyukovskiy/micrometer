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
package org.springframework.boot.actuate.metrics;

import io.micrometer.core.instrument.binder.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.LogbackMetrics;
import io.micrometer.core.instrument.binder.UptimeMetrics;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @since 2.0.0
 * @author Jon Schneider
 */
@Configuration
class RecommendedMeterBinders {
    @Bean
    @ConditionalOnMissingBean(JvmMemoryMetrics.class)
    JvmMemoryMetrics jvmMemoryMetrics() {
        return new JvmMemoryMetrics();
    }

    @Bean
    @ConditionalOnMissingBean(LogbackMetrics.class)
    @ConditionalOnClass(name = "ch.qos.logback.classic.Logger")
    LogbackMetrics logbackMetrics() {
        return new LogbackMetrics();
    }

    @Bean
    @ConditionalOnMissingBean(UptimeMetrics.class)
    UptimeMetrics uptimeMetrics() {
        return new UptimeMetrics();
    }
}