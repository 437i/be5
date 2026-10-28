package service_generator.event_generator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import service_generator.dto.ProductAttributeChangedEvent;
//import service_generator.entity.ProductEntity;
import service_generator.entity.ProductSnapshot;
import service_generator.service.ProductServiceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductEventGenerator {

    private static final String TOPIC = "product-attributes-changed";

    private final ProductServiceImpl productServiceImpl;
    private final KafkaTemplate<String, Object> kafkaTemplate;
//    private final Random random = new Random();

    @Scheduled(fixedDelay = 10000) // каждые 10 секунд после завершения предыдущего
    public void generateEvent() {

        // (через список ID или random offset в БД)
        // List<ProductEntity> products = productServiceImpl.findAll();
        // if (products.isEmpty()) return;

        // ProductEntity productEntity = products.get(random.nextInt(products.size()));

        Optional<ProductSnapshot> randomProduct = productServiceImpl.findRandomSnapshot();
        if (randomProduct.isEmpty()) {
            log.debug("No products in DB — skipping event generation");
            return;
        }
        ProductSnapshot productSnapshot = randomProduct.get();
        UUID productId = productSnapshot.id();

//        BigDecimal newPrice = productSnapshot.price().multiply(BigDecimal.valueOf(0.9 + random.nextDouble() * 0.2)); // ±10%

        BigDecimal newPrice = productSnapshot.price()
                .multiply(BigDecimal.valueOf(0.9 + ThreadLocalRandom.current().nextDouble() * 0.2))
                .setScale(2, RoundingMode.HALF_UP);

//        int idx = ThreadLocalRandom.current().nextInt(products.size());
//        int newStock = ThreadLocalRandom.current().nextInt(100);
//        double factor = 0.9 + ThreadLocalRandom.current().nextDouble() * 0.2;

//        int newStock = random.nextInt(100); // 0-99
        int newStock = ThreadLocalRandom.current().nextInt(100); // 0..99

//        productSnapshot.price(newPrice);
//        productSnapshot.availableStock(newStock);

        int updated = productServiceImpl.updatePriceAndStock(
                productId, newPrice, newStock, LocalDate.now(ZoneOffset.UTC));
        if (updated == 0) {
            log.debug("Product {} disappeared before update — skipping event", productId);
            return;
        }

        ProductAttributeChangedEvent event = new ProductAttributeChangedEvent(
                UUID.randomUUID(),
                productId,
                newPrice,
                newStock,
                Instant.now() // occurred_at (UTC, RFC 3339)
        );

//        kafkaTemplate.send(TOPIC, productId.toString(), event);

        kafkaTemplate.send(TOPIC, productId.toString(), event)
                .whenComplete((res, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send event for product {}", productId, ex);
                    } else {
                        log.info("Event sent: product={}, price={}, stock={}",
                                productId, newPrice, newStock);
                    }
                });
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