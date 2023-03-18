package ru.jug.pointofentry.springcloudgateway.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.stereotype.Component;
import ru.jug.pointofentry.springcloudgateway.configuration.ClientVersionsConfiguration;

import java.net.URI;
import java.util.Optional;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_PREDICATE_MATCHED_PATH_ROUTE_ID_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomUrlGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final static String X_CLIENT_VERSION_HEADER = "x-client-version";
    private final static String X_SERVICE_VERSION_HEADER = "x-service-version";

    private final ClientVersionsConfiguration versionsConfiguration;

    @Override
    public GatewayFilter apply(Object config) {

        return (exchange, chain) -> {

            var clientVersion = Optional.ofNullable(exchange.getRequest().getHeaders().get(X_CLIENT_VERSION_HEADER))
                    .map(x -> x.get(0))
                    .orElse("default");
            var proxiedServiceName = exchange.getAttributes().get(GATEWAY_PREDICATE_MATCHED_PATH_ROUTE_ID_ATTR).toString();
            var targetServiceVersionOrPort = versionsConfiguration.getServiceVersion(clientVersion, proxiedServiceName);

            var currentRoute = (Route) exchange.getAttributes().get(GATEWAY_ROUTE_ATTR);
            var currentRouteUri = currentRoute.getUri().toString();

            log.debug("Old URI: {}", currentRouteUri);

            var newRouteUri = URI.create(currentRouteUri
                    .replaceAll("version", targetServiceVersionOrPort));

            log.debug("New URI: {}", newRouteUri);

            var newRoute = new Route.AsyncBuilder()
                    .id(currentRoute.getId())
                    .uri(newRouteUri)
                    .asyncPredicate(currentRoute.getPredicate())
                    .filters(currentRoute.getFilters())
                    .build();

            exchange.getAttributes().put(GATEWAY_ROUTE_ATTR, newRoute);
            exchange.getResponse().getHeaders().add(X_SERVICE_VERSION_HEADER, "v" + targetServiceVersionOrPort.substring(3));

            return chain.filter(exchange);
        };
    }
}
