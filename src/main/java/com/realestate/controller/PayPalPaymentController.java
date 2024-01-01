package com.realestate.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.realestate.service.PayPalPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paypal")
@Slf4j
public class PayPalPaymentController {

    @Autowired
    private PayPalPaymentService payPalPaymentService;


    @PostMapping("/paying")
    @Operation(summary = "Creating payment by customer")
    @ApiResponse(responseCode = "201", description = "Payment is created by customer.")
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_AGENT"})
    public ResponseEntity<String> payment(@RequestParam("amount") String amount,
                          @RequestParam("currency") String currency,
                          @RequestParam("method") String method,
                          @RequestParam("description") String description) {
        try {
            Payment payment = payPalPaymentService.createPayment(Double.valueOf(amount), currency, method, "sale", description, PayPalPaymentService.CANCEL_URL, PayPalPaymentService.SUCCESS_URL);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    log.info("Http request, POST /api/paypal/paying, request parameters: " + amount, currency, method, description);
                    return new ResponseEntity<>("redirect:" + link.getHref(), HttpStatus.CREATED);
                }
            }
        } catch (PayPalRESTException e) {
            log.error("Http request, POST /api/paypal/paying, request parameters: " + amount, currency, method, description);
            e.getDetails();
        }
        return new ResponseEntity<>("There is an error in payment process!", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/cancel")
    @Operation(summary = "Cancelling payment by customer")
    @ApiResponse(responseCode = "201", description = "Payment is cancelled by customer.")
    public ResponseEntity<String> cancelPay() {
        log.error("Http request, POST /api/paypal/cancel");
        return new ResponseEntity<>("The payment is canceled!", HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/success")
    @Operation(summary = "The payment is successful")
    @ApiResponse(responseCode = "201", description = "The payment is successful.")
    public ResponseEntity<String> successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = payPalPaymentService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                log.info("Http request, GET /api/paypal/success, request parameters with paymentId " + payerId + "and PayerId" + payerId);
                return new ResponseEntity<>("The payment is successful!", HttpStatus.OK);
            }
        } catch (PayPalRESTException e) {
            log.error("Http request, GET /api/paypal/success, request parameters with paymentId " + payerId + "and PayerId" + payerId);
        }
        return new ResponseEntity<>("The payment is successful!", HttpStatus.OK);
    }


}

