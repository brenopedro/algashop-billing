package com.algaworks.algashop.billing.domain.model.creditcard;

import java.util.UUID;

public class CreditCardTestDataBuilder {

    private UUID customerId =  UUID.randomUUID();
    private String lasNumbers = "1234";
    private String brand = "Visa";
    private Integer expMonth = 12;
    private Integer expYear = 2025;
    private String gatewayCreditCardCode = "12345";

    public CreditCardTestDataBuilder() {

    }

    public static CreditCardTestDataBuilder aCreditCard() {
        return new CreditCardTestDataBuilder();
    }

    public CreditCard build() {
        return CreditCard.brandNew(customerId, lasNumbers, brand, expMonth, expYear, gatewayCreditCardCode);
    }

    public CreditCardTestDataBuilder customerId(UUID customerId) {
        this.customerId = customerId;
        return this;
    }

    public CreditCardTestDataBuilder lasNumbers(String lasNumbers) {
        this.lasNumbers = lasNumbers;
        return this;
    }

    public CreditCardTestDataBuilder brand(String brand) {
        this.brand = brand;
        return this;
    }

    public CreditCardTestDataBuilder expMonth(Integer expMonth) {
        this.expMonth = expMonth;
        return this;
    }

    public CreditCardTestDataBuilder expYear(Integer expYear) {
        this.expYear = expYear;
        return this;
    }

    public CreditCardTestDataBuilder gatewayCreditCardCode(String gatewayCreditCardCode) {
        this.gatewayCreditCardCode = gatewayCreditCardCode;
        return this;
    }
}
