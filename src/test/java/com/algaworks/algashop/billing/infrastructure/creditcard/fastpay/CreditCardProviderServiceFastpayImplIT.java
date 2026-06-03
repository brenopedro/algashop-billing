package com.algaworks.algashop.billing.infrastructure.creditcard.fastpay;

import com.algaworks.algashop.billing.domain.model.creditcard.LimitedCreditCard;
import com.algaworks.algashop.billing.infrastructure.AbstractFastpayIT;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(FastpayCreditCardTokenizationAPIClientConfig.class)
@ActiveProfiles("test")
class CreditCardProviderServiceFastpayImplIT extends AbstractFastpayIT {

    @Autowired
    private CreditCardProviderServiceFastpayImpl creditCardProvider;

    @BeforeAll
    static void setUp() {
        startMock();
    }

    @AfterAll
    static void tearDown() {
        stopMock();
    }

    @Test
    void shouldCreateCreditCard() {
        LimitedCreditCard limitedCreditCard = registerCard();

        assertThat(limitedCreditCard.getGatewayCode()).isNotBlank();
    }

    @Test
    void shouldFindRegisteredCreditCard() {
        LimitedCreditCard limitedCreditCard = registerCard();

        LimitedCreditCard limitedCreditCardFound = creditCardProvider.findById(limitedCreditCard.getGatewayCode()).orElseThrow();

        assertThat(limitedCreditCardFound.getGatewayCode()).isEqualTo(limitedCreditCard.getGatewayCode());
    }


    @Test
    void shouldDeleteRegisteredCreditCard() {
        LimitedCreditCard limitedCreditCard = registerCard();
        creditCardProvider.delete(limitedCreditCard.getGatewayCode());
    }

}