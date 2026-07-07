package com.msa4meerkatgram.domain.post.repositories;

import com.msa4meerkatgram.domain.post.entities.Post;
import com.msa4meerkatgram.domain.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    // Post : 이 저장소에서 사용할 주된 엔티티 객체
    // Long :  그 객체의 PK
    long countByUser(User user);
}
