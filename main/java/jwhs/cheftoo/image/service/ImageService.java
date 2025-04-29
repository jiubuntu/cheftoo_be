package jwhs.cheftoo.image.service;

import jakarta.transaction.Transactional;
import jwhs.cheftoo.image.entity.Images;
import jwhs.cheftoo.image.exception.MainImageNotFoundException;
import jwhs.cheftoo.image.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ImageService {

    @Value("${image.main-image.path}")
    String mainImagePath;

    @Value("${image.cookingorder-image.path}")
    String cookingOrderImagePath;

    private ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Images findMainImageByRecipeId(UUID recipeId) {
        return imageRepository.findMainImageByRecipeId(recipeId)
                .orElseThrow(() -> new MainImageNotFoundException("레시피의 대표 이미지를 찾을 수 없습니다."));
    }

    private String getImageHash(MultipartFile file) throws IOException {
        return DigestUtils.sha256Hex(file.getInputStream());
    }

    private String getImageHash(File file) throws IOException {
        return DigestUtils.sha256Hex(new FileInputStream(file));
    }


    // 이미 이미지 폴더가 생성되어있는지 체크하는 함수
    private void checkAndMakeDir(String dirPath) {
        //dir 폴더 없으면 생성
        File dir = new File(dirPath);
        if(!dir.exists()) {
            dir.mkdirs();
        }
    }


    // 대표 이미지 저장
    public UUID saveMainImage(MultipartFile file, UUID memberId, UUID recipeId) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String dirPath = mainImagePath + "/" + today + "/";
        String path = dirPath + fileName;

        checkAndMakeDir(dirPath);

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

        return saved.getImageId();

    }

    // 레시피의 조리순서에 존재하는 이미지 저장
    public String saveCookingOrderImage(MultipartFile file)  {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String dirPath = cookingOrderImagePath + "/" + today + "/";
        String path = dirPath + fileName;

        checkAndMakeDir(dirPath);

        File dest = new File(path);

        try {
            file.transferTo(dest);
            return path;
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }


    /**
     * 메인이미지 업데이트
     * Images 테이블에 데이터가 없다면 -> 새로 저장
     * 새로 업로드한 이미지와 기존 존재하던 이미지가 동일하다면 -> 아무것도 안함
     * 새로 업로드한 이미지와 기존 존재하던 이미지가 다르다면 -> 삭제 후 새로 저장
     * @param file
     * @param recipeId
     * @param memberId
     * @return
     * @throws IOException
     */
    public UUID updateMainImage(MultipartFile file, UUID recipeId, UUID memberId ) throws IOException{
        Images image = imageRepository.findMainImageByRecipeId(recipeId)
                .orElse(null);

        if (image == null ) { // 새로 저장
            return saveMainImage(file, memberId, recipeId);
        }

        // 이미 저장된 이미지가 있다면 -> 저장된 이미지의 해시값과 비교
        String existingImagePath = image.getImgPath();
        String existingImageHash = getImageHash(new File(existingImagePath));
        String uploadImageHash = getImageHash(file);

        if (existingImageHash.equals(uploadImageHash)) { // 기존 이미지와 동일하면 아무것도 안함
            return image.getImageId();
        }

        deleteMainImage(image.getImageId(), existingImagePath); // 기존 이미지와 다르면 삭제

        return saveMainImage(file, memberId, recipeId);


    }



    private boolean deleteMainImage(UUID imageId, String imgPath) {
        int affectedRow = imageRepository.deleteByImageId(imageId);

        if (affectedRow == 0) {
            return false;
        }

        // 테이블에서 삭제가 완료되었으면 이미지 파일 직접 삭제
        try {
            Path path = Paths.get(imgPath);
            Files.deleteIfExists(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void deleteByRecipeId(UUID recipeId) {
        imageRepository.deleteByRecipeId(recipeId);

    }





}
