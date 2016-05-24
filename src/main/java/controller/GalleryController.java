package controller;

import java.io.*;
import java.util.Collections;
import java.util.Queue;

import helper.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		return new ModelAndView(INDEX_PAGE);
	}

	/**
	 * informs Spring that this method should receive a HTTP request
	 * the value parameter contains the context path to which this method is mapped
	 * the method Get informs that this method should only be invoked for bound Get request
	 * the produces element tells Spring to convert the List of Greeting objects into JSON response
	 */
	@RequestMapping(value = "/photo/addPicture", method = RequestMethod.POST)
	public ResponseEntity addPicture(HttpServletRequest request) {
		final String FILES_PARAMETER = "uploadFolder";
		final String FILES_SUFFIX = ".png";

		ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
		MultiValueMap<String, MultipartFile> multiFileMap = ((StandardMultipartHttpServletRequest) request).getMultiFileMap();
		for (MultipartFile file : multiFileMap.get(FILES_PARAMETER)) {
			try {
				if (file.getOriginalFilename().endsWith(FILES_SUFFIX)) {
					fileManager.saveFile(file.getInputStream(), file.getOriginalFilename());
				}
			} catch (IOException e) {
				responseEntity = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
				//TODO logging
			}

		}
		return responseEntity;
	}

	@RequestMapping(value = "/getImage/{filePath}", method = RequestMethod.GET)
	public void getImage(HttpServletResponse response, @PathVariable String filePath) {
		response.reset();
		response.setContentType("image/png");
		try {
			fileManager.includeFile(filePath, response.getOutputStream());
		} catch (IOException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			//TODO logging
		}
	}

	@RequestMapping(value = "/photo/row/{imageCountInRow}", method = RequestMethod.GET)
	public void getImage(HttpServletRequest request, HttpServletResponse response, @PathVariable int imageCountInRow) {
		setDefaultSetting(request, response);
	}

	private void setDefaultSetting(HttpServletRequest request, HttpServletResponse response){
		final int DEFAULT_COUNT_IN_ROW = 4;
		final int DEFAULT_IMAGE_SIZE = 200;

		request.setAttribute("listFiles", fileManager.getFileQueue().toArray());
		request.setAttribute("imageCount", fileManager.getFileQueue().size());
		request.setAttribute("backgroundStyle", "");
		request.setAttribute("imageCountInRow", DEFAULT_COUNT_IN_ROW);
		request.setAttribute("isOriginalSize", false);
		request.setAttribute("imageHeight", DEFAULT_IMAGE_SIZE);
		request.setAttribute("imageWidth", DEFAULT_IMAGE_SIZE);

		try {
			request.getServletContext().getRequestDispatcher("/"+INDEX_PAGE).forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}