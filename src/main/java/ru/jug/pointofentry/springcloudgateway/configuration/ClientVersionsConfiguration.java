package ru.jug.pointofentry.springcloudgateway.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "gateway")
public class ClientVersionsConfiguration {

    private Map<String, Map<String, String>> versions;

    public String getServiceVersion(String mobileVersion, String serviceName) {
        log.debug("Version request for: service name: {}, mobile version: {}", serviceName, mobileVersion);

        return Optional.ofNullable(versions)
                .map(x -> x.get('[' + mobileVersion + ']'))
                .map(y -> y.get(serviceName))
                .orElseThrow(() -> new IllegalArgumentException("Client version is not supported yet"));
    }

}
