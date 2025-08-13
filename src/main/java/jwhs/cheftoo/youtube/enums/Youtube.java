package jwhs.cheftoo.youtube.enums;

public enum Youtube {
    YOUTUBE_CACHE_KEY("youtube:home:videos"),
    YOUTUBE_VERSION_KEY("youtube:home:version");

    private final String cacheKey;
    Youtube(String cacheKey) {
        this.cacheKey = cacheKey;
    }
    public String getCacheKey() {
        return cacheKey;
    }
}
