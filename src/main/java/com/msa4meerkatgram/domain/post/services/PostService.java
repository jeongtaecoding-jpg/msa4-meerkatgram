package com.msa4meerkatgram.domain.post.services;

import com.msa4meerkatgram.domain.post.entities.PostMybatis;
import com.msa4meerkatgram.domain.post.mapper.PostMapper;
import com.msa4meerkatgram.domain.post.requests.PostIndexReq;
import com.msa4meerkatgram.domain.post.responses.PostIndexRes;
import com.msa4meerkatgram.domain.user.entities.UserMybatis;
import com.msa4meerkatgram.domain.user.mapper.UserMapper;
import com.msa4meerkatgram.global.errors.custom.DeletedRecordException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostMapper postMapper;
    private final UserMapper userMapper;

    public PostIndexRes index(PostIndexReq postIndexReq) {
        int offset = (postIndexReq.page() - 1) * postIndexReq.limit();

        // 특정 페이지 게시글 조회
        List<PostMybatis> posts = postMapper.getPagination(postIndexReq.limit(), offset);

        // 토탈 획득
        long total = postMapper.getTotal();
        boolean lastPage = offset + postIndexReq.limit() >= total;

        // 컨트롤러 전달
        return PostIndexRes.builder()
                .total(total)
                .lastPage(lastPage)
                .posts(posts)
                .build();
    }

    public PostMybatis show(long id) {
        PostMybatis post = postMapper.findByPk(id);

        if(post == null) {
            throw new DeletedRecordException("이미 삭제된 게시글입니다.");
        }

        return post;
    }

    // 파라미터 정리: 진짜 필요한 정보만 받기 (예: 작성자 ID, 게시글 제목, 내용 등)
    // 컨트롤러에서 넘어온 User ID가 Long 타입일 수 있으니 래퍼 클래스를 쓰거나 필수 검증을 합니다.
    public PostMybatis create(Long userId, String content, String image) {

        // 1단계: 방어 로직. 토큰 ID로 유저를 조회했는데 DB에 없으면(탈퇴 등)
        UserMybatis user = userMapper.findByPk(userId);
        if (user == null) {
            throw new AccessDeniedException("유저가 아닙니다.");
        }

        // 2단계: 데이터베이스 테이블 모양과 똑같이 생긴 Post 엔티티 객체를 빌더 패턴으로 생성
        PostMybatis newPost = PostMybatis.builder()
                .userId(userId)
                .content(content)
                .image(image) // 엔티티 필드명과 동일하게 맞추세요
                .build();

        // 3단계: Mybatis 매퍼를 통해 실제 DB에 Insert 쿼리 실행
        postMapper.create(newPost); // 매퍼에 insert 관련 쿼리가 있어야 합니다.

        // 4단계 : 저장된 결과를 다시 돌려줌 (컨트롤러가 받아서 화면으로 보냄)
        return newPost;
    }

}
