package az.express.gateway.config;

import az.express.gateway.filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {


    private final AuthenticationFilter filter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("book-route", r -> r.path("/book/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://book-service"))

                .route("category-route", r -> r.path("/category/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://category-service"))

                .route("security-route", r -> r.path("/auth/**")
                        .uri("lb://security-service")).build();
    }
}