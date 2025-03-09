package jwhs.cheftoo.controller;

import jwhs.cheftoo.service.KakaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {
    private final KakaoService kakaoService;
    @Autowired
    public HomeController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }


}
