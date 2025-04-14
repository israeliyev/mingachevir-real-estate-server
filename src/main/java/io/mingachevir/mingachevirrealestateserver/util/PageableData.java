package io.mingachevir.mingachevirrealestateserver.util;

public class PageableData<T> {
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private T data;

    public Integer getPageNumber() {
        return this.pageNumber;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public Long getTotalElements() {
        return this.totalElements;
    }

    public T getData() {
        return this.data;
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

    public void setData(final T data) {
        this.data = data;
    }

    public PageableData(final Integer pageNumber, final Integer pageSize, final Long totalElements, final T data) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.data = data;
    }
}
