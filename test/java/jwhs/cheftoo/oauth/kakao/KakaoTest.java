package jwhs.cheftoo.oauth.kakao;


import jwhs.cheftoo.service.KakaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KakaoTest {
    @Value("${kakao.test.ingacode}")
    private String testIngaCode;

    @Autowired
    private KakaoService kakaoService;

    @Test
    public void getTokenTest() {
        kakaoService.getACcessToken(testIngaCode);

    }
}
