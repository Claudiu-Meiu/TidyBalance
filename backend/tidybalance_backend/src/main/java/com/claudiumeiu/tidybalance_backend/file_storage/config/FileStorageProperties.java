package com.claudiumeiu.tidybalance_backend.file_storage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file-storage")
public class FileStorageProperties {

    private String profilePictureLocation;

    public String getProfilePictureLocation() {
        return profilePictureLocation;
    }

    public void setProfilePictureLocation(String location) {
        this.profilePictureLocation = location;
    }
}
