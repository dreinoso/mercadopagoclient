package com.chainreaction.mercadopagoclient.model

class CardIssuer {
    var id: String? = null
    var name: String? = null
    var labels: List<String>? = null

    override fun toString(): String {
        return name!!
    }
}