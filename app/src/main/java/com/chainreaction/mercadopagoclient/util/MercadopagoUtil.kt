package com.chainreaction.mercadopagoclient.util

import com.chainreaction.mercadopagoclient.model.PaymentMethod
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

object MercadopagoUtil {
    fun getInstallments(
        paymentMethod: PaymentMethod,
        amount: Double
    ): Map<Int?, Any> {
        val installments: MutableMap<Int?, Any> =
            TreeMap()
        for (p in paymentMethod.getPayerCosts()!!) {
            if (p.minAllowedAmount!! < amount && amount < p.maxAllowedAmount!!) {
                val installment: MutableMap<String, Any?> =
                    HashMap()
                val shareAmount =
                    getShareAmount(amount, p.installmentRate, p.installments)
                installment["installment_rate"] = p.installmentRate
                installment["share_amount"] = shareAmount
                installment["total_amount"] = shareAmount.multiply(BigDecimal(p.installments!!))
                    .setScale(2, RoundingMode.HALF_UP)
                installments[p.installments] = installment
            }
        }
        return installments
    }

    private fun getShareAmount(
        amount: Double,
        rate: Double?,
        installments: Int?
    ): BigDecimal {
        return BigDecimal(amount * ((1 + rate!! / 100) / installments!!))
            .setScale(2, RoundingMode.HALF_UP)
    }
}