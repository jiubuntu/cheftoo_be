package jwhs.cheftoo.scrap.controller;

import jakarta.servlet.http.HttpServletRequest;
import jwhs.cheftoo.scrap.dto.ScrapInsertRequestDto;
import jwhs.cheftoo.scrap.dto.ScrapRequestDto;
import jwhs.cheftoo.scrap.dto.ScrapResponseDto;
import jwhs.cheftoo.scrap.service.ScrapService;
import jwhs.cheftoo.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ScrapController {

    private ScrapService scrapService;
    private JwtUtil jwtUtil;

    public ScrapController(ScrapService scrapService, JwtUtil jwtUtil) {
        this.scrapService = scrapService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("member/scrap")
    public ResponseEntity<List<ScrapResponseDto>> getScrapByMember(
            HttpServletRequest request
    ) {

        String token = jwtUtil.getAccessTokenFromRequest(request);
        UUID memberId = jwtUtil.getMemberIdFromToken(token);

        return ResponseEntity.status(HttpStatus.OK).body(scrapService.findAllByMemberId(memberId));
    }

    @PostMapping("member/scrap")
    public ResponseEntity<ScrapResponseDto> saveScrap(
            @RequestBody ScrapInsertRequestDto scrapInsertRequestDto,
            HttpServletRequest request
    ) {

        String token = jwtUtil.getAccessTokenFromRequest(request);
        UUID memberId = jwtUtil.getMemberIdFromToken(token);

        ScrapResponseDto savedScrap =  scrapService.saveScrap(scrapInsertRequestDto, memberId);

        return ResponseEntity.status(HttpStatus.OK).body(savedScrap);
    }


    @PutMapping("member/scrap/{scrapId}")
    public ResponseEntity<?> updateScrap(
            @PathVariable("scrapId") UUID scrapId,
            @RequestBody ScrapRequestDto scrapRequestDto,
            HttpServletRequest request
    ) {
        String token = jwtUtil.getAccessTokenFromRequest(request);
        UUID memberId = jwtUtil.getMemberIdFromToken(token);

        scrapService.updateScrap(scrapRequestDto, memberId, scrapId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("member/scrap/{scrapId}")
    public ResponseEntity<?> deleteScrap(
            @PathVariable("scrapId") UUID scrapId,
            HttpServletRequest request
    ) throws AccessDeniedException {
        String token = jwtUtil.getAccessTokenFromRequest(request);
        UUID memberId = jwtUtil.getMemberIdFromToken(token);

        scrapService.deleteScrap(scrapId, memberId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
