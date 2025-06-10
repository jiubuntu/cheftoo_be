package jwhs.cheftoo.scrap.controller;

import jakarta.servlet.http.HttpServletRequest;
import jwhs.cheftoo.scrap.dto.ScrapResponseDto;
import jwhs.cheftoo.scrap.service.ScrapService;
import jwhs.cheftoo.util.JwtUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class ScrapController {

    private ScrapService scrapService;
    private JwtUtil jwtUtil;

    public ScrapController(ScrapService scrapService, JwtUtil jwtUtil) {
        this.scrapService = scrapService;
        this.jwtUtil = jwtUtil;
    }

//    @GetMapping("member/scrap")
//    public List<ScrapResponseDto> getScrapByMember(
//            HttpServletRequest request
//    ) {
//
//        String token = jwtUtil.getTokenFromRequest(request);
//        UUID memberId = jwtUtil.getMemberIdFromToken(token);
//
//        return scrapService.findAllByMemberId(memberId);
//    }
}
