package service_generator.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import service_generator.snapshot.ProductSnapshot;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductDaoImpl implements ProductDao {

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    @Transactional
    public Optional<ProductSnapshot> findRandomSnapshot() {
        String sql = """
        SELECT id, price, available_stock
          FROM product
         ORDER BY random()
         LIMIT 1
        """;
        return jdbc.query(sql, (rs, i) -> new ProductSnapshot(
                        rs.getObject("id", UUID.class),
                        rs.getBigDecimal("price"),
                        rs.getInt("available_stock")))
                .stream().findFirst();
    }
}