package com.airbnb.common.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * @author mir00r on 21/6/21
 * @project IntelliJ IDEA
 */
public class PageAttr {
    private PageAttr() {
    }

    public static int PAGE_SIZE = 10;

    public static String SORT_BY_FIELD_ID = "id";

    public static PageRequest getPageRequest(int page) {
        return PageRequest.of(page, PageAttr.PAGE_SIZE, Sort.Direction.DESC, PageAttr.SORT_BY_FIELD_ID);
    }

    public static PageRequest getPageRequest(int page, int size) {
        if (size <= 0) size = 10;
        return PageRequest.of(page, size, Sort.Direction.DESC, PageAttr.SORT_BY_FIELD_ID);
    }

    public static PageRequest getPageRequest(int page, int size, String sortBy, Sort.Direction direction) {
        if (size <= 0) size = 10;
        return PageRequest.of(page, size, direction, sortBy);
    }

    public static PageRequest getPageRequest(int page, int size, Sort.Direction direction, String... sortBy) {
        if (size <= 0) size = 10;
        return PageRequest.of(page, size, direction, sortBy);
    }

    public static PageRequest getPageRequest(PageableParams pageableParams) {
        if (pageableParams.getSize() <= 0) pageableParams.setSize(10);
        return PageRequest.of(
                pageableParams.getPage(),
                pageableParams.getSize(),
                pageableParams.getDirection(),
                pageableParams.getSortBy().getLabel()
        );
    }
}
