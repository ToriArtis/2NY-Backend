package com.mega._NY.review.entity;

import com.mega._NY.auth.entity.User;
import com.mega._NY.item.entity.Item;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ITEM_ID")
    private Item item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="USER_ID")
    private User user;

    @Column
    private String content;

    //별점
    @Column
    private double star;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;
}
