package com.mega._NY.review.mapper;

import com.mega._NY.review.dto.ReviewDTO;
import com.mega._NY.review.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "itemId", source = "item.itemId")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "nickName", source = "user.nickName")
    @Mapping(target = "thumbnail", source = "item.thumbnail")
    ReviewDTO toDTO(Review review);

    @Mapping(target = "item", ignore = true)
    @Mapping(target = "user", ignore = true)
    Review toEntity(ReviewDTO reviewDTO);
}