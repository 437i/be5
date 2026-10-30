package service_generator.dao;

import service_generator.snapshot.ProductSnapshot;
import java.util.Optional;

public interface ProductDao {
    Optional<ProductSnapshot> findRandomSnapshot();
}