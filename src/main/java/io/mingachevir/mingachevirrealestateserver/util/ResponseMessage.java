package io.mingachevir.mingachevirrealestateserver.util;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(
    using = ResponseMessageSerializer.class
)
@JsonDeserialize(
    using = ResponseMessageDeserializer.class
)
public class ResponseMessage {
    private String code;
    private String defaultMessage;

    public static ResponseMessage ok() {
        return new ResponseMessage("succeed", "Success");
    }

    public static ResponseMessage bad() {
        return new ResponseMessage("bad.request", "Bad Request");
    }

    public static ResponseMessage error() {
        return new ResponseMessage("error", "Error");
    }

    public static ResponseMessage ex() {
        return new ResponseMessage("exception", "Exception occurred");
    }

    public static ResponseMessage resourceNotFound() {
        return new ResponseMessage("resource.not.found", "Resource not found");
    }

    public String getCode() {
        return this.code;
    }

    public String getDefaultMessage() {
        return this.defaultMessage;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public void setDefaultMessage(final String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public ResponseMessage(final String code, final String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public ResponseMessage() {
    }
}
