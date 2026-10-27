package service_generator.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import service_generator.entity.ProductEntity;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductDaoImpl implements ProductDao {

    private final NamedParameterJdbcTemplate jdbc;

    private ProductEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return ProductEntity.builder()
                .id(resultSet.getObject("id", UUID.class))
                .name(resultSet.getString("name"))
                .category(resultSet.getString("category"))
                .price(resultSet.getBigDecimal("price"))
                .availableStock(resultSet.getInt("available_stock"))
                .lastUpdateDate(resultSet.getDate("last_update_date") != null
                        ? resultSet.getDate("last_update_date").toLocalDate() : null)
                .supplierId(resultSet.getObject("supplier_id", UUID.class))
                .imageId(resultSet.getObject("image_id", UUID.class))
                .build();
    }

//    public UUID insert(ProductEntity product) {
//        UUID id = UUID.randomUUID();
//        String sql = """
//            INSERT INTO product (id, name, category, price, available_stock,
//                                 last_update_date, supplier_id, image_id)
//            VALUES (:id, :name, :category, :price, :stock, :lastUpdate, :supplierId, :imageId)
//            """;
//        jdbc.update(sql, new MapSqlParameterSource()
//                .addValue("id", id)
//                .addValue("name", product.getName())
//                .addValue("category", product.getCategory())
//                .addValue("price", product.getPrice())
//                .addValue("stock", product.getAvailableStock())
//                .addValue("lastUpdate", product.getLastUpdateDate())
//                .addValue("supplierId", product.getSupplierId())
//                .addValue("imageId", product.getImageId()));
//        return id;
//    }

    @Override
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

//    public Optional<ProductEntity> findById(UUID id) {
//        String sql = "SELECT * FROM product WHERE id = :id";
//        return jdbc.query(sql, new MapSqlParameterSource("id", id), this::mapRow)
//                .stream().findFirst();
//    }

    @Override
    public List<ProductEntity> findAll() {
        String sql = "SELECT * FROM product";
        return jdbc.query(sql, this::mapRow);
    }

//    public List<ProductEntity> findAllAvailable() {
//        String sql = "SELECT * FROM product WHERE available_stock > 0";
//        return jdbc.query(sql, this::mapRow);
//    }

//    public void decreaseStock(UUID id, int amount) {
//        String sql = "UPDATE product SET available_stock = available_stock - :amount WHERE id = :id";
//        jdbc.update(sql, new MapSqlParameterSource()
//                .addValue("id", id)
//                .addValue("amount", amount));
//    }

//    public void delete(UUID id) {
//        jdbc.update("DELETE FROM product WHERE id = :id",
//                new MapSqlParameterSource("id", id));
//    }

//    public boolean existsById(UUID id) {
//        Integer count = jdbc.queryForObject(
//                "SELECT COUNT(*) FROM product WHERE id = :id",
//                new MapSqlParameterSource("id", id), Integer.class);
//        return count != null && count > 0;
//    }

//    public Integer getStock(UUID id) {
//        return jdbc.queryForObject(
//                "SELECT available_stock FROM product WHERE id = :id",
//                new MapSqlParameterSource("id", id), Integer.class);
//    }

//    public void updateImageId(UUID productId, UUID imageId) {
//        String sql = "UPDATE product SET image_id = :imageId WHERE id = :productId";
//        jdbc.update(sql, new MapSqlParameterSource()
//                .addValue("productId", productId)
//                .addValue("imageId", imageId));
//    }
}