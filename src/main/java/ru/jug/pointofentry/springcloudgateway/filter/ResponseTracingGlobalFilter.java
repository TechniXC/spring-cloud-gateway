package ru.jug.pointofentry.springcloudgateway.filter;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResponseTracingGlobalFilter implements GlobalFilter, Ordered {

    private final static String RESPONSE_ID_HEADER = "responseId";

    private final Tracer tracer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        Optional.ofNullable(tracer)
                .map(Tracer::currentSpan)
                .map(Span::context)
                .map(TraceContext::traceId)
                .ifPresent(traceId -> exchange.getResponse().getHeaders().add(RESPONSE_ID_HEADER, traceId));

//        log.info("Mdc is {}", MDC.getCopyOfContextMap());

        return chain.filter(exchange)
                .contextCapture();
    };

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
