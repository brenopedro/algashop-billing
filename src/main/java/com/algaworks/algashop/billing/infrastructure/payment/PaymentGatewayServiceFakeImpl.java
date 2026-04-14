package com.algaworks.algashop.billing.infrastructure.payment;

import com.algaworks.algashop.billing.domain.model.invoice.PaymentMethod;
import com.algaworks.algashop.billing.domain.model.invoice.payment.Payment;
import com.algaworks.algashop.billing.domain.model.invoice.payment.PaymentGatewayService;
import com.algaworks.algashop.billing.domain.model.invoice.payment.PaymentRequest;
import com.algaworks.algashop.billing.domain.model.invoice.payment.PaymentStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentGatewayServiceFakeImpl implements PaymentGatewayService {

    @Override
    public Payment capture(PaymentRequest request) {
        return Payment.builder()
                .gatewayCode(UUID.randomUUID().toString())
                .invoiceId(request.getInvoiceId())
                .method(request.getMethod())
                .status(PaymentStatus.PAID)
                .build();
    }

    @Override
    public Payment findByCode(String gatewayCode) {
        return Payment.builder()
                .gatewayCode(UUID.randomUUID().toString())
                .invoiceId(UUID.randomUUID())
                .method(PaymentMethod.GATEWAY_BALANCE)
                .status(PaymentStatus.PAID)
                .build();
    }
}
