package sori.jakku.kkunkkyu.memore.common.exception;

import lombok.Getter;
import org.apache.http.HttpStatus;

@Getter
public enum Exception {

    USER_NOT_FOUND("유저를 찾을 수 없습니다.", HttpStatus.SC_NOT_FOUND),
    DUPLICATED_USERNAME("중복 아이디입니다.", HttpStatus.SC_BAD_REQUEST),

    MEMO_NOT_FOUND("메모를 찾을 수 없습니다.", HttpStatus.SC_NOT_FOUND),
    DUPLICATED_MEMO("중복 메모입니다.", HttpStatus.SC_BAD_REQUEST),

    NOT_YOUR_DATA("본인의 데이터가 아닙니다.", HttpStatus.SC_FORBIDDEN)

    ;


    private final String message;
    private final int code;

    Exception(String message, int code){
        this.message = message;
        this.code = code;
    }
}
