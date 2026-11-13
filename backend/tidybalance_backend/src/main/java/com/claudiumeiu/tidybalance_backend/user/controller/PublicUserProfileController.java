package com.claudiumeiu.tidybalance_backend.user.controller;

import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.claudiumeiu.tidybalance_backend.user.dto.PublicUserProfileDto;
import com.claudiumeiu.tidybalance_backend.user.dto.PublicUserProfileSearchResultDto;
import com.claudiumeiu.tidybalance_backend.user.service.PublicUserProfileService;

@RestController
@RequestMapping("/api/users/profiles")
public class PublicUserProfileController {

    private final PublicUserProfileService publicUserProfileService;

    public PublicUserProfileController(PublicUserProfileService publicUserProfileService) {
        this.publicUserProfileService = publicUserProfileService;
    }

    @GetMapping("/{publicUserId}")
    public PublicUserProfileDto getPublicUserProfile(
            @PathVariable UUID publicUserId) {
        return publicUserProfileService.getPublicUserProfile(publicUserId);
    }

    @GetMapping("/search")
    public List<PublicUserProfileSearchResultDto> searchPublicUserProfiles(
            @RequestParam("q") String query) {
        return publicUserProfileService.searchPublicUserProfiles(query);
    }

    @PostMapping("/profile-picture")
    public void uploadProfilePicture(@RequestParam MultipartFile file) {
        publicUserProfileService.updateProfilePicture(file);
    }

    @GetMapping("/profile-picture/{filename:.+}")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable String filename) {
        Resource fileResource = publicUserProfileService.getProfilePicture(filename);

        String contentType = "application/octet-stream";

        try {
            contentType = Files.probeContentType(fileResource.getFile().toPath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
        } catch (Exception e) {
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileResource.getFilename() + "\"")
                .body(fileResource);
    }
}
