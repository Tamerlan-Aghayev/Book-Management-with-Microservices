package az.express.gateway.client;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@FeignClient(name = "SecurityClient", url = "http://localhost:8086/auth")
public interface SecurityClient {
    @GetMapping("/validate")
    public Response validate(@RequestParam("token") String token);
    @PostMapping("/check-token")
    public Response checkToken(@RequestParam("header")String header, @RequestParam("url") String url);
}
