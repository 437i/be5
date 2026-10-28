package service_generator.dao;

//import service_generator.entity.ProductEntity;
import service_generator.entity.ProductSnapshot;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface ProductDao {
//    Optional<ProductEntity> findRandom();
    Optional<ProductSnapshot> findRandomSnapshot();
    int updatePriceAndStock(UUID id, BigDecimal price, int availableStock, LocalDate lastUpdate);
//    public List<ProductEntity> findAll();
}
