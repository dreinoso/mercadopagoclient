package com.chainreaction.mercadopagoclient;

import com.chainreaction.mercadopagoclient.model.PaymentMethod;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface PaymentService {

    @GET("https://api.mercadopago.com/v1/payment_methods")
    List<PaymentMethod> getPaymentMethod(@Query("public_key") String public_key);

    @GET("https://api.mercadopago.com/v1/payment_methods/card_issuers")
    List<PaymentMethod> getCardIssuers(
            @Query("public_key") String public_key,
            @Query("payment_method_id") String payment_method_id);

    @GET("https://api.mercadopago.com/v1/payment_methods/installments")
    List<PaymentMethod> getInstallments(
            @Query("public_key") String public_key,
            @Query("payment_method_id") String payment_method_id,
            @Query("issuer.id") String issuerId,
            @Query("amount") Long amount);

    @GET("/checkout/custom/payment_methods/search")
    List<PaymentMethod> getPaymentMethodByBin(@Query("public_key") String public_key, @Query("bin") String bin);

    @GET("/checkout/custom/payment_methods/search")
    void getPaymentMethodByBin(@Query("public_key") String public_key, @Query("bin") String bin, Callback<List<PaymentMethod>> callback);

    @GET("/checkout/custom/payment_methods/search")
    void getPaymentMethodById(@Query("public_key") String public_key, @Query("payment_method") String paymentMethod, Callback<List<PaymentMethod>> callback);

    @GET("/checkout/custom/payment_methods/search")
    List<PaymentMethod> getPaymentMethodById(@Query("public_key") String public_key, @Query("payment_method") String paymentMethod);

    @GET("/contacts?access_token=APP_USR-1311377052931992-052015-f96957603815fe2f3bbf7f4d4e2f2435__M_K__-84696660")
    String getContacts();

}