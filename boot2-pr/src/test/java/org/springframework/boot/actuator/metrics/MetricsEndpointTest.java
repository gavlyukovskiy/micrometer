package org.springframework.boot.actuator.metrics;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * @author Jon Schneider
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "metrics.useGlobalRegistry=false",
    "endpoints.metrics.web.enabled=true"
})
public class MetricsEndpointTest {
    @Autowired
    TestRestTemplate loopback;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void listNames() throws IOException {
        List<String> names = mapper.readValue(loopback.getForObject("/application/metrics", String.class),
            new TypeReference<List<String>>() {});

        assertThat(names).contains("jvm.memory.used");
    }

    @Test
    public void selectByName() throws IOException {
        Map<String, Collection<MetricsEndpoint.MeasurementSample>> measurements = mapper.readValue(loopback.getForObject("/application/metrics/jvm.memory.used", String.class),
            new TypeReference<Map<String, Collection<MetricsEndpoint.MeasurementSample>>>() {});

        System.out.println(measurements);

        // one entry per tag combination
        assertThat(measurements).containsKeys(
            "jvm_memory_used.area.nonheap.id.Compressed_Class_Space",
            "jvm_memory_used.area.heap.id.PS_Old_Gen");
    }

    @SpringBootApplication(scanBasePackages = "isolated")
    static class MetricsApp {
        @Bean
        public MeterRegistry registry() {
            return new SimpleMeterRegistry();
        }
    }
}
