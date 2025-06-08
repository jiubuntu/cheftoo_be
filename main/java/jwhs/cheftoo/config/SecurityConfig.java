package jwhs.cheftoo.config;

import jwhs.cheftoo.security.JwtAuthenticationFilter;
import jwhs.cheftoo.util.JwtUtil;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;


    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/oauth/kakao/callback").permitAll()
                    .requestMatchers("/mypage").authenticated()
                    .requestMatchers(HttpMethod.POST,"/recipe/*/comment").authenticated()
                    .requestMatchers(HttpMethod.DELETE,"/recipe/comment/*").authenticated()
                    .requestMatchers(HttpMethod.POST,"/recipes").authenticated()
                    .anyRequest().permitAll()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sess -> sess.disable());
        return http.build();
    }


    //cors 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:3000")); //react 서버 허용
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); //
        config.setAllowedHeaders(List.of("*")); //

        source.registerCorsConfiguration("/**", config);
        return source;
    }



}
