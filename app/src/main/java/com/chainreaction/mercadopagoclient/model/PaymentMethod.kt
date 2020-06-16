package com.chainreaction.mercadopagoclient.model

import com.chainreaction.mercadopagoclient.model.CardConfiguration
import com.chainreaction.mercadopagoclient.model.CardIssuer
import com.chainreaction.mercadopagoclient.model.ExceptionByCardIssuer
import com.chainreaction.mercadopagoclient.model.PayerCost

class PaymentMethod {
    var id: String? = null
    var name: String? = null
    var paymentTypeId: String? = null
    var cardIssuer: CardIssuer? = null
    var siteId: String? = null
    var secureThumbnail: String? = null
    var thumbnail: String? = null
    var labels: List<String>? = null
    var minAccreditationDays: Int? = null
    var maxAccreditationDays: Int? = null
    private var payerCosts: List<PayerCost>? = null
    var exceptionsByCardIssuer: List<ExceptionByCardIssuer>? = null
    var cardConfiguration: List<CardConfiguration>? = null

    fun getPayerCosts(): List<PayerCost>? {
        return payerCosts
    }

    fun setPayerCosts(payerCosts: List<PayerCost>?) {
        this.payerCosts = payerCosts
    }

    override fun toString(): String {
        return name!!
    }
}