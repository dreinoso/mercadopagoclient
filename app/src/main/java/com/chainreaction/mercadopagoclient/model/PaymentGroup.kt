package com.chainreaction.mercadopagoclient.model

import android.os.Parcel
import android.os.Parcelable
import com.chainreaction.mercadopagoclient.model.CardConfiguration
import com.chainreaction.mercadopagoclient.model.CardIssuer
import com.chainreaction.mercadopagoclient.model.ExceptionByCardIssuer
import com.chainreaction.mercadopagoclient.model.PayerCost

data class PaymentGroup(val type: String?, val count: Int) { }