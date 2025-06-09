package jwhs.cheftoo.scrap.service;

import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.auth.exception.MemberNotFoundException;
import jwhs.cheftoo.auth.repository.MemberRepository;
import jwhs.cheftoo.scrap.dto.ScrapResponseDto;
import jwhs.cheftoo.scrap.repository.ScrapRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ScrapService {

    private ScrapRepository scrapRepository;
    private MemberRepository memberRepository;

    public ScrapService(ScrapRepository scrapRepository, MemberRepository memberRepository) {
        this.scrapRepository = scrapRepository;
        this.memberRepository = memberRepository;
    }

    public List<ScrapResponseDto> findAllByMemberId(UUID memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new MemberNotFoundException("유저를 찾을 수 없습니다.");
        });

        
    }
}
