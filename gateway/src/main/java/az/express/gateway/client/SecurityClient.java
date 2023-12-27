package az.express.gateway.client;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="SecurityClient", url="http://localhost:8086")
public interface SecurityClient {

    @GetMapping("/validate")
    Response validate(@RequestParam("token") String token);
}
