package jwhs.cheftoo.image.exception;


//이미지 생성 예외
public class ImageSaveException extends RuntimeException{
    public ImageSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
