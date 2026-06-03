package com.algaworks.algashop.billing.infrastructure.creditcard.fastpay;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(value = "api/v1/credit-cards", accept = MediaType.APPLICATION_JSON_VALUE)
public interface FastpayCreditCardAPIClient {

    @PostExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
    FastpayCreditCardResponse create(@RequestBody FastpayCreditCardInput input);

    @GetExchange("/{creditCardId}")
    FastpayCreditCardResponse findById(@PathVariable String creditCardId);

    @DeleteExchange("/{creditCardId}")
    void delete(@PathVariable String creditCardId);
}
