package jwhs.cheftoo.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jwhs.cheftoo.auth.dto.MemberConsentRequestDto;
import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.auth.enums.KakaoInfo;
import jwhs.cheftoo.auth.exception.SignUpException;
import jwhs.cheftoo.auth.service.KakaoService;
import jwhs.cheftoo.auth.service.MemberConsentService;
import jwhs.cheftoo.auth.service.MemberService;
import jwhs.cheftoo.util.JwtUtil;
import jwhs.cheftoo.util.port.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.http.HttpStatusCode;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberConsentService memberConsentService;
    private final KakaoService kakaoService;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    // 약관 동의 완료 후 회원가입 처리
    @PostMapping("/terms/agree")
    public ResponseEntity<?> agreeTerms(
            @SessionAttribute(name = "KAKAO_ID", required = true) String kakaoId,
            @SessionAttribute(name = "KAKAO_ACCESS_TOKEN", required = true) String kakaoAccessToken,
            @RequestBody MemberConsentRequestDto dto,
            HttpServletRequest request,
            HttpServletResponse response
            ) throws IOException {

        if (kakaoId == null) {
            // 세션 만료/분실 -> 다시 로그인
            return ResponseEntity.status(HttpStatusCode.UNAUTHORIZED).build();
        }

        try {
            // 회원가입 처리
            Member newMember = kakaoService.registerNewUserWithKakao(dto,kakaoId);
            UUID memberId = newMember.getMemberId();

            // 동의항목 저장
            memberConsentService.saveMemberConsent(dto, memberId);

            // 카카오 액세스토큰 redis 저장
            redisUtil.set(KakaoInfo.KAKAO_ACCESS_TOKEN_KEY.getAccessTokenKey() + memberId, kakaoAccessToken);

            // 리프레시 토큰 발급
            jwtUtil.addRefreshTokenToCookie(memberId, response, jwtUtil.generateRefreshToken(memberId));

            // 액세스 토큰 발급
            String accessToken = jwtUtil.generateAccessToken(memberId);

            return ResponseEntity.ok(Map.of("accessToken", accessToken));
        } catch (Exception e) {
            throw new SignUpException("회원가입 중 문제가 발생하였습니다.");
        }
    }

    // 닉네임 설정
    @PutMapping("/nickname")
    public ResponseEntity<?> setNickName(
            @RequestParam("nickname") String nickname,
            HttpServletRequest request
    ) {
        String token = jwtUtil.getAccessTokenFromRequest(request);
        UUID memberId = jwtUtil.getMemberIdFromToken(token);

        memberService.updateNickname(memberId, nickname);

        return ResponseEntity.noContent().build(); // HttpsStatusCode = 204

    }

    // 멤버ID를 통해 닉네임 조회
    @GetMapping("/nickname")
    public ResponseEntity<?> getNickNameByMember(HttpServletRequest request) {
        String token = jwtUtil.getAccessTokenFromRequest(request);
        UUID memberId = jwtUtil.getMemberIdFromToken(token);

        return ResponseEntity.status(HttpStatus.OK).body(memberService.findNickNameByMemberId(memberId));

    }


    @GetMapping("/nickname/check/{nickname}")
    public ResponseEntity<Boolean> nicknameCheck(
            @PathVariable("nickname") String nickname
    ) {
        boolean exists = memberService.checkNickname(nickname);
        return ResponseEntity.ok(exists);
    }


    // 회원탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMember(
            HttpServletRequest request
    ) {
        String accessToken = jwtUtil.getAccessTokenFromRequest(request);
        UUID memberId = jwtUtil.getMemberIdFromToken(accessToken);

        try {
            // 카카오에서 토큰 무효화 처리
            kakaoService.unlinkWithKakao(memberId);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        memberService.deleteMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/me")
    public String getMemberInfo(
            HttpServletRequest request
    ) {
        String accessToken = jwtUtil.getAccessTokenFromRequest(request);
        UUID memberId = jwtUtil.getMemberIdFromToken(accessToken);
        return memberId.toString();
    }
}
