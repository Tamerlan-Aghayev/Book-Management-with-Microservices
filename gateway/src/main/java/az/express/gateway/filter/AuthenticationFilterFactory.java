package az.express.gateway.filter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilterFactory implements GatewayFilterFactory<AuthenticationFilterFactory.Config> {

    private final AuthenticationFilter authenticationFilter;

    public AuthenticationFilterFactory(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return authenticationFilter;
    }

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }

    public static class Config {
    }
}
