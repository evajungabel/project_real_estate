package hu.progmasters.moovsmart.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import hu.progmasters.moovsmart.domain.Property;
import hu.progmasters.moovsmart.domain.PropertyImageURL;
import hu.progmasters.moovsmart.exception.AuthenticationExceptionImpl;
import hu.progmasters.moovsmart.exception.ImageUploadFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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


    public Map<String, Object> upload(MultipartFile file, String username, Long propertyId) throws AuthenticationExceptionImpl {
        Property property = propertyService.findPropertyById(propertyId);

        if (username.equals(property.getCustomUser().getUsername())) {
           PropertyImageURL propertyImageURL = createNewPropertyImageURL(property);
            try {
                Map<String, Object> params = createMapForUpload(propertyId, propertyImageURL);
                return cloudinary.uploader().upload(file.getBytes(), params);
            } catch (IOException e) {
                throw new ImageUploadFailedException(username, propertyId);
            }
        } else {
            throw new AuthenticationExceptionImpl(username);
        }
    }


    public PropertyImageURL createNewPropertyImageURL(Property property) {
        PropertyImageURL propertyImageURL = new PropertyImageURL();
        propertyImageURL.setProperty(property);
        return propertyImageURLService.save(propertyImageURL);
    }

    public Map<String, Object> createMapForUpload(Long propertyId, PropertyImageURL propertyImageURL) {
        return ObjectUtils.asMap(
                "public_id", "myProperty" + propertyId + "/my_property" + propertyImageURL.getPropertyImageUrlId(),
                "overwrite", true,
                "faces", true,
                "notification_url", "http://localhost:8080/api/cloudinary",
                "resource_type", "auto");
    }

    public void getURL(Map<String, Object> data) {
        String url = (String) data.get("url");

        Long propertyImageURLId = Long.parseLong(url.split("my_property")[1].split("\\.")[0]);

        PropertyImageURL propertyImageURL = propertyImageURLService.findPropertyImageURLById(propertyImageURLId);
        propertyImageURL.setPropertyImageURL(url);
        propertyImageURLService.save(propertyImageURL);
    }

    public Map<String, Object> uploadFromURL(String url, String username, Long propertyId) throws AuthenticationExceptionImpl {
        Property property = propertyService.findPropertyById(propertyId);

        if (username.equals(property.getCustomUser().getUsername())) {
            PropertyImageURL propertyImageURL = createNewPropertyImageURL(property);
            try {
                Map<String, Object> params = createMapForUpload(propertyId, propertyImageURL);
                return cloudinary.uploader().upload(url, params);
            } catch (IOException e) {
                throw new ImageUploadFailedException(username, propertyId);
            }
        } else {
            throw new AuthenticationExceptionImpl(username);
        }
    }

    public Map<String, Object> deleteImage(String username, Long propertyId, Long propertyImageURLId) throws AuthenticationExceptionImpl {

        Property property = propertyService.findPropertyById(propertyId);

        String deleteFilePublicId = "myProperty" + propertyId + "/my_property" + propertyImageURLId;

        if (username.equals(property.getCustomUser().getUsername())) {
            try {
                propertyImageURLService.deleteById(propertyImageURLId);
                Map<String, Object> params = ObjectUtils.asMap(
                        "notification_url", "http://localhost:8080/api/cloudinary");
                return cloudinary.uploader().destroy(deleteFilePublicId, params);
            } catch (IOException e) {
                throw new ImageUploadFailedException(username, propertyId);
            }
        } else {
            throw new AuthenticationExceptionImpl(username);
        }
    }
}
