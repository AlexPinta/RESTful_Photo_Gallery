package controller;

import helper.EndPoints;
import helper.FileManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.mockito.Spy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;


public class GalleryControllerTest {

    @Spy
    FileManager fileManager;

    @InjectMocks
    private GalleryController galleryController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(galleryController).build();
        fileManager = new FileManager();
    }

    @Test
    public void testIndex() throws Exception {
        Queue emptyQueue = new ConcurrentLinkedQueue();

        this.mockMvc.perform(get(EndPoints.BASE_URL))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(galleryController.INDEX_PAGE))
                .andExpect(model().attribute(ModelAttributePoint.LIST_FILES, emptyQueue.toArray()));
    }

    @Test
    public void testOriginalSize() throws Exception {
        this.mockMvc.perform(get(EndPoints.RENDER_IMAGE_BY_ORIGINAL_SIZE))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(galleryController.INDEX_PAGE))
                .andExpect(model().attribute(ModelAttributePoint.ORIGINAL_IMAGE_SIZE, true));
    }

    @Test
    public void testSetPageBackground() throws Exception {
        this.mockMvc.perform(get(EndPoints.SET_PAGE_BACKGROUND_COLOR))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(galleryController.INDEX_PAGE))
                .andExpect(model().attribute(ModelAttributePoint.BACKGROUND_COLOR, "black"));
    }

    @Test
    public void testsetImageSize() throws Exception {
        this.mockMvc.perform(get(EndPoints.SET_IMAGE_SIZE.replace("{size}", "60x50")))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(galleryController.INDEX_PAGE))
                .andExpect(model().attribute(ModelAttributePoint.IMAGE_HEIGHT, "60"))
                .andExpect(model().attribute(ModelAttributePoint.IMAGE_WIDTH, "50"));
    }

    @Test
    public void testImageCountInRow() throws Exception {
        final String IMAGE_COUNT = "3";
        this.mockMvc.perform(get(EndPoints.SET_IMAGE_COUNT_IN_ROW.replace("{imageCountInRow}", IMAGE_COUNT)))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(galleryController.INDEX_PAGE))
                .andExpect(model().attribute(ModelAttributePoint.IMAGE_COUNT_IN_ROW, Integer.valueOf(IMAGE_COUNT)));
    }

    @Test
    public void testGetImage() throws Exception {
        final String TEST_FILE = "text.png";
        final String USER_FOLDER = System.getProperty("user.home")+"/test";
        fileManager.USER_FOLDER = USER_FOLDER;

        File file = new File(USER_FOLDER + File.pathSeparator + TEST_FILE);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        fileManager.saveFile(new FileInputStream(file), TEST_FILE);
        final FileManager.FileProperty fileProperty = fileManager.getFileQueue().peek();

        this.mockMvc.perform(get(EndPoints.GET_FILE_BY_HASH_CODE.replace("{fileHashCode}", fileProperty.getFileHashCode())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE));

        fileManager.deleteFile(fileProperty.getFileHashCode());
    }

//    @Test
//    public void testAddPicture() throws Exception {
//        final String FILES_PARAMETER = "uploadFolder";
//        MultiValueMap<Object, Object> multiValueMap = new LinkedMultiValueMap<>();
//
//        multiValueMap.add(FILES_PARAMETER, );
//        this.mockMvc.perform(post(EndPoints.UPLOAD_IMAGES)
//                .flashAttr(FILES_PARAMETER, ))
//                .andExpect(status().isOk())
//                .andExpect(forwardedUrl(galleryController.INDEX_PAGE));
//
//    }
}