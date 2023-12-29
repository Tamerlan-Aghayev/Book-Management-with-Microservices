package az.express.gateway.filter;

import az.express.gateway.client.SecurityClient;
import az.express.gateway.exception.ErrorResponse;
import az.express.gateway.exception.MissingAuthorizationHeaderException;
import az.express.gateway.exception.TokenValidationException;
import az.express.gateway.util.ErrorResponseConverter;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Component
@RefreshScope
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter implements GatewayFilter {

    private final RouteValidator routeValidator;
    private final SecurityClient securityClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (routeValidator.isSecured.test(exchange.getRequest())) {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                log.warn("Missing authorization header");
                throw new MissingAuthorizationHeaderException("Missing authorization header");
            }

            String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            log.info("Checking token for authorization header: {}", authorizationHeader);

            Response response = securityClient.checkToken(authorizationHeader, exchange.getRequest().getURI().getPath());

            if (response.status() != 200) {
                String errorMessage = ErrorResponseConverter.getErrorMessage(response);
                log.error("Token validation failed. Error: {}", errorMessage);
                throw new TokenValidationException(errorMessage);
            }

            log.info("Token validation successful");
        }

        return chain.filter(exchange);
    }
}
