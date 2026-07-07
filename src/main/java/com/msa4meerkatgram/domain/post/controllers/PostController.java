package com.msa4meerkatgram.domain.post.controllers;

import com.msa4meerkatgram.domain.post.responses.PostWithUserRes;
import com.msa4meerkatgram.domain.post.services.PostService;
import com.msa4meerkatgram.global.responses.GlobalRes;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PostController {

    // 이게 가장 먼저 실행이 됨!!! 이후 PostIndexReq으로 감!
    private final PostService postService;

//    @GetMapping("/posts")
//    public ResponseEntity<GlobalRes<PostIndexRes>> index(PostIndexReq postIndexReq) {
//        PostIndexRes postIndexRes = postService.index(postIndexReq);
//
//        return ResponseEntity.status(200).body(
//                GlobalRes.<PostIndexRes>builder()
//                        .code("00")
//                        .message("정상처리")
//                        .data(postIndexRes)
//                        .build()
//        );
//    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<GlobalRes<PostWithUserRes>> show(
        @Min(value = 1, message = "1이상 숫자만 허용합니다.") @PathVariable long id
    ) {
        PostWithUserRes result = postService.show(id);

        return ResponseEntity.status(200).body(
                GlobalRes.<PostWithUserRes>builder()
                        .code("00")
                        .message("게시글 상세 정상 처리")
                        .data(result)
                        .build()
        );
    }

//    @PostMapping("/posts/create")
//    public ResponseEntity<GlobalRes<PostMybatis>> PostCreate(
//            // @RequestBody: "프론트가 보낸 JSON 덩어리를 PostCreateReq 그릇에 알아서 담아줘!"
//            @RequestBody PostStoreReq storeReq, //   뷰에서 보낸 내용과 이미지
//
//            // 문지기(JwtFilter)가 토큰을 검사하고 열어준 유저 정보(Claims)를 주입받음
//            @AuthenticationPrincipal Claims claims // // 토큰에서 꺼낸 로그인 정보
//    ) {
//        // 토큰에서 로그인한 유저의 고유 ID를 뽑아냄 (더 이상 프론트에서 ID를 받지 않아 안전함)
//        Long userId = Long.parseLong(claims.getSubject());
//
//        // 서비스(핵심 업무)로 유저 ID와 DTO에 담긴 글, 사진 주소를 넘겨서 저장을 지시함
//        PostMybatis savedPost = postService.create(userId, storeReq.content(), storeReq.image());
//
//        // 약속된 포맷(코드, 메시지, 데이터)으로 HTTP 상태 200과 함께 성공 응답을 내려줌
//        return ResponseEntity.status(200).body(
//                GlobalRes.<PostMybatis>builder()
//                        .code("00")
//                        .message("게시글 작성 완료")
//                        .data(savedPost) // 4. 응답 데이터에 방금 저장된 게시글 정보를 함께 보내줍니다.
//                        .build()
//        );
//    }
}
