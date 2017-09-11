package org.springframework.boot.actuate.metrics.export.prometheus;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

/**
 * @author Jon Schneider
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "metrics.useGlobalRegistry=false",
    "endpoints.prometheus.web.enabled=true"
})
public class PrometheusScrapeEndpointTest {
    @Autowired
    TestRestTemplate loopback;

    @Test
    public void scrapeHasContentTypeText004() {
        ResponseEntity<String> response = loopback.getForEntity("/application/prometheus", String.class);
        assertThat(response)
            .satisfies(r -> assertThat(r.getStatusCode().value()).isEqualTo(200))
            .satisfies(r -> assertThat(r.getHeaders().get(CONTENT_TYPE))
                .hasOnlyOneElementSatisfying(type -> assertThat(type).contains("0.0.4")));
    }

    @SpringBootApplication(scanBasePackages = "isolated")
    static class MetricsApp {
        @Bean
        public CollectorRegistry promRegistry() {
            return new CollectorRegistry(true);
        }

        @Bean
        public MeterRegistry registry(CollectorRegistry registry) {
            return new PrometheusMeterRegistry(k -> null, registry, Clock.SYSTEM);
        }
    }
}
