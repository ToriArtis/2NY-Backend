package com.mega._NY.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {

    private Long reviewId;
    private Long itemId;
    private Long userId;
    private String nickName;
    private double star;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
