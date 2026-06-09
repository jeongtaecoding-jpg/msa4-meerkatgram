package com.msa4meerkatgram.domain.post.controllers;

import com.msa4meerkatgram.domain.post.entities.Post;
import com.msa4meerkatgram.domain.post.requests.PostIndexReq;
import com.msa4meerkatgram.domain.post.requests.PostStoreReq;
import com.msa4meerkatgram.domain.post.responses.PostIndexRes;
import com.msa4meerkatgram.domain.post.services.PostService;
import com.msa4meerkatgram.global.responses.GlobalRes;
import io.jsonwebtoken.Claims;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PostController {

    // 이게 가장 먼저 실행이 됨!!! 이후 PostIndexReq으로 감!

    private final PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<GlobalRes<PostIndexRes>> index(PostIndexReq postIndexReq) {
        PostIndexRes postIndexRes = postService.index(postIndexReq);

        return ResponseEntity.status(200).body(
                GlobalRes.<PostIndexRes>builder()
                        .code("00")
                        .message("정상처리")
                        .data(postIndexRes)
                        .build()
        );
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<GlobalRes<Post>> show(
        @Min(value = 1, message = "1이상 숫자만 허용합니다.") @PathVariable long id
    ) {
        Post result = postService.show(id);

        return ResponseEntity.status(200).body(
                GlobalRes.<Post>builder()
                        .code("00")
                        .message("게시글 상세 정상 처리")
                        .data(result)
                        .build()
        );
    }

    @PostMapping("/posts/create")
    public ResponseEntity<GlobalRes<Post>> PostCreate(
            @RequestBody PostStoreReq storeReq, //   뷰에서 보낸 내용과 이미지
            @AuthenticationPrincipal Claims claims // // 토큰에서 꺼낸 로그인 정보
    ) {
        // 2. 토큰(claims)에서 유저 ID를 뽑아냅니다.
        Long userId = Long.parseLong(claims.getSubject());

        // 3. 서비스로 '유저ID, 내용, 이미지' 3가지를 정확히 전달합니다.
        Post savedPost = postService.create(userId, storeReq.content(), storeReq.image());

        return ResponseEntity.status(200).body(
                GlobalRes.<Post>builder()
                        .code("00")
                        .message("게시글 작성 완료")
                        .data(savedPost) // 4. 응답 데이터에 방금 저장된 게시글 정보를 함께 보내줍니다.
                        .build()
        );
    }
}
