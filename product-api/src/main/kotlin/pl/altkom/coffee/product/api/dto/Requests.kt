package pl.altkom.coffee.product.api.dto

data class RegisterProductPreparationRequest(
        val id: String,
        val productDefId: String,
        val productReceiverId: String,
        val productName: String,
        val productExecutorId: String
)

data class ChangeProductReceiverRequest(
        val id: String,
        val executorId: String,
        val productReceiverNewId: String
)

data class CancelProductPreparationRequest(
        val id: String)
