//package az.express.gateway.config;
//
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//
//@org.springframework.context.annotation.Configuration
//public class Configuration {
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("book-route", r -> r
//                        .path("/book/**")
//                        .uri("localhost:8085/"))
//                .route("category-route", r -> r
//                        .path("/category/**")
//                        .uri("lb://category-service"))
//                .build();
//    }
//}
