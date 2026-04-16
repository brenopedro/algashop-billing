package com.algaworks.algashop.billing.application.invoice.query;

import com.algaworks.algashop.billing.domain.model.invoice.Invoice;
import com.algaworks.algashop.billing.domain.model.invoice.InvoiceRepository;
import com.algaworks.algashop.billing.domain.model.invoice.InvoiceTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class InvoiceQueryServiceIT {

    @Autowired
    private InvoiceQueryService invoiceQueryService;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    void shouldFindByOrderId() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().build();
        invoiceRepository.saveAndFlush(invoice);

        InvoiceOutput invoiceOutput = invoiceQueryService.findByOrderId(invoice.getOrderId());

        assertThat(invoiceOutput.getOrderId()).isEqualTo(invoice.getOrderId());

    }
}