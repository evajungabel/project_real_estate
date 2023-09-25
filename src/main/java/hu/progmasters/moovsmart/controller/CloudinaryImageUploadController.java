package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.exception.AuthenticationExceptionImpl;
import hu.progmasters.moovsmart.service.CloudinaryImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/cloudinary")
@Slf4j
public class CloudinaryImageUploadController {

    private CloudinaryImageService cloudinaryImageService;

    @Autowired
    public CloudinaryImageUploadController(CloudinaryImageService cloudinaryImageService) {
        this.cloudinaryImageService = cloudinaryImageService;
    }

    @PostMapping
    @Operation(summary = "Save property's image")
    @ApiResponse(responseCode = "201", description = "Property's image is saved")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("propertyId") Long propertyId) throws AuthenticationExceptionImpl {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, POST image /api/cloudinary by customer propertyId " + propertyId);
        Map<String, Object> data = this.cloudinaryImageService.upload(file, userDetails.getUsername(), propertyId);
        log.info("POST data image from repository/api/cloudinary by customer propertyId: " + propertyId);
        cloudinaryImageService.getURL(data);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @PostMapping("/{username}")
    @Operation(summary = "Save property's image")
    @ApiResponse(responseCode = "201", description = "Property's image is saved")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Map<String, Object>> uploadImage(@PathVariable("username") String username, @RequestParam("file") MultipartFile file, @RequestParam("propertyId") Long propertyId) throws AuthenticationExceptionImpl {
        log.info("Http request, POST image /api/cloudinary by admin propertyId " + propertyId);
        Map<String, Object> data = this.cloudinaryImageService.upload(file, username, propertyId);
        log.info("POST data image from repository/api/cloudinary by admin propertyId: " + propertyId);
        cloudinaryImageService.getURL(data);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @PostMapping("/uploadurl")
    @Operation(summary = "Save property's image by customer")
    @ApiResponse(responseCode = "201", description = "Property's image is saved by customer.")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<Map<String, Object>> uploadImageFromURL(@RequestParam("url") String url, @RequestParam("propertyId") Long propertyId) throws AuthenticationExceptionImpl {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, POST imageURL /api/cloudinary by customer propertyId " + propertyId);
        Map<String, Object> data = this.cloudinaryImageService.uploadFromURL(url, userDetails.getUsername(), propertyId);
        log.info("POST data imageURL from repository/api/cloudinary by customer propertyId: " + propertyId);
        cloudinaryImageService.getURL(data);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @PostMapping("/uploadurl/{username}")
    @Operation(summary = "Save property's image by admin")
    @ApiResponse(responseCode = "201", description = "Property's image is saved by admin.")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Map<String, Object>> uploadImageFromURL(@PathVariable("username") String username, @RequestParam("url") String url, @RequestParam("propertyId") Long propertyId) throws AuthenticationExceptionImpl {
        log.info("Http request, POST imageURL /api/cloudinary by admin propertyId " + propertyId);
        Map<String, Object> data = this.cloudinaryImageService.uploadFromURL(url, username, propertyId);
        log.info("POST data imageURL from repository/api/cloudinary by admin propertyId: " + propertyId);
        cloudinaryImageService.getURL(data);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }


    @GetMapping
    @Operation(summary = "Notification URL about saved property's image")
    @ApiResponse(responseCode = "201", description = "Notification URL about saved property's image is sent.")
    public ResponseEntity<String> notificationImage() {
        log.info("Http request, GET upoad /api/cloudinary");
        return new ResponseEntity<>("The file is uploaded!", HttpStatus.OK);
    }

    @DeleteMapping
    @Operation(summary = "Delete property's image form cloudinary by customer")
    @ApiResponse(responseCode = "200", description = "Property's image is deleted from cloudinary by customer.")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<Map<String, Object>> deleteImage(@RequestParam("propertyId") Long propertyId, @RequestParam("propertyImageURLId") Long propretyImageURLId) throws AuthenticationExceptionImpl {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, DELETE image /api/cloudinary by customer propertyId " + propertyId + "propertyImageURLId: " + propretyImageURLId);
        Map<String, Object> data = this.cloudinaryImageService.deleteImage(userDetails.getUsername(), propertyId, propretyImageURLId);
        log.info("DELETE data image from repository/api/cloudinary by customer propertyId: " + propertyId + "propertyImageURLId: " + propretyImageURLId);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @DeleteMapping("/uploadurl/{username}")
    @Operation(summary = "Delete property's image form cloudinary by admin")
    @ApiResponse(responseCode = "200", description = "Property's image is deleted from cloudinary by admin..")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Map<String, Object>> deleteImage(@PathVariable("username") String username, @RequestParam("url") String url, @RequestParam("propertyId") Long propertyId, @RequestParam("propertyImageURLId") Long propretyImageURLId) throws AuthenticationExceptionImpl {
        log.info("Http request, DELETE image /api/cloudinary by admin propertyId " + propertyId + "propertyImageURLId: " + propretyImageURLId);
        Map<String, Object> data = this.cloudinaryImageService.deleteImage(username, propertyId, propretyImageURLId);
        log.info("DELETE data image from repository/api/cloudinary by admin propertyId: " + propertyId + "propertyImageURLId: " + propretyImageURLId);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
