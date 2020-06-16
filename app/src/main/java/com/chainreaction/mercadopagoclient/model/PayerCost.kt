package com.chainreaction.mercadopagoclient.model

import java.math.BigDecimal
import java.math.RoundingMode

class PayerCost {
    var installments: Int? = null
    var installmentRate: Double? = null
    var labels: List<String>? = null
    var minAllowedAmount: Double? = null
    var maxAllowedAmount: Double? = null

    fun getShareAmount(amount: Double): BigDecimal {
        return BigDecimal(amount * ((1 + installmentRate!! / 100) / installments!!))
            .setScale(2, RoundingMode.HALF_UP)
    }

    fun getTotalAmount(amount: Double): BigDecimal {
        return BigDecimal(amount * ((1 + installmentRate!! / 100) / installments!!))
            .setScale(2, RoundingMode.HALF_UP)
            .multiply(BigDecimal(installments!!))
    }

    override fun toString(): String {
        return installments.toString()
    }
}