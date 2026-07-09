package com.msa4meerkatgram.global.responses;

public record GlobalRes<T>(
        String code
        , String message
        , T data
) {
    public static <T> GlobalRes<T> from(String code, String message, T data) {
        return new GlobalRes<T>(code, message, data);

        // Static 메소드는 GlobalRes가 인스턴스되서 저장되는 영역과 별개로 저장된다.(정보가 다르다)
        // 따라서 제네릭은 별도로 저장해야 한다.
    }
}
