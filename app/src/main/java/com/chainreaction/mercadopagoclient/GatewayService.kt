package com.chainreaction.mercadopagoclient

import com.chainreaction.mercadopagoclient.model.Card
import com.chainreaction.mercadopagoclient.model.Token
import retrofit.Callback
import retrofit.http.Body
import retrofit.http.POST
import retrofit.http.Query

interface GatewayService {
    @POST("/card_tokens")
    fun getToken(
        @Query("public_key") public_key: String?,
        @Body card: Card?,
        callback: Callback<Token?>?
    )

    @POST("/card_tokens")
    fun getToken(
        @Query("public_key") public_key: String?,
        @Body card: Card?
    ): Token?
}