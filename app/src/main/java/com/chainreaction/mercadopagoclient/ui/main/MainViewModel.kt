package com.chainreaction.mercadopagoclient.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chainreaction.mercadopagoclient.Mercadopago
import com.chainreaction.mercadopagoclient.model.Entity
import com.chainreaction.mercadopagoclient.model.PaymentMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val MP_PUBLIC_KEY = "TEST-8b64ffad-a71f-4bf7-836e-5acee02aedbb"
    var amountFormattedLiveData = MutableLiveData<String>()
    var paymentMethodsLiveData = MutableLiveData<List<PaymentMethod?>>()
    var installmentsLiveData = MutableLiveData<List<Entity.Installment?>>()
    var mercadoPago = Mercadopago(MP_PUBLIC_KEY)

    fun getPaymentMethods() {
        viewModelScope.launch(Dispatchers.IO) {
            paymentMethodsLiveData.postValue(mercadoPago.getPaymentMethods())
        }
    }

    fun getInstallments(paymentMethodId:String) {
        viewModelScope.launch(Dispatchers.IO) {
            installmentsLiveData.postValue(mercadoPago.getInstallments(paymentMethodId,
                "", getAmountAsNumber(amountFormattedLiveData.value)))
        }
    }

    private fun getAmountAsNumber(text: String?): Long? {
        var cleanString = text.toString().replace("[$,.]".toRegex(), "");
        return cleanString.toLong();
    }
}
