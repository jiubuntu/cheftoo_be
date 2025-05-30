package jwhs.cheftoo.config;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public HttpHeaders httpHeaders() {
        return new HttpHeaders();
    }
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }




}
