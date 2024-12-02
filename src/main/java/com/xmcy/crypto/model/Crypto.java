package com.xmcy.crypto.model;


import javax.validation.constraints.Positive;
import javax.validation.constraints.NotNull;
import jakarta.persistence.*;

@Entity
@Table(name = "CRYPTOS")
public record Crypto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id,
    @NotNull(message = "Timing is required")
    @Positive(message = "Timing must be positive")
    Long timing,
    @NotNull(message = "Name is required")
    String name,
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    Double price,
    String recommendation
) {
    public Crypto() {
        this(null, null, null, null,null);
    }

    public Crypto(
            Long id, Long timing, String name, Double price, String recommendation) {
        this.id = id;
        this.timing = timing;
        this.name = name;
        this.price = price;
        this.recommendation = recommendation;
    }
}