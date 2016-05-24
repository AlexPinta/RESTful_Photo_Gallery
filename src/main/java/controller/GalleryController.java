package controller;

import java.io.*;
import java.util.Iterator;
import java.util.List;

import helper.CacheManager;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
	private final int MAX_MEM_SIZE = 20 * 1024;
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
		final String userFolder = System.getProperty("user.home");
		final String filePath = request.getServletContext().getInitParameter("file-upload");

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(MAX_MEM_SIZE);
		factory.setRepository(new File(userFolder));

		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List fileItems = upload.parseRequest(new ServletRequestContext(request));
			Iterator i = fileItems.iterator();
			while (i.hasNext()) {
				saveFile(filePath, (FileItem)i.next());
			}
		} catch(Exception ex) {
			//TODO logging
		}
	}

	private void saveFile(String filePath, FileItem fi) {
		File file;
		if ( !fi.isFormField () ) {
//            String fieldName = fi.getFieldName();
            String fileName = fi.getName();
//            String contentType = fi.getContentType();
//            boolean isInMemory = fi.isInMemory();
//            long sizeInBytes = fi.getSize();
            if( fileName.lastIndexOf("\\") >= 0 ){
                file = new File( filePath +
                        fileName.substring( fileName.lastIndexOf("\\"))) ;
            }else{
                file = new File( filePath +
                        fileName.substring(fileName.lastIndexOf("\\")+1)) ;
            }
			try {
				fi.write(file);
			} catch (Exception e) {
				//TODO logging
			}
        }
	}
}
