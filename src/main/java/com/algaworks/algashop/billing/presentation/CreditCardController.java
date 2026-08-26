package com.algaworks.algashop.billing.presentation;

import com.algaworks.algashop.billing.application.creditcard.management.CreditCardManagementService;
import com.algaworks.algashop.billing.application.creditcard.management.TokenizedCreditCardInput;
import com.algaworks.algashop.billing.application.creditcard.query.CreditCardOutput;
import com.algaworks.algashop.billing.application.creditcard.query.CreditCardQueryService;
import com.algaworks.algashop.billing.infrastructure.security.SecurityAnnotations;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/credit-cards")
@RequiredArgsConstructor
public class CreditCardController {

    private final CreditCardManagementService creditCardManagementService;
    private final CreditCardQueryService creditCardQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityAnnotations.CanWriteCreditCards
    public CreditCardOutput register(@PathVariable UUID customerId,
                                     @RequestBody @Valid TokenizedCreditCardInput input) {
        input.setCustomerId(customerId);
        UUID creditCardId = creditCardManagementService.register(input);
        return creditCardQueryService.findOne(customerId, creditCardId);
    }

    @GetMapping
    @SecurityAnnotations.CanReadCreditCards
    public List<CreditCardOutput> findAllByCustomer(@PathVariable UUID customerId) {
        return creditCardQueryService.findByCustomer(customerId);
    }

    @GetMapping("/{creditCardId}")
    @SecurityAnnotations.CanReadCreditCards
    public CreditCardOutput findAllByCustomer(@PathVariable UUID customerId,
                                                    @PathVariable UUID creditCardId) {
        return creditCardQueryService.findOne(customerId, creditCardId);
    }

    @DeleteMapping("/{creditCardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityAnnotations.CanWriteCreditCards
    public void delete(@PathVariable UUID customerId, @PathVariable UUID creditCardId) {
        creditCardManagementService.delete(customerId, creditCardId);
    }
}
