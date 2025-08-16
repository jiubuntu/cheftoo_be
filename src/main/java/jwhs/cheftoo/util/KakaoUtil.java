package jwhs.cheftoo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jwhs.cheftoo.auth.dto.Kakao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
@Slf4j
@Component
public class KakaoUtil {
    @Value("${kakao.client.id}")
    private String KAKAO_CLIENT_ID;
    @Value("${kakao.client.secret}")
    private String KAKAO_CLIENT_SECRET;
    @Value("${kakao.redirect.url}")
    private String REDIRECT_URL;
    @Value("${kakao.access-token.url}")
    private String ACCESS_TOKEN_URL;

    // 인가코드로 카카오서버로부터 엑세스 토큰을 받아오는 함수
    public String getAccessToken(String ingacode) throws RuntimeException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", KAKAO_CLIENT_ID);
        params.add("redirect_url", REDIRECT_URL);
        params.add("code", ingacode);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                ACCESS_TOKEN_URL,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class);

        //json으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        Kakao.KakaoAccessToken kakaoAccessToken = null;

        try {
            kakaoAccessToken = objectMapper.readValue(response.getBody(), Kakao.KakaoAccessToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
        return kakaoAccessToken.getAccess_token();
    }



    // 엑세스 토큰으로 유저 정보를 가져오는 함수
    public Kakao.KakaoUserInfo getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                kakaoProfileRequest,
                String.class);
//        log.info("사용자 정보 가져오기 : " + response);

        ObjectMapper objectMapper = new ObjectMapper();
        Kakao.KakaoUserInfo userInfo = null;

        try {
            userInfo = objectMapper.readValue(response.getBody(), Kakao.KakaoUserInfo.class);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return userInfo;
    }

    public void logoutWithAccessToken(String accessToken) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> req =
                new HttpEntity<>(new LinkedMultiValueMap<>(), headers);

        ResponseEntity<String> resp =
                rt.postForEntity("https://kapi.kakao.com/v1/user/logout", req, String.class);

        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("카카오 logout 실패");
        }
    }

    public void unlinkWithAccessToken(String accessToken) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> req =
                new HttpEntity<>(new LinkedMultiValueMap<>(), headers);

        ResponseEntity<String> resp =
                rt.postForEntity("https://kapi.kakao.com/v1/user/unlink", req, String.class);

        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("카카오 unlink 실패");
        }
    }


}
