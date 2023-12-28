package az.express.gateway.filter;

import az.express.gateway.client.SecurityClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
@RefreshScope
@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilter {

    private final RouteValidator routeValidator;

    private final SecurityClient securityClient;




    private boolean hasUserRole(List<String> roles, String targetRole) {

        return roles.contains(targetRole);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (routeValidator.isSecured.test(exchange.getRequest())) {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("Missing authorization header");
            }

            String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);

            if (authHeader != null && authHeader.startsWith("Bearer")) {
                String token = authHeader.substring(7);
                List<String> roles = securityClient.extract(token);

                try {
                    if (exchange.getRequest().getURI().getPath().startsWith("/book/") && hasUserRole(roles, "ROLE_USER")) {
                        return chain.filter(exchange);  // Allow access for users to /book/**
                    } else if (exchange.getRequest().getURI().getPath().startsWith("/category/") && hasUserRole(roles, "ROLE_ADMIN")) {
                        return chain.filter(exchange);  // Allow access for admins to /category/**
                    } else {
                        throw new RuntimeException("Unauthorized access to the requested path");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid access...!");
                    e.printStackTrace();
                }
            }
        }

        return chain.filter(exchange);
    }

}
