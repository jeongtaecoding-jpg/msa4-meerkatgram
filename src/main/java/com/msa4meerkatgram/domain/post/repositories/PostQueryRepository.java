package com.msa4meerkatgram.domain.post.repositories;

import com.msa4meerkatgram.domain.post.entities.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.msa4meerkatgram.domain.post.entities.QPost.post;  // QPost는 빌드를 하면 자동으로 생성된다.  build 디렉토리에 가면 볼 수 있음
import static com.msa4meerkatgram.domain.user.entities.QUser.user;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    // select *
    // from posts
    //    join users
    //      on posts.user_id = users.ic
    // where deleted_at is null
    // order by created_at desc, id asc
    // limit ? offset ?
    public List<Post> pagination(int offset, int limit) {
        return jpaQueryFactory
                .selectFrom(post)
                .join(post.user, user).fetchJoin()
                .orderBy(post.createdAt.desc(), post.id.desc())
                .limit(limit)
                .offset(offset)
                .fetch(); // 쿼리를 만들어서 db로 보냄

        // join 의 첫번째 파라미터 : 조인 대상 필드, 두번째 파라미터 : 별칭으로 사용할 객체
        // fetchJoin : DB와 통신을 여러 번 하는 현상(N+1 문제)을 해결할 수 있다.
    }
}
