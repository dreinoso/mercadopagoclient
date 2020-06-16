package com.chainreaction.mercadopagoclient.model

import android.content.Context
import android.text.TextUtils
import java.util.*

class Card(
    cardNumber: String?,
    expirationMonth: Int,
    expirationYear: Int,
    securityCode: String?,
    cardholderName: String?,
    docType: String?,
    docNumber: String?
) {
    var cardNumber: String?
    var securityCode: String?
    var expirationMonth: Int
    var expirationYear: Int
    var cardholder: Cardholder
    var device: Device? = null

    fun setDevice(context: Context?) {
        device = Device(context)
    }

    private fun normalizeCardNumber(number: String?): String? {
        return number?.trim { it <= ' ' }?.replace("\\s+|-".toRegex(), "")
    }

    fun validateCard(): Boolean {
        return validateCardNumber() && validateSecurityCode() && validateExpiryDate() && validateDoc() && validateCardholderName()
    }

    fun validateCardNumber(): Boolean {
        return !TextUtils.isEmpty(cardNumber) && cardNumber!!.length > MIN_LENGTH_NUMBER && cardNumber!!.length < MAX_LENGTH_NUMBER
    }

    fun validateSecurityCode(): Boolean {
        return securityCode == null || !TextUtils.isEmpty(securityCode) && securityCode!!.length >= 3 && securityCode!!.length <= 4
    }

    fun validateExpiryDate(): Boolean {
        if (!validateExpMonth()) {
            return false
        }
        return if (!validateExpYear()) {
            false
        } else !hasMonthPassed(expirationYear, expirationMonth)
    }

    fun validateExpMonth(): Boolean {
        return if (expirationMonth == null) {
            false
        } else expirationMonth >= 1 && expirationMonth <= 12
    }

    fun validateExpYear(): Boolean {
        return if (expirationYear == null) {
            false
        } else !hasYearPassed(expirationYear)
    }

    fun validateDoc(): Boolean {
        return validateDocType() && validateDocNumber()
    }

    fun validateDocType(): Boolean {
        return !TextUtils.isEmpty(cardholder.identification?.type)
    }

    fun validateDocNumber(): Boolean {
        return !TextUtils.isEmpty(cardholder.identification?.number)
    }

    fun validateCardholderName(): Boolean {
        return !TextUtils.isEmpty(cardholder.name)
    }

    companion object {
        private val now = Calendar.getInstance()
        const val MIN_LENGTH_NUMBER = 10
        const val MAX_LENGTH_NUMBER = 19
        private fun hasYearPassed(year: Int): Boolean {
            val normalized = normalizeYear(year)
            return normalized < now[Calendar.YEAR]
        }

        private fun hasMonthPassed(year: Int, month: Int): Boolean {
            return hasYearPassed(year) || normalizeYear(
                year
            ) == now[Calendar.YEAR] && month < now[Calendar.MONTH] + 1
        }

        private fun normalizeYear(year: Int): Int {
            var year = year
            if (year < 100 && year >= 0) {
                val currentYear =
                    now[Calendar.YEAR].toString()
                val prefix = currentYear.substring(0, currentYear.length - 2)
                year = String.format(Locale.US, "%s%02d", prefix, year).toInt()
            }
            return year
        }
    }

    init {
        this.cardNumber = normalizeCardNumber(cardNumber)
        this.expirationMonth = expirationMonth
        this.expirationYear = normalizeYear(expirationYear)
        this.securityCode = securityCode
        cardholder = Cardholder()
        cardholder.name = cardholderName
        cardholder.identification = Identification()
        cardholder.identification!!.number = docNumber
        cardholder.identification!!.type = docType
    }
}