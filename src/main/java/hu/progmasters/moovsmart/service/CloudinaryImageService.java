package hu.progmasters.moovsmart.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.domain.PropertyImageURL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
@Transactional
public class CloudinaryImageService {

    private Cloudinary cloudinary;

    private CustomUserService customUserService;

    private PropertyService propertyService;

    private PropertyImageURLService propertyImageURLService;

    @Autowired
    public CloudinaryImageService(Cloudinary cloudinary, CustomUserService customUserService, PropertyService propertyService, PropertyImageURLService propertyImageURLService) {
        this.cloudinary = cloudinary;
        this.customUserService = customUserService;
        this.propertyService = propertyService;
        this.propertyImageURLService = propertyImageURLService;
    }


    public Map<String, Object> upload(MultipartFile file, String username, Long propertyId) {
        Property property = propertyService.findPropertyById(propertyId);

        PropertyImageURL propertyImageURL = new PropertyImageURL();
        propertyImageURL.setPropertyImageURL(String.valueOf(file));
        propertyImageURL.setProperty(property);
        propertyImageURLService.save(propertyImageURL);

        try {
            Map<String, Object> params = ObjectUtils.asMap(
                    "public_id", "myProperty" + propertyId + "/my_property" + propertyImageURL.getPropertyImageUrlId(),
                    "overwrite", true,
                    "faces", true,
                    "notification_url", "http://localhost:8080/api/cloudinary",
                    "resource_type", "auto");
            return cloudinary.uploader().upload(file.getBytes(), params);
        } catch (IOException e) {
            throw new RuntimeException("Image uploading fail!");
        }
    }


}
