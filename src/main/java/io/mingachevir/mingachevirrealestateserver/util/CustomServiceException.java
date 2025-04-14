package io.mingachevir.mingachevirrealestateserver.util;

import org.springframework.http.HttpStatus;

public class CustomServiceException extends BaseException {
    protected HttpStatus httpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    public CustomServiceException(String message) {
        super(message);
    }

    public CustomServiceException(String message, Object... params) {
        super(message, params);
    }
}
