package com.algaworks.algashop.billing.domain.model.invoice;

import com.algaworks.algashop.billing.domain.model.DomainException;
import com.algaworks.algashop.billing.domain.model.IdGenerator;
import lombok.*;
import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PaymentSettings {

    @EqualsAndHashCode.Include
    private UUID id;
    private UUID creditCardId;
    private String gatewayCode;
    private PaymentMethod method;

    static PaymentSettings brandNew(PaymentMethod method, UUID creditCardId) {
        Objects.requireNonNull(method);
        if (method.equals(PaymentMethod.CREDIT_CARD)) {
            Objects.requireNonNull(creditCardId);
        }
        return new PaymentSettings(
                IdGenerator.generateTimeBasedUUID(),
                creditCardId,
                null,
                method
        );
    }

    void assignGatewayCode(String gatewayCode) {
        if (StringUtils.isBlank(gatewayCode))
            throw new IllegalArgumentException("GatewayCode cannot be blank");

        if (this.getGatewayCode() != null)
            throw new DomainException("GatewayCode already exists");

        this.setGatewayCode(gatewayCode);
    }
}
