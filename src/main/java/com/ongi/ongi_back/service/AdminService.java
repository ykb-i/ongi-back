package com.ongi.ongi_back.service;

import org.springframework.http.ResponseEntity;

import com.ongi.ongi_back.common.dto.request.admin.PatchAnswerRequestDto;
import com.ongi.ongi_back.common.dto.request.admin.PatchNoticeRequestDto;
import com.ongi.ongi_back.common.dto.request.admin.PostNoticeRequestDto;
import com.ongi.ongi_back.common.dto.response.ResponseDto;

public interface AdminService {
    ResponseEntity<ResponseDto> patchAnswer(PatchAnswerRequestDto dto, Integer questionSequence, String userId);
    ResponseEntity<ResponseDto> postNotice(PostNoticeRequestDto dto, String userId);
    ResponseEntity<ResponseDto> patchNotice(PatchNoticeRequestDto dto, Integer sequence, String userId);
    ResponseEntity<ResponseDto> deleteNotice(Integer sequence, String userId);

}
