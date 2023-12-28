package az.express.gateway.filter;

import az.express.gateway.client.SecurityClient;
import feign.Response;
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




    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (routeValidator.isSecured.test(exchange.getRequest())) {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("Missing authorization header");
            }

            Response response = securityClient.checkToken((exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0),
                    exchange.getRequest().getURI().getPath()
            );


            System.out.println(response.status());
            if(response.status()!=200) throw new RuntimeException("error occurred my guy over there");
        }
        return chain.filter(exchange);

    }
}
