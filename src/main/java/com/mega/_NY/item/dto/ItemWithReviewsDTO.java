package com.mega._NY.item.dto;

import com.mega._NY.review.dto.ReviewDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemWithReviewsDTO {
    private ItemDTO item;
    private Page<ReviewDTO> reviews;
}
