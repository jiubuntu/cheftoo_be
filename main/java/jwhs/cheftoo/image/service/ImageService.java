package jwhs.cheftoo.image.service;

import jakarta.transaction.Transactional;
import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.cookingOrder.dto.CookingOrderDto;
import jwhs.cheftoo.cookingOrder.dto.CookingOrderRequestSaveDto;
import jwhs.cheftoo.cookingOrder.entity.CookingOrder;
import jwhs.cheftoo.cookingOrder.repository.CookingOrderRepository;
import jwhs.cheftoo.image.entity.Images;
import jwhs.cheftoo.image.exception.MainImageNotFoundException;
import jwhs.cheftoo.image.repository.ImageRepository;
import jwhs.cheftoo.recipe.entity.Recipe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
    private CookingOrderRepository cookingOrderRepository;

    public ImageService(ImageRepository imageRepository, CookingOrderRepository cookingOrderRepository) {
        this.imageRepository = imageRepository;
        this.cookingOrderRepository = cookingOrderRepository;
    }

    // 트랜잭션 커밋 후 조리순서 이미지 저장하는 작업 저장
    private static final ThreadLocal<List<Runnable>> deferredImageSaves = ThreadLocal.withInitial(ArrayList::new);

    // 조리순서 이미지 저장 작업 추가
    private void addDeferredImageSave(Runnable task) {
        deferredImageSaves.get().add(task);
    }

    // 트랜잭션 커밋 후 조리순서 이미지 저장
    public void registerCookingOrderImageFileSaveTask() {
        List<Runnable> tasks = deferredImageSaves.get();

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                for (Runnable task : tasks) {
                    task.run();
                }
                tasks.clear(); // 현재 쓰레드의 리스트 초기화
            }
        });
    }


    // 트랜잭션 커밋 후, 파일시스템에 메인이미지 저장
    public void registerMainImageFileSaveTask(Runnable task) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                task.run();
            }
        });
    }

    public Images findMainImageByRecipeId(Recipe recipe) {
        return imageRepository.findMainImageByRecipe(recipe)
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
    public UUID saveMainImageMetaAndFile(MultipartFile file, Member member, Recipe recipe) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String dirPath = mainImagePath + "/" + today + "/";
        String path = dirPath + fileName;


        //이미지 테이블에 이미지 메타데이터 저장
        Images saved = imageRepository.save(
                Images.builder()
                        .recipe(recipe)
                        .member(member)
                        .imgPath(path)
                        .build());

        // 트랜잭션 커밋 후, 메인 이미지 파일 저장
        registerMainImageFileSaveTask(() -> {
            checkAndMakeDir(dirPath);
            File dest = new File(path);
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                throw new RuntimeException("이미지 저장 실패", e);
            }
        });

        return saved.getImageId();

    }

    // 레시피의 조리순서에 존재하는 이미지 저장
    public void saveCookingOrderImageMetaAndFile(CookingOrderRequestSaveDto step, MultipartFile stepImage, Recipe recipe, int idx)  {
        // 이미지의 메타 데이터 저장
        String fileName = UUID.randomUUID() + "_" + stepImage.getOriginalFilename();
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String dirPath = cookingOrderImagePath + "/" + today + "/";
        String path = dirPath + fileName;

        cookingOrderRepository.save(
                CookingOrder.builder()
                        .recipe(recipe)
                        .order(idx)
                        .content(step.getContent())
                        .imgPath(path)
                        .build()
        );


        // 트랜잭션 커밋 후, 이미지 저장
        addDeferredImageSave(() -> {
            checkAndMakeDir(dirPath);
            File dest = new File(path);
            try {
                stepImage.transferTo(dest);
            } catch (IOException e) {
                throw new RuntimeException("조리순서 이미지 저장 실패", e);
            }
        });

    }



//    public UUID updateMainImage(MultipartFile file, UUID recipeId, UUID memberId ) throws IOException{
//        Images image = imageRepository.findMainImageByRecipeId(recipeId)
//                .orElse(null);
//
//        if (image == null ) { // 새로 저장
//            return saveMainImage(file, memberId, recipeId);
//        }
//
//        // 이미 저장된 이미지가 있다면 -> 저장된 이미지의 해시값과 비교
//        String existingImagePath = image.getImgPath();
//        String existingImageHash = getImageHash(new File(existingImagePath));
//        String imageHash = getImageHash(file);
//
//        if (existingImageHash.equals(imageHash)) { // 기존 이미지와 동일하면 아무것도 안함
//            return image.getImageId();
//        }
//
//        deleteMainImage(image.getImageId(), existingImagePath); // 기존 이미지와 다르면 삭제
//
//        return saveMainImage(file, memberId, recipeId);
//
//
//    }



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

    public void deleteByRecipeId(Recipe recipe) {
        imageRepository.deleteByRecipe(recipe);

    }





}
