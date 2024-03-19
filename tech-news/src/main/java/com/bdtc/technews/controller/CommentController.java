package com.bdtc.technews.controller;

import com.bdtc.technews.dto.CommentDetailingDto;
import com.bdtc.technews.dto.CommentRequestDto;
import com.bdtc.technews.service.comment.CommentService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/{newsId}")
    @Transactional
    public ResponseEntity createComment(@PathVariable UUID newsId, @RequestBody @Valid CommentRequestDto commentRequestDto) {
        CommentDetailingDto comment = commentService.createComment(newsId, commentRequestDto);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/{newsId}")
    public ResponseEntity getNewsComments(@PathVariable UUID newsId, @PageableDefault() Pageable pageable) {
        Page<CommentDetailingDto> commentsPage = commentService.getCommentsByNewsId(newsId, pageable);
        return ResponseEntity.ok(commentsPage);
    }
}