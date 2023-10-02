package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.exception.AuthenticationExceptionImpl;
import hu.progmasters.moovsmart.service.CloudinaryImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(summary = "Saving property's image")
    @ApiResponse(responseCode = "201", description = "Property's image is saved by customer.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("propertyId") Long propertyId) throws AuthenticationExceptionImpl {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, POST image /api/cloudinary, with: " + userDetails.getUsername() + " propertyId " + propertyId);
        Map<String, Object> data = this.cloudinaryImageService.upload(file, userDetails.getUsername(), propertyId);
        log.info("POST data image of repository from /api/cloudinary, with: " + userDetails.getUsername() +  "propertyId" + propertyId);
        cloudinaryImageService.getURL(data);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @PostMapping("/{username}")
    @Operation(summary = "Saving property's image by admin.")
    @ApiResponse(responseCode = "201", description = "Property's image is saved by admin.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Map<String, Object>> uploadImage(@PathVariable("username") String username, @RequestParam("file") MultipartFile file, @RequestParam("propertyId") Long propertyId) throws AuthenticationExceptionImpl {
        log.info("Http request, POST image /api/cloudinary/{username}, with: " + username + " propertyId " + propertyId);
        Map<String, Object> data = this.cloudinaryImageService.upload(file, username, propertyId);
        log.info("POST data image from repository/api/cloudinary/{username}, with: " + username + " propertyId: " + propertyId);
        cloudinaryImageService.getURL(data);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @PostMapping("/uploadurl")
    @Operation(summary = "Saving property's image uploaded as URL by customer")
    @ApiResponse(responseCode = "201", description = "Property's image uploaded as URL is saved by customer.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<Map<String, Object>> uploadImageFromURL(@RequestParam("url") String url, @RequestParam("propertyId") Long propertyId) throws AuthenticationExceptionImpl {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, POST imageURL /api/cloudinary/uploadurl, with: " + userDetails.getUsername() + " propertyId " + propertyId);
        Map<String, Object> data = this.cloudinaryImageService.uploadFromURL(url, userDetails.getUsername(), propertyId);
        log.info("POST data imageURL of repository from /api/cloudinary/uploadurl, with: " + userDetails.getUsername() + " propertyId: " + propertyId);
        cloudinaryImageService.getURL(data);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @PostMapping("/uploadurl/{username}")
    @Operation(summary = "Saving property's image uploaded as URL by admin")
    @ApiResponse(responseCode = "201", description = "Property's image uploaded as URL is saved by admin.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Map<String, Object>> uploadImageFromURL(@PathVariable("username") String username, @RequestParam("url") String url, @RequestParam("propertyId") Long propertyId) throws AuthenticationExceptionImpl {
        log.info("Http request, POST imageURL /api/cloudinary/uploadurl/{username}, with: " + username + " propertyId " + propertyId);
        Map<String, Object> data = this.cloudinaryImageService.uploadFromURL(url, username, propertyId);
        log.info("POST data imageURL from repository/api/cloudinary/uploadurl/{username}, with: " + username + " propertyId: " + propertyId);
        cloudinaryImageService.getURL(data);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @PostMapping("/profile")
    @Operation(summary = "Saving customer's image")
    @ApiResponse(responseCode = "201", description = "Customer's image is saved by customer.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<Map<String, Object>> uploadProfileImage(@RequestParam("file") MultipartFile file) throws AuthenticationExceptionImpl {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, POST image /api/cloudinary/profile, with: " + userDetails.getUsername());
        Map<String, Object> data = this.cloudinaryImageService.uploadProfile(file, userDetails.getUsername());
        log.info("POST data image of repository from /api/cloudinary/profile, with: " + userDetails.getUsername());
        cloudinaryImageService.getURLProfile(data);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @PostMapping("/profile/{username}")
    @Operation(summary = "Saving customers's image by admin.")
    @ApiResponse(responseCode = "201", description = "Customer's image is saved by admin.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Map<String, Object>> uploadImageProfile(@PathVariable("username") String username, @RequestParam("file") MultipartFile file, @RequestParam("propertyId") Long propertyId) throws AuthenticationExceptionImpl {
        log.info("Http request, POST image /api/cloudinary/profile/{username}, with: " + username);
        Map<String, Object> data = this.cloudinaryImageService.uploadProfile(file, username);
        log.info("POST data image from repository/api/cloudinary/profile/{username}, with: " + username);
        cloudinaryImageService.getURLProfile(data);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @PostMapping("/profile/uploadurl")
    @Operation(summary = "Saving customer's image uploaded as URL by customer")
    @ApiResponse(responseCode = "201", description = "Customer's image uploaded as URL is saved by customer.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<Map<String, Object>> uploadImageProfileFromURL(@RequestParam("url") String url) throws AuthenticationExceptionImpl {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, POST imageURL /api/cloudinary/profile/uploadurl, with: " + userDetails.getUsername());
        Map<String, Object> data = this.cloudinaryImageService.uploadProfileFromURL(url, userDetails.getUsername());
        log.info("POST data imageURL of repository from /api/cloudinary/profile/uploadurl, with: " + userDetails.getUsername());
        cloudinaryImageService.getURL(data);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @PostMapping("/profile/uploadurl/{username}")
    @Operation(summary = "Saving property's image uploaded as URL by admin")
    @ApiResponse(responseCode = "201", description = "Property's image uploaded as URL is saved by admin.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Map<String, Object>> uploadImageProfileFromURL(@PathVariable("username") String username, @RequestParam("url") String url) throws AuthenticationExceptionImpl {
        log.info("Http request, POST imageURL /api/cloudinary/profile/uploadurl/{username}, with: " + username);
        Map<String, Object> data = this.cloudinaryImageService.uploadProfileFromURL(url, username);
        log.info("POST data imageURL from repository/api/cloudinary/profile/uploadurl/{username}, with: " + username);
        cloudinaryImageService.getURL(data);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Notification URL about saved property's image")
    @ApiResponse(responseCode = "201", description = "Notification URL about saved property's image is sent.")
    public ResponseEntity<String> notificationImage() {
        log.info("Http request, GET upload notification /api/cloudinary");
        return new ResponseEntity<>("The file is uploaded!", HttpStatus.OK);
    }

    @DeleteMapping
    @Operation(summary = "Deleting property's image form cloudinary by customer")
    @ApiResponse(responseCode = "200", description = "Property's image is deleted from cloudinary by customer.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<Map<String, Object>> deleteImage(@RequestParam("propertyId") Long propertyId, @RequestParam("propertyImageURLId") Long propretyImageURLId) throws AuthenticationExceptionImpl {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, DELETE image /api/cloudinary, with: " + userDetails.getUsername() + " propertyId " + propertyId + "propertyImageURLId: " + propretyImageURLId);
        Map<String, Object> data = this.cloudinaryImageService.deleteImage(userDetails.getUsername(), propertyId, propretyImageURLId);
        log.info("DELETE data image of repository from /api/cloudinary, with: " + userDetails.getUsername() + " propertyId: " + propertyId + "propertyImageURLId: " + propretyImageURLId);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @DeleteMapping("/uploadurl/{username}")
    @Operation(summary = "Deleting property's image form cloudinary by admin")
    @ApiResponse(responseCode = "200", description = "Property's image is deleted from cloudinary by admin.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Map<String, Object>> deleteImage(@PathVariable("username") String username, @RequestParam("propertyId") Long propertyId, @RequestParam("propertyImageURLId") Long propretyImageURLId) throws AuthenticationExceptionImpl {
        log.info("Http request, DELETE image /api/cloudinary/uploadurl/{username}, with: " + username + " propertyId " + propertyId + "propertyImageURLId: " + propretyImageURLId);
        Map<String, Object> data = this.cloudinaryImageService.deleteImage(username, propertyId, propretyImageURLId);
        log.info("DELETE data image of repository from /api/cloudinary/uploadurl/{username}, with: " + username + " propertyId: " + propertyId + "propertyImageURLId: " + propretyImageURLId);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    @DeleteMapping("/profile")
    @Operation(summary = "Deleting customer's image form cloudinary by customer")
    @ApiResponse(responseCode = "200", description = "Customer's image is deleted from cloudinary by customer.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<Map<String, Object>> deleteProfileImage(@RequestParam("customUserImageURLId") Long customUserImageURLId) throws AuthenticationExceptionImpl {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Http request, DELETE image /api/cloudinary/profile, with: " + userDetails.getUsername() + "customUserImageURLId: " + customUserImageURLId);
        Map<String, Object> data = this.cloudinaryImageService.deleteProfileImage(userDetails.getUsername(), customUserImageURLId);
        log.info("DELETE data image of repository from /api/cloudinary/profile, with: " + userDetails.getUsername() + "customUserImageURLId: " + customUserImageURLId);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @DeleteMapping("/profile/{username}")
    @Operation(summary = "Deleting customer's image form cloudinary by admin")
    @ApiResponse(responseCode = "200", description = "Customer's image is deleted from cloudinary by admin.")
    @SecurityRequirement(name = "basicAuth")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Map<String, Object>> deleteImage(@PathVariable("username") String username, @RequestParam("customUserImageURLId") Long customUserImageURLId) throws AuthenticationExceptionImpl {
        log.info("Http request, DELETE image /api/cloudinary/profile/{username}, with: " + username + "customUserImageURLId: " + customUserImageURLId);
        Map<String, Object> data = this.cloudinaryImageService.deleteProfileImage(username, customUserImageURLId);
        log.info("DELETE data image of repository from /api/cloudinary/profile/{username}, with: " + username + "customUserImageURLId: " + customUserImageURLId);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
