package jwhs.cheftoo.scrap.service;

import jakarta.transaction.Transactional;
import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.auth.exception.MemberNotFoundException;
import jwhs.cheftoo.auth.repository.MemberRepository;
import jwhs.cheftoo.scrap.dto.ScrapInsertRequestDto;
import jwhs.cheftoo.scrap.dto.ScrapRequestDto;
import jwhs.cheftoo.scrap.dto.ScrapResponseDto;
import jwhs.cheftoo.scrap.entity.Scrap;
import jwhs.cheftoo.scrap.exception.ScrapNotFoundException;
import jwhs.cheftoo.scrap.repository.ScrapRepository;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        return scrapRepository.findAllByMember(member).stream()
                .map( scrap -> {
                    return ScrapResponseDto.builder()
                            .scrapId(scrap.getScrapId())
                            .scrapName(scrap.getScrapName())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ScrapResponseDto saveScrap(ScrapInsertRequestDto scrapRequestDto, UUID memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new MemberNotFoundException("유저를 찾을 수 없습니다.");
        });

        Scrap scrap = Scrap.builder()
                .scrapId(null)
                .scrapName(scrapRequestDto.getScrapName())
                .member(member)
                .build();

        Scrap savedScrap = scrapRepository.save(scrap);

        return ScrapResponseDto.fromEntity(savedScrap);
    }

    @Transactional
    public ScrapResponseDto updateScrap(ScrapRequestDto scrapRequestDto, UUID memberId, UUID scrapId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new MemberNotFoundException("유저를 찾을 수 없습니다.");
        });

        Scrap scrap = scrapRepository.findById(scrapId).orElseThrow(() -> {
            throw new ScrapNotFoundException("스크랩 폴더를 찾을 수 없습니다.");
        });

        scrap.setScrapName(scrapRequestDto.getScrapName());

        Scrap updatedScrap = scrapRepository.save(scrap);

        return ScrapResponseDto.fromEntity(updatedScrap);


    }

    @Transactional
    public void deleteScrap(UUID scrapId, UUID memberId) throws AccessDeniedException {

        Scrap existingScrap = scrapRepository.findById(scrapId).orElseThrow(() -> {
            throw new ScrapNotFoundException("스크랩 폴더를 찾을 수 없습니다.");
        });

        // 요청을 날린 유저와 스크랩을 만든 유저와 다른지 확인
        if (!memberId.equals(existingScrap.getMember().getMemberId())) {
            throw new AccessDeniedException("해당 스크랩 삭제 권한이 없습니다.");
        }

        scrapRepository.deleteById(scrapId);

    }

    @Transactional
    public void deleteAllByMember(Member member) {
        scrapRepository.deleteAllByMember(member);
    }
}
