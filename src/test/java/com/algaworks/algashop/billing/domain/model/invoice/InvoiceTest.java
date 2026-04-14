package com.algaworks.algashop.billing.domain.model.invoice;

import com.algaworks.algashop.billing.domain.model.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class InvoiceTest {

    @Test
    public void shouldMarkInvoiceAsPaidWhenUnpaid() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().build();
        invoice.changePaymentSettings(PaymentMethod.GATEWAY_BALANCE, null);

        invoice.markAsPaid();

        assertWith(invoice,
                i -> assertThat(i.isPaid()).isTrue(),
                i -> assertThat(i.getPaidAt()).isNotNull()
        );
    }

    @Test
    public void shouldCancelInvoiceWithReason() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().build();
        String cancelReason = "Customer requested cancellation";

        invoice.cancel(cancelReason);

        assertWith(invoice,
                i -> assertThat(i.isCanceled()).isTrue(),
                i -> assertThat(i.getCanceledAt()).isNotNull(),
                i -> assertThat(i.getCancelReason()).isEqualTo(cancelReason)
        );
    }

    @Test
    public void shouldIssueInvoiceCorrectly() {
        String orderId = "123";
        UUID customerId = UUID.randomUUID();
        Payer payer = InvoiceTestDataBuilder.aPayer();
        Set<LineItem> items = new HashSet<>();
        items.add(InvoiceTestDataBuilder.aLineItem());
        items.add(InvoiceTestDataBuilder.aLineItemAlt());

        Invoice invoice = Invoice.issue(orderId, customerId, payer, items);

        BigDecimal expectedTotalAmount = invoice.getItems()
                .stream()
                .map(LineItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertWith(invoice,
                i -> assertThat(i.getId()).isNotNull(),
                i -> assertThat(i.getTotalAmount()).isEqualTo(expectedTotalAmount),
                i -> assertThat(i.getStatus()).isEqualTo(InvoiceStatus.UNPAID)
        );
    }

    @Test
    public void shouldThrowExceptionWhenIssuingInvoiceWithEmptyItems() {
        Set<LineItem> emptyItems = new HashSet<>();
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Invoice.issue("01226N0693HDA23",
                        UUID.randomUUID(),
                        InvoiceTestDataBuilder.aPayer(),
                        emptyItems));
    }

    @Test
    public void shouldChangePaymentSettingsWhenUnpaid() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().build();
        UUID creditCardId = UUID.randomUUID();
        invoice.changePaymentSettings(PaymentMethod.CREDIT_CARD, creditCardId);
        assertWith(invoice,
                i -> assertThat(i.getPaymentSettings()).isNotNull(),
                i -> assertThat(i.getPaymentSettings().getMethod()).isEqualTo(PaymentMethod.CREDIT_CARD),
                i -> assertThat(i.getPaymentSettings().getCreditCardId()).isEqualTo(creditCardId));
    }

    @Test
    public void shouldThrowExceptionWhenChangingPaymentSettingsToPaidInvoice() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().status(InvoiceStatus.PAID).build();
        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> invoice.changePaymentSettings(PaymentMethod.CREDIT_CARD, UUID.randomUUID()));
    }



    @Test
    public void shouldThrowExceptionWhenMarkingCanceledInvoiceAsPaid() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().status(InvoiceStatus.CANCELED).build();

        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(invoice::markAsPaid);
    }

    @Test
    public void shouldThrowExceptionWhenCancelingAlreadyCanceledInvoice() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().status(InvoiceStatus.CANCELED).build();

        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> invoice.cancel("Another reason"));
    }

    @Test
    public void shouldAssignPaymentGatewayCodeWhenUnpaid() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().paymentSettings(PaymentMethod.CREDIT_CARD, UUID.randomUUID()).build();
        String gatewayCode = "code-from-gateway";

        invoice.assignPaymentGatewayCode(gatewayCode);

        assertThat(invoice.getPaymentSettings().getGatewayCode()).isEqualTo(gatewayCode);
    }

    @Test
    public void shouldThrowExceptionWhenAssigningGatewayCodeToPaidInvoice() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().status(InvoiceStatus.PAID).build();
        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> invoice.assignPaymentGatewayCode("some-code"));
    }

    @Test
    public void shouldThrowExceptionWhenTryingToModifyItemsSet() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().build();
        Set<LineItem> items = invoice.getItems();

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(items::clear);
    }
}