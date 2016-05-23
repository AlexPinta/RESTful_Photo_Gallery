package controller;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * informs Spring that it should convert the objects returned from the controller methods
 * into JSON or XML responses
 */
@RestController
public class GreetingController {
	/**
	 * informs Spring that this method should receive a HTTP request
	 * the value parameter contains the context path to which this method is mapped
	 * the method Get informs that this method should only be invoked for bound Get request
	 * the produces element tells Spring to convert the List of Greeting objects into JSON response
	 */
	@RequestMapping(
			value = "/api/greetings/{id}",
			method = RequestMethod.DELETE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> deleteGreeting(@PathVariable BigInteger id, @RequestBody Object greeting) {
//		boolean deleted = delete(id);
//		if (!deleted) {
//			return new ResponseEntity<Greeting>(HttpStatus.INTERNAL_SERVER_ERROR);
//		}
		
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
}
