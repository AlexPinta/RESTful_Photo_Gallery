package controller;

import java.io.*;
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
	final String INDEX_PAGE = "index";

	@RequestMapping("/photo")
	ModelAndView index() {
		fileManager.clearFileQueue();
		final ModelAndView modelAndView = new ModelAndView(INDEX_PAGE);
		setModelAttribute(modelAndView);
		return modelAndView;
	}

	/**
	 */
	@RequestMapping(value = "/photo/original", method = RequestMethod.GET)
	public ModelAndView originalSize() {
		final ModelAndView modelAndView = new ModelAndView(INDEX_PAGE);
		setModelAttribute(modelAndView);
		modelAndView.addObject("isOriginalSize", true);
		return modelAndView;
	}

	/**
	 */
	@RequestMapping(value = "/photo/blackBackground", method = RequestMethod.GET)
	public ModelAndView setPageBackground() {
		final ModelAndView modelAndView = new ModelAndView(INDEX_PAGE);
		setModelAttribute(modelAndView);
		modelAndView.addObject("backgroundColor", "#FF0000");
		return modelAndView;
	}

	/**
	 */
	@RequestMapping(value = "/photo/wh/{size}", method = RequestMethod.GET)
	public ModelAndView setPageBackground(@PathVariable String size) {
		final int HEIGHT_WIDTH_COUNT = 2;
		final String SIZE_DELIMITER = "x";
		final ModelAndView modelAndView = new ModelAndView(INDEX_PAGE);
		setModelAttribute(modelAndView);

		final String[] sizeAttributes = size.split(SIZE_DELIMITER);
		if (sizeAttributes.length == HEIGHT_WIDTH_COUNT) {
			modelAndView.addObject("imageHeight", sizeAttributes[0]);
			modelAndView.addObject("imageWidth", sizeAttributes[1]);
		}
		return modelAndView;
	}

	/**
	 */
	@RequestMapping(value = "/photo/row/{imageCountInRow}", method = RequestMethod.GET)
	public ModelAndView imageCountInRow(@PathVariable int imageCountInRow) {
		final ModelAndView modelAndView = new ModelAndView(INDEX_PAGE);
		setModelAttribute(modelAndView);
		modelAndView.addObject("imageCountInRow", imageCountInRow);
		return modelAndView;
	}



	@RequestMapping(value = "/getImage/{filePath}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public byte[] getImage(@PathVariable String filePath) {
		byte[] fileBytes = {0};
		try {
			fileBytes = fileManager.retrieveFile(filePath);
		} catch (IOException e) {
			//TODO logging
		}
		return fileBytes;
	}

	@RequestMapping(value = "/photo/addImage", method = RequestMethod.POST)
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
		final int DEFAULT_COUNT_IN_ROW = 4;
		final int DEFAULT_IMAGE_SIZE = 200;

		model.addObject("listFiles", fileManager.getFileQueue().toArray());
		model.addObject("imageCount", fileManager.getFileQueue().size());
		model.addObject("backgroundColor", "");
		model.addObject("imageCountInRow", DEFAULT_COUNT_IN_ROW);
		model.addObject("isOriginalSize", false);
		model.addObject("imageHeight", DEFAULT_IMAGE_SIZE);
		model.addObject("imageWidth", DEFAULT_IMAGE_SIZE);
	}
}