package com.chainreaction.mercadopagoclient

import com.chainreaction.mercadopagoclient.model.Entity
import com.chainreaction.mercadopagoclient.model.PaymentMethod
import retrofit.Callback
import retrofit.http.GET
import retrofit.http.Query

interface PaymentService {
    @GET("/payment_methods")
    fun getPaymentMethod(@Query("public_key") public_key: String?): List<PaymentMethod?>?

    @GET("/payment_methods/card_issuers")
    fun getCardIssuers(
        @Query("public_key") public_key: String?,
        @Query("payment_method_id") payment_method_id: String?
    ): List<PaymentMethod?>?

    @GET("/payment_methods/installments")
    fun getInstallments(
        @Query("public_key") public_key: String?,
        @Query("payment_method_id") payment_method_id: String?,
        @Query("issuer.id") issuerId: String?,
        @Query("amount") amount: Long?
    ): List<Entity.Installment?>?

    @GET("/checkout/custom/payment_methods/search")
    fun getPaymentMethodByBin(
        @Query("public_key") public_key: String?,
        @Query("bin") bin: String?
    ): List<PaymentMethod?>?

    @GET("/checkout/custom/payment_methods/search")
    fun getPaymentMethodByBin(
        @Query("public_key") public_key: String?,
        @Query("bin") bin: String?,
        callback: Callback<MutableList<PaymentMethod>>?
    )

    @GET("/checkout/custom/payment_methods/search")
    fun getPaymentMethodById(
        @Query("public_key") public_key: String?,
        @Query("payment_method") paymentMethod: String?,
        callback: Callback<List<PaymentMethod?>?>?
    )

    @GET("/checkout/custom/payment_methods/search")
    fun getPaymentMethodById(
        @Query("public_key") public_key: String?,
        @Query("payment_method") paymentMethod: String?
    ): List<PaymentMethod?>?

    @get:GET("/contacts?access_token=APP_USR-1311377052931992-052015-f96957603815fe2f3bbf7f4d4e2f2435__M_K__-84696660")
    val contacts: String?
}