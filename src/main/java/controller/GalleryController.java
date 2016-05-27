package controller;

import java.io.IOException;
import java.util.Map;

import helper.EndPoints;
import helper.FileManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
* The GalleryController class implements managing requests and creating response.
* You can invoke different REST functionality by using different url
* 
* @author  Alex Pinta, Oleh Pinta
*/
@RestController
public class GalleryController {
	final static Logger logger = Logger.getLogger(GalleryController.class);
	@Autowired
	FileManager fileManager;
	final public String INDEX_PAGE = "index";

	/**
     * This method is used to open main page.
     * It initialize needed parameters and send them through the response
     */
	@RequestMapping(EndPoints.BASE_URL)
	ModelAndView index() {
		fileManager.clearFileQueue();
		final ModelAndView modelAndView = new ModelAndView(INDEX_PAGE);
		setModelAttribute(modelAndView);
		return modelAndView;
	}

	/**
     * This method is used to open gallery page with the photos with the original size.
     * It initialize needed parameters and send them through the response
     */
	@RequestMapping(value = EndPoints.RENDER_IMAGE_BY_ORIGINAL_SIZE, method = RequestMethod.GET)
	public ModelAndView originalSize() {
		final String ORIGINAL_IMAGE_SIZE = "auto";
		final ModelAndView modelAndView = new ModelAndView(INDEX_PAGE);
		setModelAttribute(modelAndView);
		modelAndView.addObject(ModelAttributePoint.IMAGE_HEIGHT, ORIGINAL_IMAGE_SIZE);
		modelAndView.addObject(ModelAttributePoint.IMAGE_WIDTH, ORIGINAL_IMAGE_SIZE);
		return modelAndView;
	}

	/**
     * This method is used to open gallery page with the photos on the black background.
     * It initialize needed parameters and send them through the response
     */
	@RequestMapping(value = EndPoints.SET_PAGE_BACKGROUND_COLOR, method = RequestMethod.GET)
	public ModelAndView setPageBackground() {
		final ModelAndView modelAndView = new ModelAndView(INDEX_PAGE);
		setModelAttribute(modelAndView);
		modelAndView.addObject(ModelAttributePoint.BACKGROUND_COLOR, "black");
		return modelAndView;
	}

	/**
     * This method is used to open gallery page with the photos with the given size.
     * It initialize needed parameters and send them through the response
     */
	@RequestMapping(value = EndPoints.SET_IMAGE_SIZE, method = RequestMethod.GET)
	public ModelAndView setImageSize(@PathVariable String height, @PathVariable String width) {
		final ModelAndView modelAndView = new ModelAndView(INDEX_PAGE);
		setModelAttribute(modelAndView);
		modelAndView.addObject(ModelAttributePoint.IMAGE_HEIGHT, height);
		modelAndView.addObject(ModelAttributePoint.IMAGE_WIDTH, width);
		return modelAndView;
	}

	/**
     * This method is used to open gallery page with the photos with the original size.
     * It initialize needed parameters and send them through the response
     */
	@RequestMapping(value = EndPoints.SET_IMAGE_COUNT_IN_ROW, method = RequestMethod.GET)
	public ModelAndView imageCountInRow(@PathVariable int imageCountInRow) {
		final ModelAndView modelAndView = new ModelAndView(INDEX_PAGE);
		modelAndView.addObject(ModelAttributePoint.IMAGE_COUNT_IN_ROW, imageCountInRow);
		setModelAttribute(modelAndView);
		return modelAndView;
	}

	/**
     * This method is used to retrieve the picture from the FileProperty.
     * It converts picture to the byte array and return this array
     */
	@RequestMapping(value = EndPoints.GET_FILE_BY_HASH_CODE, method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public byte[] getImage(@PathVariable String fileHashCode) {
		byte[] fileBytes = {0};
		try {
			fileBytes = fileManager.retrieveFile(fileHashCode);
		} catch (IOException e) {
			logger.error("Can't retrieve file by hash code : " + fileHashCode, e);
		}
		return fileBytes;
	}

	/**
     * This method is used to upload .png pictures from the client to server.
     * It initialize needed parameters and send them through the response
     */
	@RequestMapping(value = EndPoints.UPLOAD_IMAGES, method = RequestMethod.POST)
	public ModelAndView addPicture(HttpServletRequest request) {
		final ModelAndView modelAndView = new ModelAndView(INDEX_PAGE);
		final String FILES_PARAMETER = "uploadFolder";
		final String FILES_SUFFIX = ".png";

		MultiValueMap<String, MultipartFile> multiFileMap = ((StandardMultipartHttpServletRequest) request).getMultiFileMap();
		for (MultipartFile file : multiFileMap.get(FILES_PARAMETER)) {
			try {
				if (file.getOriginalFilename().endsWith(FILES_SUFFIX)) {
					fileManager.saveFile(file.getInputStream(), file.getOriginalFilename());
				}
			} catch (IOException e) {
				logger.error("Can't save file on server: " + file.getOriginalFilename(), e);
			}
		}
		setModelAttribute(modelAndView);
		return modelAndView;
	}

	/**
	 * method sets all needed attribute, so that thymeleaf could use them
	 * parameter model we change this parameter by setting attributes
	 */
	private void setModelAttribute(ModelAndView model) {
		int countInRow = 4;
		final int IMAGE_HEIGHT = 200;
		final int IMAGE_WIDTH = 200;

		if (!model.isEmpty()) {
			Map<String, Object> mapModel = model.getModel();
			if (mapModel.containsKey(ModelAttributePoint.IMAGE_COUNT_IN_ROW)) {
				countInRow = (int) mapModel.get(ModelAttributePoint.IMAGE_COUNT_IN_ROW);
			}
		}

		int[] imagesInRow = new int[countInRow];
		for (int i = countInRow-1; i >= 0; i--) {
			imagesInRow[countInRow-i-1] = i;
		}

		model.addObject(ModelAttributePoint.LIST_FILES, fileManager.getFileQueue().toArray());
		model.addObject(ModelAttributePoint.IMAGE_COUNT, fileManager.getFileQueue().size());
		model.addObject(ModelAttributePoint.BACKGROUND_COLOR, "");
		model.addObject(ModelAttributePoint.IMAGE_COUNT_IN_ROW, countInRow);
		model.addObject(ModelAttributePoint.IMAGE_HEIGHT, IMAGE_HEIGHT);
		model.addObject(ModelAttributePoint.IMAGE_WIDTH, IMAGE_WIDTH);
		model.addObject(ModelAttributePoint.IMAGES_IN_ROW, imagesInRow);
	}
}