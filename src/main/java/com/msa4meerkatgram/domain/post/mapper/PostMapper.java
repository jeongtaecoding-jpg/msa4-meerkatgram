package com.msa4meerkatgram.domain.post.mapper;

import com.msa4meerkatgram.domain.post.entities.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    List<Post> getPagination(int limit, int offset);
    long getTotal();
    Post findByPk(long id);
    long countPostsByUserId(long userId);

    // List<Post> create(Post post); 오답!!!
    // 수정됨: List<Post> 대신 int(성공한 행의 개수)를 사용
    int create(Post post);

   }

