package com.chainreaction.mercadopagoclient.model

class ExceptionByCardIssuer {
    var cardIssuer: CardIssuer? = null
    var labels: List<String>? = null
    var thumbnail: String? = null
    var secureThumbnail: String? = null
    var totalFinancialCost: Double? = null
    var acceptedBins: List<Int>? = null
    private var payerCosts: List<PayerCost>? = null

    fun getPayerCosts(): List<PayerCost>? {
        return payerCosts
    }

    fun setPayerCosts(payerCosts: List<PayerCost>?) {
        this.payerCosts = payerCosts
    }
}