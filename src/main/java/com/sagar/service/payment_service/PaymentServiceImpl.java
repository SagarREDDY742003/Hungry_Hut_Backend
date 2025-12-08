package com.sagar.service.payment_service;

import com.sagar.model.Order;
import com.sagar.response.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${frontend.base.url}")
    private String frontendBaseUrl;

    @Override
    public PaymentResponse createPaymentLink(Order order) {

        if (stripeSecretKey == null || stripeSecretKey.isBlank()) {
            throw new RuntimeException("Stripe secret key is not configured");
        }

        if (frontendBaseUrl == null || !frontendBaseUrl.startsWith("http")) {
            throw new RuntimeException("Frontend base URL is not configured correctly");
        }

        Long totalPriceObj = order.getTotalPrice();

        if (totalPriceObj == null) {
            throw new RuntimeException("Order total price is null");
        }

        // amount in paise (e.g. ₹100.50 -> 10050)
        long baseAmount = Math.round(totalPriceObj * 100);
        long totalAmountInPaise = baseAmount + 5800L; // if you want a fixed fee

        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontendBaseUrl+"/payment/success/" + order.getId())
                .setCancelUrl(frontendBaseUrl+"/payment/fail")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("inr")
                                                .setUnitAmount(totalAmountInPaise)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("hungry hut")
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        try {
            Session session = Session.create(params);
            PaymentResponse res = new PaymentResponse();
            res.setPayment_url(session.getUrl());
            return res;
        } catch (StripeException e) {
            // You can log this properly with a logger
            throw new RuntimeException("Failed to create Stripe payment session: " + e.getMessage(), e);
        }
    }
}

