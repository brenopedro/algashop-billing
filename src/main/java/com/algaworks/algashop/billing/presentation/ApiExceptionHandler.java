package com.algaworks.algashop.billing.presentation;

import com.algaworks.algashop.billing.domain.model.DomainEntityNotFoundException;
import com.algaworks.algashop.billing.domain.model.DomainException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle("Invalid fields");
        problemDetail.setDetail("One or more fields are invalid");
        problemDetail.setType(URI.create("/errors/invalid-fields"));

        Map<String, String> fieldErrors = ex.getBindingResult().getAllErrors().stream().collect(
                Collectors.toMap(
                        objectError -> ((FieldError) objectError).getField(),
                        objectError -> messageSource.getMessage(objectError, LocaleContextHolder.getLocale())));

        problemDetail.setProperty("fields", fieldErrors);

        return super.handleExceptionInternal(ex, problemDetail, headers, status, request);
    }

    @ExceptionHandler(DomainEntityNotFoundException.class)
    public ProblemDetail handleDomainEntityNotFoundException(DomainEntityNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Not found");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setType(URI.create("/errors/not-found"));
        return problemDetail;
    }

    @ExceptionHandler({DomainException.class, UnprocessableContentException.class})
    public ProblemDetail handleUnprocessableContentException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_CONTENT);
        problemDetail.setTitle("Unprocessable content");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setType(URI.create("/errors/unprocessable-content"));
        return problemDetail;
    }

    @ExceptionHandler(GatewayTimeoutException.class)
    public ProblemDetail handleGatewayTimeoutException(GatewayTimeoutException ex) {
        log.error(ex.getMessage(), ex);
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.GATEWAY_TIMEOUT);
        problemDetail.setTitle("Gateway Timeout");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setType(URI.create("/errors/gateway-timeout"));
        return problemDetail;
    }

    @ExceptionHandler(BadGatewayException.class)
    public ProblemDetail handleBadGatewayException(BadGatewayException ex) {
        log.error(ex.getMessage(), ex);
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);
        problemDetail.setTitle("Bad Gateway");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setType(URI.create("/errors/bad-gateway"));
        return problemDetail;
    }


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setType(URI.create("/errors/internal-server-error"));
        return problemDetail;
    }
}
