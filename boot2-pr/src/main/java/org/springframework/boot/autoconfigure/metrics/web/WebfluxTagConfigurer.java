package org.springframework.boot.autoconfigure.metrics.web;

import io.micrometer.core.instrument.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;

import static java.util.Arrays.asList;

/**
 * @since 2.0.0
 * @author Jon Schneider
 */
public class WebfluxTagConfigurer {
    /**
     * Supplies default tags to the WebFlux annotation-based server programming model.
     */
    Iterable<Tag> httpRequestTags(ServerWebExchange exchange, Throwable exception) {
        return asList(method(exchange), uri(exchange), exception(exception), status(exchange));
    }

    public Tag uri(ServerWebExchange exchange) {
        PathPattern pathPattern = exchange.getAttributeOrDefault(org.springframework.web.reactive.HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE, null);
        String rawUri = pathPattern.getPatternString();

        if (!StringUtils.hasText(rawUri)) {
            rawUri = "/";
        }
        return Tag.of("uri", rawUri.isEmpty() ? "root" : rawUri);
    }

    public Tag method(ServerWebExchange exchange) {
        return Tag.of("method", exchange.getRequest().getMethod().toString());
    }

    public Tag status(ServerWebExchange exchange) {
        HttpStatus status = exchange.getResponse().getStatusCode();
        if(status == null)
            status = HttpStatus.OK;
        return Tag.of("status", status.toString());
    }

    public Tag exception(Throwable exception) {
        if (exception != null) {
            return Tag.of("exception", exception.getClass().getSimpleName());
        }
        return Tag.of("exception", "None");
    }
}