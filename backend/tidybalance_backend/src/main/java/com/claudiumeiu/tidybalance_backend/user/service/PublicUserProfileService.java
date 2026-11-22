package com.claudiumeiu.tidybalance_backend.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.claudiumeiu.tidybalance_backend.file.file_storage.service.ProfilePictureStorageService;
import com.claudiumeiu.tidybalance_backend.file.file_validation.service.FileValidationService;
import com.claudiumeiu.tidybalance_backend.user.dto.PublicUserProfileDto;
import com.claudiumeiu.tidybalance_backend.user.dto.PublicUserProfileSearchResultDto;
import com.claudiumeiu.tidybalance_backend.user.entity.UserEntity;
import com.claudiumeiu.tidybalance_backend.user.exception.PublicUserProfileNotFoundException;
import com.claudiumeiu.tidybalance_backend.user.repository.UserRepository;

@Service
public class PublicUserProfileService {

    private final UserAuthService userAuthService;
    private final UserRepository userRepository;
    private final ProfilePictureStorageService profilePictureStorageService;
    private final FileValidationService fileValidationService;

    public PublicUserProfileService(UserRepository userRepository,
            ProfilePictureStorageService profilePictureStorageService, UserAuthService userAuthService,
            FileValidationService fileValidationService) {
        this.userRepository = userRepository;
        this.profilePictureStorageService = profilePictureStorageService;
        this.userAuthService = userAuthService;
        this.fileValidationService = fileValidationService;
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

    public Resource getProfilePicture(String filename) {
        return profilePictureStorageService.loadProfilePicture(filename);
    }

    public void updateProfilePicture(MultipartFile file) {
        UserEntity user = userAuthService.currentUser();

        String oldFilename = user.getProfilePicture();
        String newFilename = user.getPublicUserId() + ".webp";

        profilePictureStorageService.storeProfilePicture(file, newFilename);

        user.setProfilePicture(newFilename);
        userRepository.saveAndFlush(user);

        if (oldFilename != null
                && !oldFilename.isBlank()
                && !oldFilename.equals(newFilename)) {
            profilePictureStorageService.deleteProfilePicture(oldFilename);
        }
    }

    public void removeProfilePicture() {
        UserEntity user = userAuthService.currentUser();

        String currentFilename = user.getProfilePicture();

        if (currentFilename == null || currentFilename.isBlank()) {
            return;
        }

        profilePictureStorageService.deleteProfilePicture(currentFilename);

        user.setProfilePicture(null);
        userRepository.save(user);
    }
}
