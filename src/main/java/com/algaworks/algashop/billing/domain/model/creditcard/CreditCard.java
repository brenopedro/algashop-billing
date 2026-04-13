package com.algaworks.algashop.billing.domain.model.creditcard;

import com.algaworks.algashop.billing.domain.model.IdGenerator;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CreditCard {

    @EqualsAndHashCode.Include
    private UUID id;
    private OffsetDateTime createAt;
    private UUID customerId;
    private String lastNumbers;
    private String brand;
    private Integer expMonth;
    private Integer expYear;
    private String gatewayCode;

    public static CreditCard brandNew(UUID customerId, String lastNumbers, String brand,
                                      Integer expMonth, Integer expYear, String gatewayCreditCardCode) {
        Objects.requireNonNull(customerId);
        Objects.requireNonNull(expMonth);
        Objects.requireNonNull(expYear);

        if (StringUtils.isAnyBlank(lastNumbers, brand, gatewayCreditCardCode))
            throw new IllegalArgumentException("Last numbers, brand and gateway credit card code cannot be blank");

        return new CreditCard(
                IdGenerator.generateTimeBasedUUID(),
                OffsetDateTime.now(),
                customerId,
                lastNumbers,
                brand,
                expMonth,
                expYear,
                gatewayCreditCardCode
        );
    }

    public void setGatewayCode(String gatewayCode) {
        if (StringUtils.isBlank(this.gatewayCode)) throw new IllegalArgumentException("Gateway code cannot be blank");
        this.gatewayCode = gatewayCode;
    }
}
