package com.claudiumeiu.tidybalance_backend.user.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
