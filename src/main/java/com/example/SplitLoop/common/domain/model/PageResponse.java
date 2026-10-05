package com.example.SplitLoop.common.domain.model;

import lombok.Builder;

import java.util.List;

@Builder
public record PageResponse<T>(

        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last,
        boolean first
){

}