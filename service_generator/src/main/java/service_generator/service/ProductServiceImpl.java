package service_generator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service_generator.dao.ProductDaoImpl;
import service_generator.entity.ProductEntity;
//import service_generator.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

//    private final ProductRepository productRepository;
    private final ProductDaoImpl productDaoImpl;
//    private final ProductEntity productEntity;

    @Override
    @Transactional
    public int updatePriceAndStock(UUID id, BigDecimal price, int availableStock, LocalDate lastUpdate) {
        return productDaoImpl.updatePriceAndStock(id, price, availableStock, lastUpdate);
    }

    @Override
    @Transactional
    public List<ProductEntity> findAll() {
        return productDaoImpl.findAll();
    }


//    @Transactional
//    public ProductEntity getById(UUID id) {
//        Optional<ProductEntity> productEntity;
//        productEntity = productRepository.findById(id);
//        return productEntity.get();
//    }

//    public List<ProductResponse> getAllAvailable() {
//        return productRepository.findAllAvailable()
//                .stream().map(productMapper::toResponse).toList(); // пустой список
//    }

//    @Transactional
//    public void decreaseStock(UUID id, int amount) {
//        if (!productRepository.existsById(id)) {
//            throw new ResourceNotFoundException("Product with id " + id + " not found");
//        }
//        Integer currentStock = productDao.getStock(id);
//        if (currentStock == null || currentStock < amount) {
//            throw new IllegalArgumentException("Not enough stock. Available: " + currentStock);
//        }
//        productRepository.decreaseStock(id, amount);
//    }

//    public void delete(UUID id) {
//        if (!productRepository.existsById(id)) {
//            throw new ResourceNotFoundException("Product with id " + id + " not found");
//        }
//        productRepository.delete(id);
//    }
}