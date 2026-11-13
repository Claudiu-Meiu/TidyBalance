package com.claudiumeiu.tidybalance_backend.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.claudiumeiu.tidybalance_backend.file_storage.service.ProfilePictureStorageService;
import com.claudiumeiu.tidybalance_backend.user.dto.PublicUserProfileDto;
import com.claudiumeiu.tidybalance_backend.user.dto.PublicUserProfileSearchResultDto;
import com.claudiumeiu.tidybalance_backend.user.entity.UserEntity;
import com.claudiumeiu.tidybalance_backend.user.exception.PublicUserProfileNotFoundException;
import com.claudiumeiu.tidybalance_backend.user.repository.UserRepository;

@Service
public class PublicUserProfileService {

    private final UserRepository userRepository;
    private final ProfilePictureStorageService profilePictureStorageService;

    public PublicUserProfileService(UserRepository userRepository,
            ProfilePictureStorageService profilePictureStorageService) {
        this.userRepository = userRepository;
        this.profilePictureStorageService = profilePictureStorageService;
    }

    public PublicUserProfileDto getPublicUserProfile(UUID publicUserId) {
        UserEntity user = userRepository.findByPublicUserId(publicUserId)
                .orElseThrow(() -> new PublicUserProfileNotFoundException());

        return new PublicUserProfileDto(
                user.getPublicUserId(),
                user.getNickname(),
                user.getAccountCreatedAt(),
                user.getProfilePicture());

    }

    public List<PublicUserProfileSearchResultDto> searchPublicUserProfiles(String query) {
        String normalizedQuery = query.trim();

        if (normalizedQuery.length() < 2) {
            return List.of();
        }

        return userRepository.findByNicknameContainingIgnoreCase(query)
                .stream()
                .map(user -> new PublicUserProfileSearchResultDto(
                        user.getPublicUserId(),
                        user.getNickname(),
                        user.getAccountCreatedAt(),
                        user.getProfilePicture()))
                .toList();
    }

    public void updateProfilePicture(MultipartFile file) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        String originalFilename = file.getOriginalFilename();
        String extension = ".webp";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename
                    .substring(originalFilename.lastIndexOf(".")).toLowerCase();
        }

        String currentlySavedFilename = user.getProfilePicture();
        String filenameToUpload = user.getPublicUserId().toString() + extension;

        if (currentlySavedFilename != null
                && !currentlySavedFilename.isBlank()
                && !currentlySavedFilename.equals(filenameToUpload)) {
            profilePictureStorageService.deleteProfilePicture(currentlySavedFilename);
        }

        profilePictureStorageService.storeProfilePicture(file, filenameToUpload);

        user.setProfilePicture(filenameToUpload);
        userRepository.saveAndFlush(user);
    }

    public Resource getProfilePicture(String filename) {
        return profilePictureStorageService.loadProfilePicture(filename);
    }
}
