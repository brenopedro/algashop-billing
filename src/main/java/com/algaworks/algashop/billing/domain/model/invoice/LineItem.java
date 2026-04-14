package com.algaworks.algashop.billing.domain.model.invoice;

import com.algaworks.algashop.billing.domain.model.FieldValidations;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter(AccessLevel.PRIVATE)
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class LineItem {

    private Integer number;
    private String name;
    private BigDecimal amount;

    @Builder
    public LineItem(Integer number, String name, BigDecimal amount) {
        FieldValidations.requiresNonBlank(name);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(number);

        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be greater than zero");

        if (number <= 0)
            throw new IllegalArgumentException("Number must be greater than zero");
        this.number = number;
        this.name = name;
        this.amount = amount;
    }
}
