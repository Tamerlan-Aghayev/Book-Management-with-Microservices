package az.express.gateway.client;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "SecurityClient", url = "http://localhost:8086/auth")
public interface SecurityClient {
    @GetMapping("/validate")
    public Response validate(@RequestParam("token") String token);
    @GetMapping("/extract")
    public List<String> extract(@RequestParam("token") String token);
}
