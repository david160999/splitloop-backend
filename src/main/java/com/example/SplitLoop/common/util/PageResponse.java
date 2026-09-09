package com.example.SplitLoop.common.util;

import lombok.Builder;

import java.util.List;

@Builder
public class PageResponse<T> {

    private List<T> content;

    private int page;

    private int size;

    private long totalElements;

    private int totalPages;

    private boolean first;

    private boolean last;
}