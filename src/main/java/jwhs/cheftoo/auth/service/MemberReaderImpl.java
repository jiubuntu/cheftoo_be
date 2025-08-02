package jwhs.cheftoo.auth.service;

import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.auth.port.MemberReader;
import jwhs.cheftoo.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class MemberReaderImpl implements MemberReader {

    private final MemberRepository memberRepository;
    @Override
    public Member getById(UUID memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new NoSuchElementException("member를 찾을 수 없습니다,");
        });
        return member;
    }
}
