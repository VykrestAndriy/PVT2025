package com.example.lb_7.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookTitleAuthorDTO {
    private String title;
    private String authorName;

    public BookTitleAuthorDTO(String title, String authorName) {
        this.title = title;
        this.authorName = authorName;
    }
}