package jwhs.cheftoo.auth.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberConsentRequestDto {
    @JsonProperty("privacy")
    private boolean privacy;

    @JsonProperty("tos")
    private boolean tos;

    @JsonProperty("smsMarketing")
    private boolean smsMarketing;

    @JsonProperty("nickName")
    private String nickName;
}
