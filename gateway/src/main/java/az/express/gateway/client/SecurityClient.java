package az.express.gateway.client;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "SecurityClient", url = "http://SECURITY-SERVICE")
public interface SecurityClient {
    @GetMapping("/validate")
    public Response validate(@RequestParam("token") String token);
}
