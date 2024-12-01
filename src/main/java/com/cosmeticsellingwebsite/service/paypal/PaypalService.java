package com.cosmeticsellingwebsite.service.paypal;

import com.paypal.sdk.PaypalServerSdkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.sdk.controllers.OrdersController;
import com.paypal.sdk.exceptions.ApiException;
import com.paypal.sdk.http.response.ApiResponse;
import com.paypal.sdk.models.*;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;

@Service
public class PaypalService {

    @Autowired
    private PaypalServerSdkClient client;

    public Order createOrder(String cart, Double amount) throws IOException, ApiException {
        //convert vnd to usd
        double calculatedAmount = 0.000039 * amount;
        DecimalFormat df = new DecimalFormat("0.00");
        String formattedAmount = df.format(calculatedAmount);
        OrdersCreateInput ordersCreateInput = new OrdersCreateInput.Builder(
                null,
                new OrderRequest.Builder(
                        CheckoutPaymentIntent.CAPTURE,
                        Arrays.asList(
                                new PurchaseUnitRequest.Builder(
                                        new AmountWithBreakdown.Builder(
                                                "USD",
//													"999.00",
                                                formattedAmount
//                                                ,
//                                                String.valueOf(0.000039*amount)
                                        )
                                                .build())
                                        .build()))
                        .build())
                .build();

        OrdersController ordersController = client.getOrdersController();

        ApiResponse<Order> apiResponse = ordersController.ordersCreate(ordersCreateInput);

        return apiResponse.getResult();
    }

    public Order captureOrders(String orderID) throws IOException, ApiException {
        OrdersCaptureInput ordersCaptureInput = new OrdersCaptureInput.Builder(
                orderID,
                null)
                .build();
        OrdersController ordersController = client.getOrdersController();
        ApiResponse<Order> apiResponse = ordersController.ordersCapture(ordersCaptureInput);
        return apiResponse.getResult();
    }
}
