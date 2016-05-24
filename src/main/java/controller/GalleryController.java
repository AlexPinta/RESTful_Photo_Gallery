package controller;

import java.io.*;
import java.math.BigInteger;

import helper.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

/**
 * informs Spring that it should convert the objects returned from the controller methods
 * into JSON or XML responses
 */
@RestController
public class GalleryController {
	@Autowired
	CacheManager cacheManager;

	@RequestMapping("/")
	ModelAndView index(){
		return new ModelAndView("index");
	}

	/**
	 * informs Spring that this method should receive a HTTP request
	 * the value parameter contains the context path to which this method is mapped
	 * the method Get informs that this method should only be invoked for bound Get request
	 * the produces element tells Spring to convert the List of Greeting objects into JSON response
	 */
	@RequestMapping(value = "/addPicture",
			method = RequestMethod.POST,
			consumes = MediaType.IMAGE_PNG_VALUE)
	protected void doPost(HttpServletRequest request) throws IOException, ServletException {
		final String relativePath = request.getParameter("relativePath");
		final Part filePart = request.getPart("file");

		InputStream fileContent = null;
		try (OutputStream out = new FileOutputStream(new File(relativePath + File.separator + relativePath))) {
			fileContent = filePart.getInputStream();

			int read = 0;
			final byte[] bytes = new byte[2048];

			while ((read = fileContent.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			cacheManager.addFile(out);

//			LOGGER.log(Level.INFO, "File{0}being uploaded to {1}", new Object[]{fileName, path});
		} catch (FileNotFoundException fne) {
//			LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}", new Object[]{fne.getMessage()});
		} finally {
			if (fileContent != null) {
				fileContent.close();
			}
		}
	}
}
