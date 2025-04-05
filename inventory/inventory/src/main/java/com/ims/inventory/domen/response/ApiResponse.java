package com.ims.inventory.domen.response;

import lombok.Data;

@Data
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private Long totalElements;
    private Integer totalPages;
    private Integer pageNumber;
    private Integer pageSize;

    // Constructor for normal responses
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Constructor for paginated responses
    public ApiResponse(boolean success, String message, T data, Long totalElements,
                       Integer pageNumber, Integer pageSize) {
        this.success = success;
        this.message = message;
        this.data = data; // Ensure safe casting
        this.totalElements = totalElements;
        this.totalPages = Math.toIntExact((totalElements > pageSize ? totalElements / pageSize : 1));
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }
}
