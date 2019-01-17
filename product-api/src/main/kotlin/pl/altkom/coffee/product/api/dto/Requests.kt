package pl.altkom.coffee.product.api.dto

data class RegisterProductPreparationRequest(
        val id: String,
        val productDefId: String,
        val productReceiverId: String,
        val productName: String,
        val productExecutorId: String
)

data class CancelProductPreparationRequest(
        val id: String
)

data class ChangeProductReceiverRequest(
        val id: String,
        val productReceiverNewId: String,
        val productExecutorId: String
)

