package com.mega._NY.item.entity;



import com.mega._NY.item.repository.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class TestDataInitializer {

    @Bean
    public CommandLineRunner initTestData(ItemRepository itemRepository) {
        return args -> {
            // 데이터베이스가 비어있을 때만 테스트 데이터 생성
            if (itemRepository.count() == 0) {
                Random random = new Random();
                List<Item> testItems = new ArrayList<>();

                // 카테고리별 10개씩 생성
                for (ItemCategory category : ItemCategory.values()) {
                    for (int i = 0; i < 10; i++) {
                        Item item = createTestItem(random, category, null);
                        testItems.add(item);
                    }
                }

                // 남은 10개는 랜덤 카테고리로 생성 (색상별로 각각 1개씩)
                for (ItemColor color : ItemColor.values()) {
                    Item item = createTestItem(random, null, color);
                    testItems.add(item);
                }

                itemRepository.saveAll(testItems);
                System.out.println("Test data initialized with " + testItems.size() + " items.");
            } else {
                System.out.println("Database is not empty. Skipping test data initialization.");
            }
        };
    }

    private Item createTestItem(Random random, ItemCategory category, ItemColor specificColor) {
        int price = random.nextInt(1000) * 100; // 10000 ~ 109999 사이의 랜덤 가격
        int discountRate = random.nextInt(40); // 0 ~ 49% 사이의 랜덤 할인율
        int discountPrice = price * (100 - discountRate) / 100;

        ItemCategory itemCategory = (category != null) ? category : ItemCategory.values()[random.nextInt(ItemCategory.values().length)];
        ItemColor itemColor = (specificColor != null) ? specificColor : ItemColor.values()[random.nextInt(ItemColor.values().length)];

        return Item.builder()
                .title("Test Item " + (random.nextInt(1000) + 1))
                .content("This is a test item in category " + itemCategory.name() + " with color " + itemColor.name())
                .thumbnail(List.of("default_descriptionImage.png"))
                .descriptionImage(List.of("default_descriptionImage.png"))
                .price(price)
                .discountRate(discountRate)
                .discountPrice(discountPrice)
                .sales(random.nextInt(100))
                .size(ItemSize.values()[random.nextInt(ItemSize.values().length)])
                .color(itemColor)
                .category(itemCategory)
                .build();
    }
}
