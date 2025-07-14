package jwhs.cheftoo.image.enums;

import java.time.Duration;

public enum S3ImageType {


    RECIPE_GET_DURATION(Duration.ofMinutes(15)),
    RECIPE_PUT_DURATION(Duration.ofMinutes(5)),
    COOKING_ORDER_GET_DURATION(Duration.ofMinutes(15)),
    COOKING_ORDER_PUT_DURATION(Duration.ofMinutes(5));

    private final Duration duration;

    S3ImageType(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }
}
