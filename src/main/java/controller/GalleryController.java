package controller;

import java.io.*;
import java.util.Map;

import helper.EndPoint;
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
 * informs Spring that it should convert the objects returned from the controller methods
 * into JSON or XML responses
 */
@RestController
public class GalleryController {
	@Autowired
	FileManager fileManager;
	final public String INDEX_PAGE = "index";

	@RequestMapping(EndPoint.BASE_URL)
	ModelAndView index() {
		fileManager.clearFileQueue();
		final ModelAndView modelAndView = new ModelAndView(INDEX_PAGE);
		setModelAttribute(modelAndView);
		return modelAndView;
	}

	/**
	 */
	@RequestMapping(value = EndPoint.RENDER_IMAGE_BY_ORIGINAL_SIZE, method = RequestMethod.GET)
	public ModelAndView originalSize() {
		final ModelAndView modelAndView = new ModelAndView(INDEX_PAGE);
		setModelAttribute(modelAndView);
		modelAndView.addObject(ModelAttributePoint.ORIGINAL_IMAGE_SIZE, true);
		return modelAndView;
	}

	/**
	 */
	@RequestMapping(value = EndPoint.SET_PAGE_BACKGROUND_COLOR, method = RequestMethod.GET)
	public ModelAndView setPageBackground() {
		final ModelAndView modelAndView = new ModelAndView(INDEX_PAGE);
		setModelAttribute(modelAndView);
		modelAndView.addObject(ModelAttributePoint.BACKGROUND_COLOR, "#FF0000");
		return modelAndView;
	}

	/**
	 */
	@RequestMapping(value = EndPoint.SET_IMAGE_SIZE, method = RequestMethod.GET)
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
	 */
	@RequestMapping(value = EndPoint.SET_IMAGE_COUNT_IN_ROW, method = RequestMethod.GET)
	public ModelAndView imageCountInRow(@PathVariable int imageCountInRow) {
		final ModelAndView modelAndView = new ModelAndView(INDEX_PAGE);
		modelAndView.addObject(ModelAttributePoint.IMAGE_COUNT_IN_ROW, imageCountInRow);
		setModelAttribute(modelAndView);
		return modelAndView;
	}



	@RequestMapping(value = EndPoint.GET_FILE_BY_HASH_CODE, method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
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

	@RequestMapping(value = EndPoint.UPLOAD_IMAGES, method = RequestMethod.POST)
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
		int DEFAULT_COUNT_IN_ROW = 4;
		int DEFAULT_IMAGE_HEIGHT = 200;
		int DEFAULT_IMAGE_WIDTH = 200;
		
		if (!model.isEmpty()) {
			Map<String, Object> mapModel = model.getModel();
			if (mapModel.containsKey("imageCountInRow")) {
				DEFAULT_COUNT_IN_ROW = (int) mapModel.get("imageCountInRow");
			}
			if (mapModel.containsKey("imageHeight")) {
				DEFAULT_IMAGE_HEIGHT = Integer.valueOf((mapModel.get("imageHeight").toString()));
			}
			if (mapModel.containsKey("imageHeight")) {
				DEFAULT_IMAGE_WIDTH = Integer.valueOf((mapModel.get("imageWidth").toString()));
			}
		}
		
		int[] intArr = new int[DEFAULT_COUNT_IN_ROW];
		
		for (int i = DEFAULT_COUNT_IN_ROW-1; i >= 0; i--) {
			intArr[DEFAULT_COUNT_IN_ROW-i-1] = i;
		}


		model.addObject(ModelAttributePoint.LIST_FILES, fileManager.getFileQueue().toArray());
		model.addObject(ModelAttributePoint.IMAGE_COUNT, fileManager.getFileQueue().size());
		model.addObject(ModelAttributePoint.BACKGROUND_COLOR, "");
		model.addObject(ModelAttributePoint.IMAGE_COUNT_IN_ROW, DEFAULT_COUNT_IN_ROW);
		model.addObject(ModelAttributePoint.ORIGINAL_IMAGE_SIZE, false);
		model.addObject(ModelAttributePoint.IMAGE_HEIGHT, DEFAULT_IMAGE_HEIGHT);
		model.addObject(ModelAttributePoint.IMAGE_WIDTH, DEFAULT_IMAGE_WIDTH);
		model.addObject("intArr", intArr);
	}
}