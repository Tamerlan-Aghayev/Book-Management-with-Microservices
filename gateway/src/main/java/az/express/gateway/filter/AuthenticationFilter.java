package az.express.gateway.filter;

import az.express.gateway.client.SecurityClient;
import feign.Response;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator routeValidator;
    private final SecurityClient securityClient;
    public AuthenticationFilter(RouteValidator routeValidator, SecurityClient securityClient) {
        super(Config.class);
        this.routeValidator=routeValidator;
        this.securityClient=securityClient;

    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain)->{
            if(routeValidator.isSecured.test(exchange.getRequest())){
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                    throw new RuntimeException("missing authorization header");
                String authheader=exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if(authheader!=null && authheader.startsWith("Bearer")){
                    authheader = authheader.substring(7);

                }
                Response response= securityClient.validate(authheader);
                if(response.status()!=200){
                    throw new RuntimeException("salam");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config{}
}
