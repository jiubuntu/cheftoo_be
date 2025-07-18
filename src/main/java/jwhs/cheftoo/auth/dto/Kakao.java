package jwhs.cheftoo.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;

// 카카오 로그인 관련 DTO
public class Kakao {
    // 카카오 엑세스 토큰 DTO
    @Data
    public static class KakaoAccessToken {
        private String token_type;
        private String access_token;
        private int expires_in;
        private String refresh_token;
        private int refresh_token_expires_in;;
        private String scope;

    }
    // 카카오 유저정보 DTO
    @Data
    public static class KakaoUserInfo {
        private Long id;
        private String connected_at;
        private Properties properties;
        private KakaoAccount kakao_account;
        @Getter
        public static class Properties {
            private String nickname;
        }

        @Getter
        public static class KakaoAccount {
            private boolean profile_nickname_needs_agreement;
            private Profile profile;

            @Getter
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Profile {
                private String nickname;
                private boolean is_default_nickname;
            }
        }
    }
}


