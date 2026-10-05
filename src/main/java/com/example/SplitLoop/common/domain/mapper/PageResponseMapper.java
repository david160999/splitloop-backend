package com.example.SplitLoop.common.domain.mapper;

import com.example.SplitLoop.common.domain.model.PageResponse;
import org.springframework.data.domain.Page;

import java.util.function.Function;

public final class PageResponseMapper {

    public static <T, R> PageResponse<R> map(
            Page<T> page,
            Function<T, R> mapper) {

        return PageResponse.<R>builder()
                .content(page.getContent().stream().map(mapper).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
