package jwhs.cheftoo.auth.service;

import jakarta.transaction.Transactional;
import jwhs.cheftoo.auth.dto.MemberConsentRequestDto;
import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.auth.entity.MemberConsent;
import jwhs.cheftoo.auth.enums.ConsentType;
import jwhs.cheftoo.auth.exception.MemberNotFoundException;
import jwhs.cheftoo.auth.repository.MemberConsentRepository;
import jwhs.cheftoo.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberConsentService {
    private final MemberConsentRepository memberConsentRepository;
    private final MemberRepository memberRepository;

    @Value("${policy.version.privacy}")
    private String privacyVersion;

    @Value("${policy.version.tos}")
    private String tosVersion;

    @Value("${policy.version.sms}")
    private String smsVersion;

    @Transactional
    public void saveMemberConsent(MemberConsentRequestDto dto, UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("해당 유저가 존재하지 않습니다."));

        boolean privacy = dto.isPrivacy();
        boolean tos = dto.isTos();
        boolean smsMarketing = dto.isSmsMarketing();

        upsertConsent(member, ConsentType.PRIVACY, privacy,
                privacyVersion);
        upsertConsent(member, ConsentType.TOS, tos,
                tosVersion);

        upsertConsent(member, ConsentType.SMS_MARKETING, smsMarketing,
                smsVersion);


    }


    private void upsertConsent(Member member,
                               ConsentType type,
                               boolean agreed,
                               String version
) {

        memberConsentRepository.clearLatest(member.getMemberId(), type);

        MemberConsent saved = MemberConsent.builder()
                .member(member)
                .consentType(type)
                .version(version)
                .agreed(agreed)
                .agreedAt(LocalDateTime.now())
                .isLatest(true)
                .build();

        memberConsentRepository.save(saved);
    }
}
