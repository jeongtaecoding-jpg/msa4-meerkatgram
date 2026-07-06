package com.msa4meerkatgram.domain.post.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostMybatis {
    private Long id;
    private Long userId;
    private String content;
    private String image;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;


}
