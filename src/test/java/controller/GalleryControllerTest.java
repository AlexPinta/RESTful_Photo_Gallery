package controller;

//import helper.EndPoint;
//import helper.FileManager;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import static org.mockito.Mockito.*;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import org.springframework.test.web.servlet.MockMvc;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
//
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import java.util.Queue;
//import java.util.concurrent.ConcurrentLinkedQueue;


public class GalleryControllerTest {

//    @Mock
//    FileManager fileManager;
//
//    @InjectMocks
//    private GalleryController galleryController;
//
//    private MockMvc mockMvc;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        this.mockMvc = MockMvcBuilders.standaloneSetup(galleryController).build();
//    }
//
//    @Test
//    public void testIndex() throws Exception {
//        Queue emptyQueue = new ConcurrentLinkedQueue();
//        when(fileManager.getFileQueue()).thenReturn(emptyQueue);
//
//        this.mockMvc.perform(get(EndPoint.BASE_URL))
//                .andExpect(status().isOk())
//                .andExpect(forwardedUrl(galleryController.INDEX_PAGE))
//                .andExpect(model().attribute(ModelAttributePoint.LIST_FILES, emptyQueue.toArray()));
//    }
//
//    @Test
//    public void testOriginalSize() throws Exception {
////        this.mockMvc.perform(get(EndPoint.RENDER_IMAGE_BY_ORIGINAL_SIZE))
//////                .param("email", "mvcemail@test.com")
////
////                .andDo(print())
////                .andExpect(status().isOk())
////                .andExpect(forwardedUrl(galleryController.INDEX_PAGE))
////                .andExpect(model().attribute(ModelAttributePoint.LIST_FILES, emptyQueue.toArray()));
//
//    }
//
//    @Test
//    public void testSetPageBackground() throws Exception {
////        this.mockMvc.perform(get(EndPoint.RENDER_IMAGE_BY_ORIGINAL_SIZE))
////                .andDo(print())
////                .andExpect(status().isOk())
////                .andExpect(forwardedUrl(galleryController.INDEX_PAGE))
////                .andExpect(model().attribute(ModelAttributePoint.LIST_FILES, emptyQueue.toArray()));
//
//    }
//
//    @Test
//    public void testsetImageSize() throws Exception {
////        this.mockMvc.perform(get(EndPoint.RENDER_IMAGE_BY_ORIGINAL_SIZE)
////                .param("email", "mvcemail@test.com"))
////
////                .andDo(print())
////                .andExpect(status().isOk())
////                .andExpect(forwardedUrl(galleryController.INDEX_PAGE))
////                .andExpect(model().attribute(ModelAttributePoint.LIST_FILES, emptyQueue.toArray()));
//
//    }
//
//    @Test
//    public void testImageCountInRow() throws Exception {
//
//    }
//
//    @Test
//    public void testGetImage() throws Exception {
//
//    }
//
//    @Test
//    public void testAddPicture() throws Exception {
//
//    }
}