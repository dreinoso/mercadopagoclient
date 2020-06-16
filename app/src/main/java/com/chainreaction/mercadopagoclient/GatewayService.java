package com.chainreaction.mercadopagoclient;

import com.chainreaction.mercadopagoclient.model.Card;
import com.chainreaction.mercadopagoclient.model.Token;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Query;

public interface GatewayService {
    @POST("/card_tokens")
    void getToken(@Query("public_key") String public_key, @Body Card card, Callback<Token> callback);

    @POST("/card_tokens")
    Token getToken(@Query("public_key") String public_key, @Body Card card);
}