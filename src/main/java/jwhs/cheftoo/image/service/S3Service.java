package jwhs.cheftoo.image.service;

import jwhs.cheftoo.image.enums.S3ImageType;
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
    @Value("${cloud.aws.s3.recipe.image.bucket}")
    private String recipeImageBucket;

    @Value("${cloud.aws.s3.cookingorder.image.bucket}")
    private String cookingOrderImageBucket;

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    public void deleteImage(String key, String bucket) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        s3Client.deleteObject(deleteRequest);
    }


    // 레시피 이미지 존재 여부 확인
    public boolean doesImageExist(String key) {
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


    // presigned URL 발급 (GET)
    public URL generateImagePresignedGetUrl(String bucket, String key, Duration duration) {
        if (key == null) {
            return null;
        }

        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(duration)
                .getObjectRequest(getRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url();

    }

    // presigned URL 발급 (PUT)
    public URL generateImagePresignedPutUrl(String bucket, String key, String contentType, Duration duration) {
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(duration)
                .putObjectRequest(putRequest)
                .build();

        return s3Presigner.presignPutObject(presignRequest).url();
    }

    public URL generateRecipeImagePresignedPutUrl(String key, String contentType, S3ImageType s3ImageType) {
        return generateImagePresignedPutUrl(recipeImageBucket, key, contentType, s3ImageType.getDuration());
    }

    public URL generateCookingOrderImagePresignedPutUrl(String key, String contentType, S3ImageType s3ImageType) {
        return generateImagePresignedPutUrl(cookingOrderImageBucket, key, contentType, s3ImageType.getDuration());
    }

    public URL generateRecipeImagePresignedGetUrl(String key, S3ImageType s3ImageType) {
        return generateImagePresignedGetUrl(recipeImageBucket, key, s3ImageType.getDuration());
    }

    public URL generateCookingOrderImagePresignedGetUrl(String key, S3ImageType s3ImageType) {
        return generateImagePresignedGetUrl(cookingOrderImageBucket, key, s3ImageType.getDuration());
    }

    public void deleteRecipeImage(String key) {
        deleteImage(key, recipeImageBucket);
    }

    public void deleteCookingOrderImage(String key) {
        deleteImage(key, cookingOrderImageBucket);
    }


}
