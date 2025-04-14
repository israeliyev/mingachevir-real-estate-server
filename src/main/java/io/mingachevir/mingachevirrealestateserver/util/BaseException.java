package io.mingachevir.mingachevirrealestateserver.util;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

public abstract class BaseException extends RuntimeException {
    private String message;
    private Object[] params;

    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
        this.message = message;
    }

    public BaseException(String message, Object[] params) {
        this(message);
        this.params = params;
    }

    @NonNull
    protected abstract HttpStatus httpStatus();

    public final int responseStatusCode() {
        return this.httpStatus().value();
    }

    public Object[] getParams() {
        return this.params;
    }

    public String getMessage() {
        return this.message;
    }
}
