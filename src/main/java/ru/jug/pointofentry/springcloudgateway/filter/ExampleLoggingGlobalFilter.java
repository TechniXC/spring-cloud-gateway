package ru.jug.pointofentry.springcloudgateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ExampleLoggingGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.info("{} request to {} has been received", exchange.getRequest().getMethod(), exchange.getRequest().getURI());

        return chain.filter(exchange)
                .contextCapture()
                .then(Mono.fromRunnable(() -> {
                    log.info("Response with status {} has been provided: {}", exchange.getResponse().getStatusCode(), exchange.getResponse());
                }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
