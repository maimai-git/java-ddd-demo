package com.example.application.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;
import java.util.Collections;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MultiResponse<T> extends Response {

    private Collection<T> data;
    private Long total;
    private Integer pageIndex;
    private Integer pageSize;

    public static <T> MultiResponse<T> of(Collection<T> data) {
        MultiResponse<T> r = new MultiResponse<>();
        r.setSuccess(true);
        r.data = data;
        return r;
    }

    public static <T> MultiResponse<T> of(Collection<T> data, long total, int pageIndex, int pageSize) {
        MultiResponse<T> r = of(data);
        r.total = total;
        r.pageIndex = pageIndex;
        r.pageSize = pageSize;
        return r;
    }

    public static <T> MultiResponse<T> ofEmpty() {
        return of(Collections.emptyList());
    }

    public Collection<T> getData() { return data; }
    public void setData(Collection<T> data) { this.data = data; }
    public Long getTotal() { return total; }
    public Integer getPageIndex() { return pageIndex; }
    public Integer getPageSize() { return pageSize; }
}
