package jwhs.cheftoo.image.controller;

import jwhs.cheftoo.image.dto.PresignedUrlResponseDto;
import jwhs.cheftoo.image.enums.S3ImageType;
import jwhs.cheftoo.image.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class S3Controller {

    private final S3Service s3Service;

    @Value("${image.recipe-image.path}")
    String recipeImageKey;
    @Value("${image.cookingorder-image.path}")
    String cookingOrderImageKey;


    @DeleteMapping("recipe-image/{key}")
    public ResponseEntity<Void> deleteRecipeImage(
            @PathVariable String key
    ) {
        s3Service.deleteRecipeImage(key);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("cooking-order-image/{key}")
    public ResponseEntity<Void> deleteCookingOrderImage(
            @PathVariable String key
    ) {
        s3Service.deleteCookingOrderImage(key);
        return ResponseEntity.ok().build();
    }


    // 레시피 이미지 저장 presignedURL 발급
    @GetMapping("recipe-image/presigned-put")
    public ResponseEntity<PresignedUrlResponseDto> getRecipePresignedPutUrl(
            @RequestParam("contentType") String contentType,
            @RequestParam("fileName") String fileName
    ) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String path = recipeImageKey + "/" + today + "/";
        fileName = UUID.randomUUID() + "_" + fileName;
        String key = path + fileName;

        URL url = s3Service.generateRecipeImagePresignedPutUrl(key, contentType, S3ImageType.RECIPE_PUT_DURATION);
        return ResponseEntity.status(HttpStatus.OK).body(
                PresignedUrlResponseDto.builder()
                        .url(url.toString())
                        .key(key)
                        .build()
        );
    }


    // 조리순서 이미지 저장 presignedURL 발급
    @GetMapping("cooking-order-image/presigned-put")
    public ResponseEntity<PresignedUrlResponseDto> getCookingOrderPresignedPutUrl(
            @RequestParam("contentType") String contentType,
            @RequestParam("fileName") String fileName
    ) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String path = cookingOrderImageKey + "/" + today + "/";
        fileName = UUID.randomUUID() + "_" + fileName;
        String key = path + fileName;

        URL url = s3Service.generateCookingOrderImagePresignedPutUrl(key, contentType, S3ImageType.COOKING_ORDER_PUT_DURATION);

        return ResponseEntity.status(HttpStatus.OK).body(
                PresignedUrlResponseDto.builder()
                        .url(url.toString())
                        .key(key)
                        .build()
        );
    }
}
