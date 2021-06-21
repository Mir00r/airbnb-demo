package com.airbnb.common.utils;

import org.springframework.data.domain.Sort;

import java.util.Locale;

/**
 * @author mir00r on 21/6/21
 * @project IntelliJ IDEA
 */
public class PageableParams {
    private final String query;
    private final int page;
    private int size;
    private final SortByFields sortBy;
    private final Sort.Direction direction;

    private PageableParams(String query, int page, int size, SortByFields sortBy, Sort.Direction direction) {
        this.query = query;
        this.page = page;
        this.size = size;
        this.sortBy = sortBy;
        this.direction = direction;
    }

    public static PageableParams of(String query, int page, int size, SortByFields sortBy, Sort.Direction direction) {
        return new PageableParams(query, page, size, sortBy, direction);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getQuery() {
        if (this.query == null) return null;
        return this.query.toLowerCase(Locale.getDefault());
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public SortByFields getSortBy() {
        return sortBy;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    enum SortByFields {
        ID("id"), CREATED_AT("createdAt");

        String label;

        SortByFields(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
}
