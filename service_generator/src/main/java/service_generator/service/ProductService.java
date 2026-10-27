package service_generator.service;

import service_generator.entity.ProductEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    public int updatePriceAndStock(UUID id, BigDecimal price, int availableStock, LocalDate lastUpdate);
    public List<ProductEntity> findAll();
}
