package pl.altkom.coffee.product.api.dto

data class BeginProductPreparationRequest(
        val id: String, val productDefId: String, val productReceiverName: String, val productName: String)

data class ChangeProductReceiverRequest(
        val id: String, val productReceiverNewName: String)
