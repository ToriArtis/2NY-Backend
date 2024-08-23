package com.mega._NY.review.mapper;

import com.mega._NY.auth.entity.User;
import com.mega._NY.item.entity.Item;
import com.mega._NY.review.dto.ReviewDTO;
import com.mega._NY.review.entity.Review;
import com.mega._NY.review.entity.Review.ReviewBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-23T15:52:47+0900",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public ReviewDTO toDTO(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewDTO reviewDTO = new ReviewDTO();

        reviewDTO.setItemId( reviewItemItemId( review ) );
        reviewDTO.setUserId( reviewUserId( review ) );
        reviewDTO.setNickName( reviewUserNickName( review ) );
        List<String> thumbnail = reviewItemThumbnail( review );
        List<String> list = thumbnail;
        if ( list != null ) {
            reviewDTO.setThumbnail( new ArrayList<String>( list ) );
        }
        reviewDTO.setReviewId( review.getReviewId() );
        reviewDTO.setOrderId( review.getOrderId() );
        reviewDTO.setStar( review.getStar() );
        reviewDTO.setContent( review.getContent() );
        reviewDTO.setCreatedAt( review.getCreatedAt() );
        reviewDTO.setUpdatedAt( review.getUpdatedAt() );

        return reviewDTO;
    }

    @Override
    public Review toEntity(ReviewDTO reviewDTO) {
        if ( reviewDTO == null ) {
            return null;
        }

        ReviewBuilder review = Review.builder();

        review.reviewId( reviewDTO.getReviewId() );
        review.orderId( reviewDTO.getOrderId() );
        review.content( reviewDTO.getContent() );
        review.star( reviewDTO.getStar() );
        review.createdAt( reviewDTO.getCreatedAt() );
        review.updatedAt( reviewDTO.getUpdatedAt() );

        return review.build();
    }

    private Long reviewItemItemId(Review review) {
        if ( review == null ) {
            return null;
        }
        Item item = review.getItem();
        if ( item == null ) {
            return null;
        }
        Long itemId = item.getItemId();
        if ( itemId == null ) {
            return null;
        }
        return itemId;
    }

    private Long reviewUserId(Review review) {
        if ( review == null ) {
            return null;
        }
        User user = review.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String reviewUserNickName(Review review) {
        if ( review == null ) {
            return null;
        }
        User user = review.getUser();
        if ( user == null ) {
            return null;
        }
        String nickName = user.getNickName();
        if ( nickName == null ) {
            return null;
        }
        return nickName;
    }

    private List<String> reviewItemThumbnail(Review review) {
        if ( review == null ) {
            return null;
        }
        Item item = review.getItem();
        if ( item == null ) {
            return null;
        }
        List<String> thumbnail = item.getThumbnail();
        if ( thumbnail == null ) {
            return null;
        }
        return thumbnail;
    }
}
