package com.chainreaction.mercadopagoclient

import com.chainreaction.mercadopagoclient.model.Token
import retrofit.Callback

abstract class TokenCallback<T : Token?> :
    Callback<T> {
    abstract fun success(data: T)
}