package pl.altkom.coffee.product.api.dto

data class RegisterProductPreparationRequest(
        val id: String, val productDefId: String, val productReceiverName: String, val productName: String)

data class EndProductPreparationRequest(
        val id: String)

data class ChangeProductReceiverRequest(
        val id: String, val productReceiverNewName: String)

data class CancelProductPreparationRequest(
        val id: String)
