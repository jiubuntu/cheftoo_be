package jwhs.cheftoo.auth.enums;

public enum KakaoInfo {
    KAKAO_ACCESS_TOKEN_KEY("kakao:accessToken:member:");

    private final String accessTokenKey;

    KakaoInfo(String accessTokenKey) {
        this.accessTokenKey = accessTokenKey;
    }

    public String getAccessTokenKey() {
        return accessTokenKey;
    }
}
