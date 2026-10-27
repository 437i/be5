package service_generator.dao;

import service_generator.entity.ProductEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ProductDao {
    public int updatePriceAndStock(UUID id, BigDecimal price, int availableStock, LocalDate lastUpdate);
    public List<ProductEntity> findAll();
}
