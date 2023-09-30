package hu.progmasters.moovsmart.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PayoutBatch;
import com.paypal.base.rest.PayPalRESTException;
import hu.progmasters.moovsmart.domain.paypal.Order;
import hu.progmasters.moovsmart.domain.paypal.PayoutOrder;
import hu.progmasters.moovsmart.service.PayPalPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static hu.progmasters.moovsmart.service.PayPalPaymentService.CANCEL_URL;
import static hu.progmasters.moovsmart.service.PayPalPaymentService.SUCCESS_URL;

@RestController
@RequestMapping("/api/paypal")
@Slf4j
public class PayPalPaymentController {

    @Autowired
    private PayPalPaymentService payPalPaymentService;


    @PostMapping("/paying")
    public String payment(@RequestParam("amount") String amount,
                          @RequestParam("currency") String currency,
                          @RequestParam("method") String method,
                          @RequestParam("description") String description) {
        try {
            Payment payment = payPalPaymentService.createPayment(Double.valueOf(amount), currency, method, "sale", description, CANCEL_URL, SUCCESS_URL);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.info("Http request, POST /api/paypal/paying, request parameters: " + amount, currency, method, description);
            e.getDetails();
        }
        return "/paying/error";
    }

    @GetMapping(CANCEL_URL)
    public ResponseEntity<String> cancelPay() {
        return new ResponseEntity<>("The payment is canceled!", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/paying/error")
    public ResponseEntity<String> cancelError() {
        return new ResponseEntity<>("There is an error in payment process!", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(SUCCESS_URL)
    public ResponseEntity<String> successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = payPalPaymentService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return new ResponseEntity<>("The payment is successful!", HttpStatus.OK);
            }
        } catch (PayPalRESTException e) {
            log.info("Http request, POST /api/paypal/paying, request parameters with paymentId " + payerId + "and PayerId" + payerId);
        }
        return new ResponseEntity<>("The payment is successful!", HttpStatus.OK);
    }

    @PostMapping("/payout")
    public PayoutBatch payout(@RequestBody PayoutOrder payoutOrder) {
        try {
            return payPalPaymentService.payout(payoutOrder.getTotalAmount(), payoutOrder.getCurrency(),
                    payoutOrder.getReceiver());
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return null;
    }
}

