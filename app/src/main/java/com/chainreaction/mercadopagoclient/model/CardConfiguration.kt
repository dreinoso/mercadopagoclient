package com.chainreaction.mercadopagoclient.model

class CardConfiguration {
    var binCardPattern: String? = null
    var binCardExclusionPattern: String? = null
    var cardNumberLength: Int? = null
    var securityCodeLength: Int? = null
    var luhnAlgorithm: String? = null
    var additionalInfoNeeded: List<String>? = null
    var exceptionsByCardValidations: String? = null

}