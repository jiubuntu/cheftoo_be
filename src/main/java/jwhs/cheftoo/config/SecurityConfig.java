package jwhs.cheftoo.config;

import jwhs.cheftoo.security.JwtAuthenticationFilter;
import jwhs.cheftoo.util.JwtUtil;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
                // CSP 설정
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("script-src 'self';")
                        )
                )
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("api/oauth/kakao/callback").permitAll()
                        .requestMatchers("api/member/terms/agree").permitAll()
                        .requestMatchers("api/mypage").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"api/member/me").authenticated()
                        .requestMatchers(HttpMethod.GET,"api/member/me").authenticated()
                        .requestMatchers("api/auth/nickname").authenticated()
                        .requestMatchers("api/auth/check").authenticated()
                        .requestMatchers("api/auth/logout").authenticated()
                        .requestMatchers(HttpMethod.POST,"api/auth/member/consent").authenticated()
                        .requestMatchers(HttpMethod.POST,"api/recipe/*/comment").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"api/recipe/comment/*").authenticated()
                        .requestMatchers(HttpMethod.GET,"api/member/comment").authenticated()
                        .requestMatchers(HttpMethod.PUT,"api/comment").authenticated()
                        .requestMatchers(HttpMethod.POST,"api/recipe").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"api/recipe").authenticated()
                        .requestMatchers(HttpMethod.GET,"api/recipe/member").authenticated()
                        .requestMatchers("api/member/scrap/**").authenticated()
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
        config.setAllowedOrigins(List.of("http://3.37.143.36:9000")); 
        config.setAllowedOrigins(List.of("https://cheftoo.co.kr"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); //
        config.setAllowedHeaders(List.of("*")); //

        source.registerCorsConfiguration("/**", config);
        return source;
    }



}
