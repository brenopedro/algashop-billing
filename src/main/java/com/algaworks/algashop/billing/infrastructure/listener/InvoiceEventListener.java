package com.algaworks.algashop.billing.infrastructure.listener;

import com.algaworks.algashop.billing.domain.model.invoice.InvoiceCanceledEvent;
import com.algaworks.algashop.billing.domain.model.invoice.InvoiceIssuedEvent;
import com.algaworks.algashop.billing.domain.model.invoice.InvoicePaidEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InvoiceEventListener {

    @EventListener
    public void listen(InvoiceIssuedEvent event) {
        log.info("Received InvoiceIssuedEvent: {}", event);
    }

    @EventListener
    public void listen(InvoicePaidEvent event) {
        log.info("Received InvoicePaidEvent: {}", event);
    }

    @EventListener
    public void listen(InvoiceCanceledEvent event) {
        log.info("Received InvoiceCanceledEvent: {}", event);
    }
}
