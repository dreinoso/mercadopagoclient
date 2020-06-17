package com.chainreaction.mercadopagoclient

import android.content.Context
import com.chainreaction.mercadopagoclient.model.Card
import com.chainreaction.mercadopagoclient.model.PaymentMethod
import com.chainreaction.mercadopagoclient.model.Token
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import retrofit.Callback
import retrofit.RestAdapter
import retrofit.converter.GsonConverter

class Mercadopago(private val defaultPublishableKey: String) {
    private val gson =
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .serializeNulls().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create()
    private val restAdapter =
        RestAdapter.Builder().setEndpoint(MERCADOPAGO_BASE_URL)
            .setLogLevel(RestAdapter.LogLevel.FULL).setConverter(GsonConverter(gson)).build()
    private val restAdapterApi =
        RestAdapter.Builder().setEndpoint(BASE_URL)
            .setLogLevel(RestAdapter.LogLevel.FULL).setConverter(GsonConverter(gson)).build()

    fun createToken (
        card: Card,
        callback: Callback<*>?,
        context: Context
    ) {
        card.setDevice(context)
        val service = restAdapter.create(GatewayService::class.java)
        service.getToken(defaultPublishableKey, card, callback as Callback<Token?>?)
    }

    fun getPaymentMethodByBin(
        bin: String?,
        callback: Callback<MutableList<PaymentMethod>>?
    ) {
        val service =
            restAdapterApi.create(PaymentService::class.java)
        service.getPaymentMethodByBin(defaultPublishableKey, bin, callback)
    }

    fun getPaymentMethodById(
        paymentMethod: String?,
        callback: Callback<List<PaymentMethod?>?>?
    ) {
        val service =
            restAdapterApi.create(PaymentService::class.java)
        service.getPaymentMethodById(defaultPublishableKey, paymentMethod, callback)
    }

    fun createToken(card: Card?): Token? {
        val service = restAdapter.create(GatewayService::class.java)
        return service.getToken(defaultPublishableKey, card)
    }

    fun getPaymentMethodByBin(bin: String?): List<PaymentMethod?>? {
        val service =
            restAdapterApi.create(PaymentService::class.java)
        return service.getPaymentMethodByBin(defaultPublishableKey, bin)
    }

    fun getPaymentMethodById(paymentMethod: String?): List<PaymentMethod?>? {
        val service =
            restAdapterApi.create(PaymentService::class.java)
        return service.getPaymentMethodById(defaultPublishableKey, paymentMethod)
    }

    fun getPaymentMethods(): List<PaymentMethod?>? {
        val service =
            restAdapterApi.create(PaymentService::class.java)
        return service.getPaymentMethod(defaultPublishableKey)
    }

    fun getCardIssuers(
        paymentMethod: String?,
        paymentMethodId: String?
    ): List<PaymentMethod?>? {
        val service =
            restAdapterApi.create(PaymentService::class.java)
        return service.getCardIssuers(defaultPublishableKey, paymentMethodId)
    }

    fun getInstallments(
        paymentMethod: String?, paymentMethodId: String?,
        issuerId: String?, amount: Long?
    ): List<PaymentMethod?>? {
        val service =
            restAdapterApi.create(PaymentService::class.java)
        return service.getInstallments(defaultPublishableKey, paymentMethodId, issuerId, amount)
    }

    companion object {
        private const val MERCADOPAGO_BASE_URL = "https://pagamento.mercadopago.com"
        private const val BASE_URL = "https://api.mercadopago.com/v1"
    }

}