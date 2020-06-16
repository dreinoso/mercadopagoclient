package com.chainreaction.mercadopagoclient.model

import android.content.Context

class Device(context: Context?) {
    var fingerprint: Fingerprint

    init {
        fingerprint = context?.let { Fingerprint(it) }!!
    }
}