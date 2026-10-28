package service_generator.snapshot;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductSnapshot(UUID id, BigDecimal price, int availableStock) {}