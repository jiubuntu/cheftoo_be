package jwhs.cheftoo.image.service;

import jwhs.cheftoo.image.exception.ImageSaveException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    @Value("${cloud.aws.s3.recipe.image.bucket}")
    private String recipeImageBucket;

    @Value("${cloud.aws.s3.cookingorder.image.bucket}")
    private String cookingOrderImageBucket;


    public String uploadRecipeImage(String key, MultipartFile file) throws ImageSaveException, IOException {
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(recipeImageBucket)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        return key;
    }

    public String suploadCookingOrderImage(String key, MultipartFile file) throws IOException {
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(cookingOrderImageBucket)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        return key;
    }



    public void deleteRecipeImage(String key) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(recipeImageBucket)
                .key(key)
                .build();
        s3Client.deleteObject(deleteRequest);
    }


    public void deleteCookingOrderImage(String key) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(cookingOrderImageBucket)
                .key(key)
                .build();
        s3Client.deleteObject(deleteRequest);
    }


    // 레시피 이미지 존재 여부 확인
    public boolean doesRecipeImageExist(String key) {
        try {
            HeadObjectRequest request = HeadObjectRequest.builder()
                    .bucket(recipeImageBucket)
                    .key(key)
                    .build();
            s3Client.headObject(request);
            return true;
        } catch (S3Exception e) {
            return false;
        }
    }

    // 조리순서 이미지 존재 여부 확인
    public boolean doesCookingOrderImageExist(String key) {
        try {
            HeadObjectRequest request = HeadObjectRequest.builder()
                    .bucket(cookingOrderImageBucket)
                    .key(key)
                    .build();
            s3Client.headObject(request);
            return true;
        } catch (S3Exception e) {
            return false;
        }
    }


    // recipe image presigned URL 발급 (GET)
    public URL generateRecipeImagePresignedGetUrl(String key, Duration duration) {
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(recipeImageBucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(duration)
                .getObjectRequest(getRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url();
    }

    // cooking order image presigned URL 발급 (GET)
    public URL generateCookingOrderImagePresignedGetUrl(String key, Duration duration) {
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(cookingOrderImageBucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(duration)
                .getObjectRequest(getRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url();
    }
    // presigned URL 발급 (PUT)
    public URL generateRecipeImagePresignedPutUrl(String key, Duration duration) {
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(recipeImageBucket)
                .key(key)
                .contentType("image/jpeg")
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(duration)
                .putObjectRequest(putRequest)
                .build();

        return s3Presigner.presignPutObject(presignRequest).url();
    }

    // presigned URL 발급 (PUT)
    public URL generateCookingOrderImagePresignedPutUrl(String key, Duration duration) {
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(cookingOrderImageBucket)
                .key(key)
                .contentType("image/jpeg")
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(duration)
                .putObjectRequest(putRequest)
                .build();

        return s3Presigner.presignPutObject(presignRequest).url();
    }
}
