package com.msa4meerkatgram.global.security.constant;

import lombok.Getter;

@Getter
public enum ProviderPolicy {
    // private ProviderPolicy NONE = new ProviderPolicy("NONE");   //  아래의 NONE("NONE")절과 같다.
    NONE("NONE")
    ,KAKAO("KAKAO")
    ,GOOGLE("GOOGLE");

    private final String provider;

    ProviderPolicy(String provider) {
        this.provider = provider;
    }
}
