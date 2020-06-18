package com.chainreaction.mercadopagoclient.model

import java.util.*

class Token {
    var id: String? = null
    var publicKey: String? = null
    var cardId: String? = null
    var luhnValidation: String? = null
    var status: String? = null
    var usedDate: String? = null
    var cardNumberLength: Int? = null
    var creationDate: Date? = null
    var truncCardNumber: String? = null
    var securityCodeLength: Int? = null
    var expirationMonth: Int? = null
    var expirationYear: Int? = null
    var lastModifiedDate: Date? = null
    var dueDate: Date? = null

}