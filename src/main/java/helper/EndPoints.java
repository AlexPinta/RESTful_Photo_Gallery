package helper;

/**
* The EndPoints interface contains all urls, you could invoke as a user
* 
* @author  Alex Pinta, Oleh Pinta
*/
public interface EndPoints {
    String BASE_URL = "/photo";
    String RENDER_IMAGE_BY_ORIGINAL_SIZE = BASE_URL + "/original";
    String SET_PAGE_BACKGROUND_COLOR = BASE_URL + "/blackbackground";
    String SET_IMAGE_SIZE = BASE_URL + "/wh/{size}";
    String SET_IMAGE_COUNT_IN_ROW = BASE_URL + "/row/{imageCountInRow}";
    String GET_FILE_BY_HASH_CODE = BASE_URL + "/get/{fileHashCode}";
    String UPLOAD_IMAGES = BASE_URL + "/addImage";
}
