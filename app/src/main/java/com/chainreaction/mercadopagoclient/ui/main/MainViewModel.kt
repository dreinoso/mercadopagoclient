package com.chainreaction.mercadopagoclient.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chainreaction.mercadopagoclient.Mercadopago
import com.chainreaction.mercadopagoclient.model.PaymentMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var amountFormattedLiveData = MutableLiveData<String>()
    var paymentMethodsLiveData = MutableLiveData<List<PaymentMethod?>>()
    var mercadoPago = Mercadopago("841d020b-1077-4742-ad55-7888a0f5aefa")

    fun getPaymentMethods() {
        viewModelScope.launch(Dispatchers.IO) {
            paymentMethodsLiveData.postValue(mercadoPago.getPaymentMethods())
        }
    }
}
