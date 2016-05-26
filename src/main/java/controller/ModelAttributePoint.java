/**
* Controller classes used to manage users requests
* <p>
* These classes contain functionality to get request from client, process it and create response
* </p>
*
* @since 1.0
* @author Alex Pinta, Oleh Pinta
* @version 1.0
*/
package controller;

/**
* The ModelAttributePoint interface contains all parameters, we able to send from controller to the view
* 
* @author  Alex Pinta, Oleh Pinta
*/
public interface ModelAttributePoint {
    String LIST_FILES = "listFiles";
    String IMAGE_COUNT = "imageCount";
    String BACKGROUND_COLOR = "backgroundColor";
    String IMAGE_COUNT_IN_ROW = "imageCountInRow";
    String ORIGINAL_IMAGE_SIZE = "isOriginalSize";
    String IMAGE_HEIGHT = "imageHeight";
    String IMAGE_WIDTH = "imageWidth";
}
