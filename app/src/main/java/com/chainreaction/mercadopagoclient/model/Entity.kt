package com.chainreaction.mercadopagoclient.model

sealed class Entity {

    data class Installment(
        val payment_method_id: String?,
        val payment_type_id: String?,
        val payer_costs: List<PlayerCosts>?
    ) : Entity()

    data class PlayerCosts(
        val installments: String?,
        val recommended_message: String?,
        val total_amount: Double?
    ) : Entity()
}