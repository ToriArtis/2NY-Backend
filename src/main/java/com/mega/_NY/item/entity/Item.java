package com.mega._NY.item.entity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Item {

    @Id
    @Column(name = "ITEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 칼럼 추가시 자동 증가
    private Long itemId; // pk 상품Id

    @Column
    private String title; // 제목

    @Column
    private String content; // 내용

    @Column
    private String thumbnail; // 썸네일 이미지 저장 주소

    @Column
    private String descriptionImage; // 제품 상세이미지 저장 주소

    @Column
    private int price; // 가격

    @Column
    private int discountPrice; // 할인된 가격

    @Column
    private int discountRate; // 할인율 %

    @Column
    private int sales; // 상품 판매된 횟수

    @Column
    private int size; // 옷 사이즈

    @Column
    private int color; // 옷 색상

    @Column
    private LocalDateTime createdAt; // 최초 생성 시간

    @Column
    private LocalDateTime updatedAt; // 마지막 변경 시간


}