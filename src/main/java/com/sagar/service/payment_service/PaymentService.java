package com.sagar.service.payment_service;

import com.sagar.model.Order;
import com.sagar.response.PaymentResponse;
import com.stripe.exception.StripeException;

public interface PaymentService{
    public PaymentResponse createPaymentLink(Order order) throws StripeException;
}
