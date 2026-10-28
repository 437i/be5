package service_generator.dao;

import service_generator.snapshot.ProductSnapshot;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface ProductDao {
    Optional<ProductSnapshot> findRandomSnapshot();
    int updatePriceAndStock(UUID id, BigDecimal price, int availableStock, LocalDate lastUpdate);
}