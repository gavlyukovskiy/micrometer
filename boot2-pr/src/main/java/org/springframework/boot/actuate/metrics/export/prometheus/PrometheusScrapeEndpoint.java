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
package org.springframework.boot.actuate.metrics.export.prometheus;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import org.springframework.boot.endpoint.Endpoint;
import org.springframework.boot.endpoint.EndpointExposure;
import org.springframework.boot.endpoint.ReadOperation;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @since 2.0.0
 * @author Jon Schneider
 *
 * Spring Boot Actuator endpoint that outputs Prometheus metrics in a format that
 * can be scraped by the Prometheus server
 */
@Endpoint(id = "prometheus", exposure = EndpointExposure.WEB)
public class PrometheusScrapeEndpoint {
    private final CollectorRegistry collectorRegistry;

    PrometheusScrapeEndpoint(CollectorRegistry collectorRegistry) {
        this.collectorRegistry = collectorRegistry;
    }

    @ReadOperation(produces = TextFormat.CONTENT_TYPE_004)
    public String scrape() {
        try {
            Writer writer = new StringWriter();
            TextFormat.write004(writer, collectorRegistry.metricFamilySamples());
            return writer.toString();
        } catch (IOException e) {
            // This actually never happens since StringWriter::write() doesn't throw any IOException
            throw new RuntimeException("Writing metrics failed", e);
        }
    }
}
