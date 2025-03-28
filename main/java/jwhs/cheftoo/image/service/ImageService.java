package jwhs.cheftoo.image.service;

import jakarta.transaction.Transactional;
import jwhs.cheftoo.image.Images;
import jwhs.cheftoo.image.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ImageService {

    private ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }


    public void saveMainImage(MultipartFile file, UUID memberId, UUID recipeId) {
        // 대표 이미지 저장
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String path = "/uploads/images/main_images/" + today + "/" + fileName;
        File dest = new File(path);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }

        //이미지 테이블에 이미지 메타데이터 저장
        Images saved = imageRepository.save(
                Images.builder()
                        .recipeId(recipeId)
                        .memberId(memberId)
                        .imgPath(path)
                        .build());

    }

    // 레시피의 조리순서에 존재하는 이미지 저장
    public String saveCookingOrderImage(MultipartFile file)  {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String path = "/uploads/images/cooking_orders_images/" + today + "/" + fileName;
        File dest = new File(path);

        try {
            file.transferTo(dest);
            return path;
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }



}
