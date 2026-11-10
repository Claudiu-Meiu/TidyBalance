package com.claudiumeiu.tidybalance_backend.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.claudiumeiu.tidybalance_backend.user.dto.PublicUserProfileDto;
import com.claudiumeiu.tidybalance_backend.user.dto.PublicUserProfileSearchResultDto;
import com.claudiumeiu.tidybalance_backend.user.entity.UserEntity;
import com.claudiumeiu.tidybalance_backend.user.exception.PublicUserProfileNotFoundException;
import com.claudiumeiu.tidybalance_backend.user.repository.UserRepository;

@Service
public class PublicUserProfileService {

    private final UserRepository userRepository;

    public PublicUserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PublicUserProfileDto getPublicUserProfile(UUID publicUserId) {
        UserEntity user = userRepository.findByPublicUserId(publicUserId)
                .orElseThrow(() -> new PublicUserProfileNotFoundException());

        return new PublicUserProfileDto(
                user.getPublicUserId(),
                user.getNickname(),
                user.getAccountCreatedAt());
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
                        user.getAccountCreatedAt()))
                .toList();
    }
}
