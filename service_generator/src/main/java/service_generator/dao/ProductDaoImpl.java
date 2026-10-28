package service_generator.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import service_generator.snapshot.ProductSnapshot;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Override
    @Transactional
    public int updatePriceAndStock(UUID id, BigDecimal price, int availableStock, LocalDate lastUpdate) {
        String sql = """
        UPDATE product
           SET price = :price,
               available_stock = :stock,
               last_update_date = :lastUpdate
         WHERE id = :id
        """;
        return jdbc.update(sql, new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("price", price)
                .addValue("stock", availableStock)
                .addValue("lastUpdate", lastUpdate));
    }
}