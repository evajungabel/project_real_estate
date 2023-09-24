package hu.progmasters.moovsmart.controller;

import hu.progmasters.moovsmart.service.CloudinaryImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("propertyId") Long propertyId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> data = this.cloudinaryImageService.upload(file, userDetails.getUsername(), propertyId);
        cloudinaryImageService.getURL(data);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<String> notificationImage() {
        return new ResponseEntity<>("The file is uploaded!", HttpStatus.OK);
    }
}
