package com.mega._NY.auth.config.exception;

import lombok.Getter;

public enum ExceptionCode {
    USER_NOT_FOUND(404, "회원 정보를 찾을 수 없습니다."),
    EXIST_EMAIL(409,"이미 가입한 e-mail입니다."),
    EXIST_NICK_NAME(409,"이미 존재하는 닉네임입니다."),
    EXIST_PHONE_NUMBER(409,"이미 존재하는 연락처입니다."),
    ORDER_NOT_FOUND(404, "존재하지 않는 주문입니다."),
    CART_NOT_FOUND(404, "존재하지 않는 카트입니다."),
    ITEM_NOT_FOUND(404, "존재하지 않는 상품입니다."),
    ITEMCART_NOT_FOUND(404, "존재하지 않는 항목입니다."),
    REVIEW_NOT_FOUND(404, "존재하지 않는 리뷰입니다."),
    REVIEW_NOT_ALLOWED(403, "구매하지 않은 상품에 대해 리뷰를 작성할 수 없습니다."),
    REVIEW_ALREADY_EXISTS(403, "후기 작성한 상품입니다."),
    TALK_NOT_FOUND(404, "존재하지 않는 토크입니다."),
    ACCESS_DENIED_USER(403, "권한이 없는 유저입니다."),
    EXPIRED_TID(404,"tid가 null입니다."),
    PERIOD_NOT_CHANGE(400, "주기를 변경할 수 없습니다."),
    NICKNAME_TOO_LONG(400, "닉네임은 5글자 이하여야 합니다."),
    PAY_CANCEL(404, "결제 취소."),
    PAY_FAILED(500, "결제 실패.");
    ;


    @Getter
    private int code;
    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}