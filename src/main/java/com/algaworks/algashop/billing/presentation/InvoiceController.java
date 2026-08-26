package com.algaworks.algashop.billing.presentation;

import com.algaworks.algashop.billing.application.invoice.management.GenerateInvoiceInput;
import com.algaworks.algashop.billing.application.invoice.management.InvoiceManagementApplicationService;
import com.algaworks.algashop.billing.application.invoice.query.InvoiceOutput;
import com.algaworks.algashop.billing.application.invoice.query.InvoiceQueryService;
import com.algaworks.algashop.billing.infrastructure.security.SecurityAnnotations;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders/{orderId}/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceQueryService invoiceQueryService;
    private final InvoiceManagementApplicationService invoiceManagementApplicationService;

    @GetMapping
    @SecurityAnnotations.CanReadInvoices
    public InvoiceOutput findByOrder(@PathVariable String orderId) {
        return invoiceQueryService.findByOrderId(orderId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityAnnotations.CanWriteInvoices
    public InvoiceOutput generate(@PathVariable String orderId,
                                  @RequestBody @Valid GenerateInvoiceInput input) {
        input.setOrderId(orderId);
        UUID invoiceId = invoiceManagementApplicationService.generate(input);
        try {
            invoiceManagementApplicationService.processPayment(invoiceId);
        } catch (Exception e) {
            log.error("Error when processing payment for invoice {}", invoiceId, e);
        }
        return invoiceQueryService.findByOrderId(orderId);
    }
}
