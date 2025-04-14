package io.mingachevir.mingachevirrealestateserver.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;
import org.springframework.http.HttpStatus;

public class GenericResponse<T> implements Serializable {
    private static final long serialVersionUID = 7208220849220984767L;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private transient Integer pageNumber;
    @JsonInclude(Include.NON_NULL)
    private transient Integer pageSize;
    @JsonInclude(Include.NON_NULL)
    private transient Long totalElements;
    private transient ResponseMessage message;
    @JsonInclude(Include.NON_NULL)
    private transient List<ErrorFields> errors;
    private transient int responseStatus;
    @JsonInclude(Include.NON_NULL)
    private transient T data;
    @JsonInclude(Include.NON_NULL)
    private transient String errorMessageKey;
    @JsonInclude(Include.NON_NULL)
    private transient String errorMessage;
    @JsonInclude(Include.NON_NULL)
    private transient String stackTrace;

    public GenericResponse(ResponseMessage message, T data, List<ErrorFields> errors) {
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    public static <T> GenericResponse<T> empty() {
        return new GenericResponse<T>();
    }

    public static <T> GenericResponse<T> ok() {
        return createGenericResponse((Integer)null, (Integer)null, (Long)null, HttpStatus.OK.value(), ResponseMessage.ok(), (Object)null, (List)null);
    }

    public static <T> GenericResponse<T> ok(Integer pageNumber, Integer pageSize, Long totalElements, T data) {
        return createGenericResponse(pageNumber, pageSize, totalElements, HttpStatus.OK.value(), ResponseMessage.ok(), data, (List)null);
    }

    public static <T> GenericResponse<T> ok(T data) {
        return createGenericResponse((Integer)null, (Integer)null, (Long)null, HttpStatus.OK.value(), ResponseMessage.ok(), data, (List)null);
    }

    public static <T> GenericResponse<T> ok(ResponseMessage message) {
        return createGenericResponse((Integer)null, (Integer)null, (Long)null, HttpStatus.OK.value(), message, (Object)null, (List)null);
    }

    public static <T> GenericResponse<T> bad() {
        return createGenericResponse((Integer)null, (Integer)null, (Long)null, HttpStatus.BAD_REQUEST.value(), ResponseMessage.bad(), (Object)null, (List)null);
    }

    public static <T> GenericResponse<T> bad(Throwable ex, String key) {
        GenericResponse r = createGenericResponse((Integer)null, (Integer)null, (Long)null, HttpStatus.BAD_REQUEST.value(), ResponseMessage.bad(), (Object)null, (List)null);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        r.setStackTrace(sw.toString());
        r.setErrorMessageKey(key);
        r.setErrorMessage(ex.getMessage());
        return r;
    }

    public static <T> GenericResponse<T> resourceNotFound() {
        return createGenericResponse((Integer)null, (Integer)null, (Long)null, HttpStatus.NOT_FOUND.value(), ResponseMessage.resourceNotFound(), (Object)null, (List)null);
    }

    public static <T> GenericResponse<T> withError(List<ErrorFields> errors) {
        return  createGenericResponse((Integer)null, (Integer)null, (Long)null, HttpStatus.NOT_ACCEPTABLE.value(), ResponseMessage.error(), null, errors);
    }

    public static GenericResponse<?> withError(int responseStatus, String errorMessageKey, String localizedErrorMessage) {
        return createGenericResponse((Integer)null, (Integer)null, (Long)null, responseStatus, new ResponseMessage(errorMessageKey, localizedErrorMessage), (Object)null, (List)null);
    }

    public static <T> GenericResponse<T> fromPageableData(PageableData<?> pageableData, T data) {
        return ok(pageableData.getPageNumber(), pageableData.getPageSize(), pageableData.getTotalElements(), data);
    }

    private static <T> GenericResponse<T> createGenericResponse(Integer pageNumber, Integer pageSize, Long totalElements, int responseStatus, ResponseMessage message, T data, List<ErrorFields> errors) {
        GenericResponse<T> genericResponse = new GenericResponse<T>(message, data, errors);
        genericResponse.setPageNumber(pageNumber);
        genericResponse.setPageSize(pageSize);
        genericResponse.setTotalElements(totalElements);
        genericResponse.setResponseStatus(responseStatus);
        return genericResponse;
    }

    public Integer getPageNumber() {
        return this.pageNumber;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public Long getTotalElements() {
        return this.totalElements;
    }

    public ResponseMessage getMessage() {
        return this.message;
    }

    public List<ErrorFields> getErrors() {
        return this.errors;
    }

    public int getResponseStatus() {
        return this.responseStatus;
    }

    public T getData() {
        return this.data;
    }

    public String getErrorMessageKey() {
        return this.errorMessageKey;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public String getStackTrace() {
        return this.stackTrace;
    }

    public void setPageNumber(final Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(final Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalElements(final Long totalElements) {
        this.totalElements = totalElements;
    }

    public void setMessage(final ResponseMessage message) {
        this.message = message;
    }

    public void setErrors(final List<ErrorFields> errors) {
        this.errors = errors;
    }

    public void setResponseStatus(final int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public void setErrorMessageKey(final String errorMessageKey) {
        this.errorMessageKey = errorMessageKey;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setStackTrace(final String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof GenericResponse)) {
            return false;
        } else {
            GenericResponse<?> other = (GenericResponse)o;
            return other.canEqual(this);
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof GenericResponse;
    }

    public int hashCode() {
        int result = 1;
        return 1;
    }

    public String toString() {
        return "GenericResponse(pageNumber=" + this.getPageNumber() + ", pageSize=" + this.getPageSize() + ", totalElements=" + this.getTotalElements() + ", message=" + this.getMessage() + ", errors=" + this.getErrors() + ", responseStatus=" + this.getResponseStatus() + ", data=" + this.getData() + ", errorMessageKey=" + this.getErrorMessageKey() + ", errorMessage=" + this.getErrorMessage() + ", stackTrace=" + this.getStackTrace() + ")";
    }

    public GenericResponse() {
    }

    private GenericResponse(final Integer pageNumber, final Integer pageSize, final Long totalElements, final ResponseMessage message, final List<ErrorFields> errors, final int responseStatus, final T data, final String errorMessageKey, final String errorMessage, final String stackTrace) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.message = message;
        this.errors = errors;
        this.responseStatus = responseStatus;
        this.data = data;
        this.errorMessageKey = errorMessageKey;
        this.errorMessage = errorMessage;
        this.stackTrace = stackTrace;
    }

    public static <T> GenericResponse<T> of(final Integer pageNumber, final Integer pageSize, final Long totalElements, final ResponseMessage message, final List<ErrorFields> errors, final int responseStatus, final T data, final String errorMessageKey, final String errorMessage, final String stackTrace) {
        return new GenericResponse<T>(pageNumber, pageSize, totalElements, message, errors, responseStatus, data, errorMessageKey, errorMessage, stackTrace);
    }
}
