package service_generator.event_generator;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import service_generator.dto.ProductAttributeChangedEvent;
import service_generator.entity.ProductEntity;
//import service_generator.repository.ProductRepository;
import service_generator.service.ProductServiceImpl;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.List;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class ProductEventGenerator {

    private static final String TOPIC = "product-attributes-changed";

    private final ProductServiceImpl productServiceImpl; // ваш репозиторий
    //    private final ProductEntity productEntity;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Random random = new Random();

    @Scheduled(fixedDelay = 10000) // каждые 10 секунд после завершения предыдущего
    public void generateEvent() {
        // 1. Берем случайный товар (упрощенно — через список ID или random offset в БД)
        List<ProductEntity> products = productServiceImpl.findAll();
        if (products.isEmpty()) return;

        ProductEntity productEntity = products.get(random.nextInt(products.size()));

        // 2. Мутируем атрибуты
        BigDecimal newPrice = productEntity.getPrice().multiply(BigDecimal.valueOf(0.9 + random.nextDouble() * 0.2)); // ±10%

//        int idx = ThreadLocalRandom.current().nextInt(products.size());
//        int newStock = ThreadLocalRandom.current().nextInt(100);
//        double factor = 0.9 + ThreadLocalRandom.current().nextDouble() * 0.2;

        int newStock = random.nextInt(100); // 0-99

        productEntity.setPrice(newPrice);
        productEntity.setAvailableStock(newStock);
//        productRepository.save(productEntity);

        int updated = productServiceImpl.updatePriceAndStock(
                productEntity.getId(), newPrice, newStock, LocalDate.now(ZoneOffset.UTC));
        if (updated == 0) {
            return;
        }

        // 3. Публикуем событие
//        ProductAttributeChangedEvent event = new ProductAttributeChangedEvent(
//                productEntity.getId(), newPrice, newStock, Instant.now()
//        );

        ProductAttributeChangedEvent event = new ProductAttributeChangedEvent(
                UUID.randomUUID(),          // event_id
                productEntity.getId(),            // product_id
                newPrice,                   // price (BigDecimal → строка "1049.90")
                newStock,                   // available_stock
                Instant.now()               // occurred_at (UTC, RFC 3339)
        );

        kafkaTemplate.send(TOPIC, productEntity.getId().toString(), event);
    }
}


//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class ProductEventGenerator {
//
//    private final ProductRepository productRepository;
//    private final KafkaTemplate<String, Object> kafkaTemplate;
//
//    @Scheduled(fixedDelay = 10_000)
//    public void generateEvent() {
//        List<ProductEntity> products = productRepository.findAll();
//        if (products.isEmpty()) {
//            log.debug("No products to generate event for");
//            return;
//        }
//
//        ProductEntity product = products.get(
//                ThreadLocalRandom.current().nextInt(products.size()));
//
//        BigDecimal newPrice = product.getPrice()
//                .multiply(BigDecimal.valueOf(0.9 + ThreadLocalRandom.current().nextDouble() * 0.2))
//                .setScale(2, RoundingMode.HALF_UP);
//
//        int newStock = ThreadLocalRandom.current().nextInt(100);
//
//        product.setPrice(newPrice);
//        product.setAvailableStock(newStock);
//        productRepository.save(product);
//
//        ProductAttributeChangedEvent event = new ProductAttributeChangedEvent(
//                product.getId(), newPrice, newStock, Instant.now());
//
//        kafkaTemplate.send("product-attributes-changed", product.getId().toString(), event)
//                .whenComplete((result, ex) -> {
//                    if (ex != null) {
//                        log.error("Failed to send event for product {}", product.getId(), ex);
//                    } else {
//                        log.info("Sent event for product {}: price={}, stock={}",
//                                product.getId(), newPrice, newStock);
//                    }
//                });
//    }
//}