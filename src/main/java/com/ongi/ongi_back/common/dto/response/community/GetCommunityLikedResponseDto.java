package com.ongi.ongi_back.common.dto.response.community;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ongi.ongi_back.common.dto.response.ResponseDto;
import com.ongi.ongi_back.common.entity.LikedEntity;

import lombok.Getter;

@Getter
public class GetCommunityLikedResponseDto extends ResponseDto {
    private List<String> likes;

    private GetCommunityLikedResponseDto(List<LikedEntity> likedEntities) {
        this.likes = new ArrayList<>();
        for (LikedEntity likedEntity: likedEntities) {
            String userId = likedEntity.getUserId();
            this.likes.add(userId);
        }
    }

    public static ResponseEntity<GetCommunityLikedResponseDto> success(List<LikedEntity> likedEntities) {
        GetCommunityLikedResponseDto body = new GetCommunityLikedResponseDto(likedEntities);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
