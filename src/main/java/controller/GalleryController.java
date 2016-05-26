package controller;

import java.io.*;
import java.util.Map;

import helper.EndPoints;
import helper.FileManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
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
		final ModelAndView modelAndView = new ModelAndView(INDEX_PAGE);
		setModelAttribute(modelAndView);
		modelAndView.addObject(ModelAttributePoint.ORIGINAL_IMAGE_SIZE, true);
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
	public ModelAndView setImageSize(@PathVariable String size) {
		final int HEIGHT_WIDTH_COUNT = 2;
		final String SIZE_DELIMITER = "x";
		final ModelAndView modelAndView = new ModelAndView(INDEX_PAGE);
		setModelAttribute(modelAndView);

		final String[] sizeAttributes = size.split(SIZE_DELIMITER);
		if (sizeAttributes.length == HEIGHT_WIDTH_COUNT) {
			modelAndView.addObject(ModelAttributePoint.IMAGE_HEIGHT, sizeAttributes[0]);
			modelAndView.addObject(ModelAttributePoint.IMAGE_WIDTH, sizeAttributes[1]);
		}
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
			//TODO logging
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
				//TODO logging
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
		int imageHeight = 200;
		int imageWidth = 200;
		boolean isOriginalSize = false;
		
		if (!model.isEmpty()) {
			Map<String, Object> mapModel = model.getModel();
			if (mapModel.containsKey(ModelAttributePoint.IMAGE_COUNT_IN_ROW)) {
				countInRow = (int) mapModel.get(ModelAttributePoint.IMAGE_COUNT_IN_ROW);
			}
			if (mapModel.containsKey(ModelAttributePoint.IMAGE_HEIGHT)) {
				imageHeight = Integer.valueOf((mapModel.get(ModelAttributePoint.IMAGE_HEIGHT).toString()));
			}
			if (mapModel.containsKey(ModelAttributePoint.IMAGE_WIDTH)) {
				imageWidth = Integer.valueOf((mapModel.get(ModelAttributePoint.IMAGE_WIDTH).toString()));
			}
			if (mapModel.containsKey(ModelAttributePoint.ORIGINAL_IMAGE_SIZE)) {
				isOriginalSize = Boolean.valueOf((mapModel.get(ModelAttributePoint.ORIGINAL_IMAGE_SIZE).toString()));
			}
		}
		
		int[] intArr = new int[countInRow];
		
		for (int i = countInRow-1; i >= 0; i--) {
			intArr[countInRow-i-1] = i;
		}


		model.addObject(ModelAttributePoint.LIST_FILES, fileManager.getFileQueue().toArray());
		model.addObject(ModelAttributePoint.IMAGE_COUNT, fileManager.getFileQueue().size());
		model.addObject(ModelAttributePoint.BACKGROUND_COLOR, "");
		model.addObject(ModelAttributePoint.IMAGE_COUNT_IN_ROW, countInRow);
		model.addObject(ModelAttributePoint.ORIGINAL_IMAGE_SIZE, isOriginalSize);
		model.addObject(ModelAttributePoint.IMAGE_HEIGHT, imageHeight);
		model.addObject(ModelAttributePoint.IMAGE_WIDTH, imageWidth);
		model.addObject("intArr", intArr);
	}
}