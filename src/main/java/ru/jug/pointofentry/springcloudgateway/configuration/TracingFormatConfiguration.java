package ru.jug.pointofentry.springcloudgateway.configuration;

import brave.Tracing;
import brave.TracingCustomizer;
import brave.handler.SpanHandler;
import brave.propagation.B3Propagation;
import brave.propagation.CurrentTraceContext;
import brave.propagation.Propagation;
import brave.sampler.Sampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

@Configuration
public class TracingFormatConfiguration {

    private static final String DEFAULT_APPLICATION_NAME = "application";

    @Bean
    public Tracing braveTracing(Environment environment, List<SpanHandler> spanHandlers,
                                List<TracingCustomizer> tracingCustomizers, CurrentTraceContext currentTraceContext,
                                Propagation.Factory propagationFactory, Sampler sampler) {
        String applicationName = environment.getProperty("spring.application.name", DEFAULT_APPLICATION_NAME);
        Tracing.Builder builder = Tracing.newBuilder()
                .currentTraceContext(currentTraceContext)
                .traceId128Bit(true)
                .supportsJoin(false)
                .propagationFactory(B3Propagation.newFactoryBuilder().injectFormat(B3Propagation.Format.MULTI).build())
                .sampler(sampler)
                .localServiceName(applicationName);
        spanHandlers.forEach(builder::addSpanHandler);
        for (TracingCustomizer tracingCustomizer : tracingCustomizers) {
            tracingCustomizer.customize(builder);
        }
        return builder.build();
    }

}
