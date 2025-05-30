package com.ongi.ongi_back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ongi.ongi_back.common.dto.request.community.PatchCommunityCommentRequestDto;
import com.ongi.ongi_back.common.dto.request.community.PatchCommunityPostRequestDto;
import com.ongi.ongi_back.common.dto.request.community.PostCommentRequestDto;
import com.ongi.ongi_back.common.dto.request.community.PostCommunityRequestDto;
import com.ongi.ongi_back.common.dto.response.ResponseDto;
import com.ongi.ongi_back.common.dto.response.community.GetCommunityPostResponseDto;
import com.ongi.ongi_back.common.dto.response.community.GetCommunityResponseDto;
import com.ongi.ongi_back.common.dto.response.community.GetCommunityCommentsResponseDto;
import com.ongi.ongi_back.common.dto.response.community.GetCommunityCommentResponseDto;
import com.ongi.ongi_back.common.dto.response.community.GetCommunityLikedResponseDto;
import com.ongi.ongi_back.service.CommunityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/community")
@RequiredArgsConstructor
public class CommunityController {
    
    private final CommunityService communityService;

    @PostMapping({"/write"})
    public ResponseEntity<ResponseDto> postCommunityPost(
        @RequestBody @Valid PostCommunityRequestDto requestBody,
        @AuthenticationPrincipal String userId
    )   {
        ResponseEntity<ResponseDto> response = communityService.postCommunityPost(requestBody, userId);
        return response;
    }

    @PatchMapping({"/{postSequence}"})
    public ResponseEntity<ResponseDto> patchCommunityPost(
        @RequestBody @Valid PatchCommunityPostRequestDto requestBody,
        @PathVariable("postSequence") Integer postSequence,
        @AuthenticationPrincipal String userId
    )   {
        ResponseEntity<ResponseDto> response = communityService.patchCommunityPost(requestBody, postSequence, userId);
        return response;
    }

    @PatchMapping({"/{postSequence}/view"})
    public ResponseEntity<ResponseDto> patchCommunityViewCount(
        @PathVariable("postSequence") Integer postSequence,
        @AuthenticationPrincipal String userId
    )   {
        ResponseEntity<ResponseDto> response = communityService.patchCommunityViewCount(postSequence);
        return response;
    }

    @DeleteMapping({"/{postSequence}"})
    public ResponseEntity<ResponseDto> deleteCommunityPost(
        @PathVariable("postSequence") Integer postSequence,
        @AuthenticationPrincipal String userId
    )   {
        ResponseEntity<ResponseDto> response = communityService.deleteCommunityPost(postSequence, userId);
        return response;
    }

    @GetMapping({"/{postSequence}"})
    public ResponseEntity<? super GetCommunityPostResponseDto> getCommunityPost(
        @PathVariable("postSequence") Integer postSequence
    )   {
        ResponseEntity<? super GetCommunityPostResponseDto> response = communityService.getCommunityPost(postSequence);
        return response;
    }

    @GetMapping({"", "/"})
    public ResponseEntity<? super GetCommunityResponseDto> getCommunity(
        @RequestParam(value="board", required=false) String board,
        @RequestParam(value="category", required=false) String category,
        @RequestParam(value="region", required=false) String region,
        @RequestParam(value="county", required=false) String county
    ) {
        ResponseEntity<? super GetCommunityResponseDto> response = null;
        String address = null;
        if (board.equals("전체 글")) {
            response = communityService.getCommunity();
        }
        else {
            response = communityService.getBoard(board);
        }

        if (category != null) {
            response = communityService.getCategory(category);
        }

        if (region != null) {
            if (county != null) address = region + " " + county;
            else address = region;
            response = communityService.getCounty(address, category);
        }

        return response;
    }

    @GetMapping({"/search"})
    public ResponseEntity<? super GetCommunityResponseDto> getCommunityPost(
        @RequestParam(value="type", required=false) String type,
        @RequestParam(value="keyword", required=false) String keyword
    )   {

        ResponseEntity<? super GetCommunityResponseDto> response = null;
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseDto.noSearchKeyword();
        } 
        else if(type.equals("writer")) {
            response = communityService.getCommunityPostByWriter(keyword);
        }
        else if(type.equals("title")) {
            response = communityService.getCommunityPostByTitle(keyword);
        }
        else if(type.equals("content")) {
            response = communityService.getCommunityPostByContent(keyword);
        }
        return response;
    }

    @PostMapping("/{postSequence}/comment")
    public ResponseEntity<ResponseDto> postCommunityComment(
        @RequestBody @Valid PostCommentRequestDto requestBody,
        @PathVariable("postSequence") Integer postSequence,
        @AuthenticationPrincipal String userId
    )   {
        ResponseEntity<ResponseDto> response = communityService.postComment(requestBody, postSequence, userId);
        return response;
    }

    @DeleteMapping("/{postSequence}/comment/{commentSequence}")
    public ResponseEntity<ResponseDto> deleteCommunityComment(
        @PathVariable("postSequence") Integer postSequence,
        @PathVariable("commentSequence") Integer commentSequence,
        @AuthenticationPrincipal String userId
    )   {
        ResponseEntity<ResponseDto> response = communityService.deleteCommunityComment(postSequence, commentSequence, userId);
        return response;
    }

    @GetMapping("/{postSequence}/comment")
    public ResponseEntity<? super GetCommunityCommentsResponseDto> getCommunityComments(
        @PathVariable("postSequence") Integer postSequence
    )   {
        ResponseEntity<? super GetCommunityCommentsResponseDto> response = communityService.getCommunityComments(postSequence);
        return response;
    }

    @PatchMapping("/{postSequence}/comment/{commentSequence}")
    public ResponseEntity<ResponseDto> patchCommunityComment(
        @RequestBody @Valid PatchCommunityCommentRequestDto dto,
        @PathVariable("postSequence") Integer postSequence,
        @PathVariable("commentSequence") Integer commentSequence,
        @AuthenticationPrincipal String userId
    )   {
        ResponseEntity<ResponseDto> response = communityService.patchCommunityComment(dto, postSequence, commentSequence, userId);
        return response;
    }

    @GetMapping("/{postSequence}/comment/{commentSequence}")
    public ResponseEntity<? super GetCommunityCommentResponseDto> getCommunityComment(
        @PathVariable("postSequence") Integer postSequence,
        @PathVariable("commentSequence") Integer commentSequence
    )   {
        ResponseEntity<? super GetCommunityCommentResponseDto> response = communityService.getCommunityComment(postSequence, commentSequence);
        return response;
    }

    @PutMapping("/{postSequence}/liked")
    public ResponseEntity<ResponseDto> putLiked(
        @PathVariable("postSequence") Integer postSequence,
        @AuthenticationPrincipal String userId
    )   {
        ResponseEntity<ResponseDto> response = communityService.putCommunityLiked(postSequence, userId);
        return response;
    }

    @GetMapping("/{postSequence}/liked")
    public ResponseEntity<? super GetCommunityLikedResponseDto> getCommunityliked(
        @PathVariable("postSequence") Integer postSequence
    )   {
        ResponseEntity<? super GetCommunityLikedResponseDto> response = communityService.getCommunityLiked(postSequence);
        return response;
    }
}
