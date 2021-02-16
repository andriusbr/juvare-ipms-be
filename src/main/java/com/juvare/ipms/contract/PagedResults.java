package com.juvare.ipms.contract;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PagedResults<T> {

    private long totalElements;
    private long numberOfElements;
    private List<T> content;

    public PagedResults(Page<T> page){
        this.totalElements = page.getTotalElements();
        this.numberOfElements = page.getNumberOfElements();
        this.content = page.getContent();

    }
}
