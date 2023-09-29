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
import org.springframework.web.bind.annotation.*;

import static hu.progmasters.moovsmart.service.PayPalPaymentService.CANCEL_URL;
import static hu.progmasters.moovsmart.service.PayPalPaymentService.SUCCESS_URL;

@RestController
@RequestMapping("/api/paypal")
@Slf4j
public class PayPalPaymentController {

    @Autowired
    private PayPalPaymentService payPalPaymentService;

    @PostMapping("/pay")
    public String payment(@RequestBody Order order) {
        try {
            Payment payment = payPalPaymentService.createPayment(order.getTotalAmount(), order.getCurrency(), order.getDescription());
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping(CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping(SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = payPalPaymentService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "success";
            }
        } catch (PayPalRESTException e) {
        }
        return "redirect:/";
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

