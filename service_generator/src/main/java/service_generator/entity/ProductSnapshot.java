package service_generator.entity;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductSnapshot(UUID id, BigDecimal price, int availableStock) {}