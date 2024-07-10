package com.mega._NY.item.repository;

import com.mega._NY.item.entity.Item;
import com.mega._NY.item.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SearchRepository {

    // QueryDSL을 사용하기 위한 쿼리 팩토리
    private final JPAQueryFactory queryFactory;

    public SearchRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<Item> searchItems(String[] types, String keyword, Pageable pageable) {

        // Qdomain 객체 생성
        QItem item = QItem.item;

        // 기본 쿼리 생성
        JPAQuery<Item> query = queryFactory.selectFrom(item);

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        // 검색 조건이 있는 경우
        if (types != null && types.length > 0 && keyword != null && !keyword.isEmpty()) {
            for (String type : types) {
                switch (type) {
                    case "c": // 내용 검색
                        booleanBuilder.or(item.content.contains(keyword));
                        break;
                    case "t": // 제목 검색
                        booleanBuilder.or(item.title.contains(keyword));
                        break;
                }
            }
            // 생성된 조건을 쿼리에 적용
            query.where(booleanBuilder);
        }

        // 총 결과 수 조회
        long totalCount = query.fetchCount();

        // 페이징 적용하여 결과 조회
        List<Item> content = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Page 객체 생성 및 반환
        return new PageImpl<>(content, pageable, totalCount);
    }
}