package pl.altkom.coffee.product.api

data class ProductPreparationRegisteredEvent(
        val id: String,
        val productDefId: String,
        val productReceiverId: String,
        val productExecutorId: String
)

data class ProductPreparationCancelledEvent(val id: String)

data class ProductReceiverChangedEvent(
        val id: String,
        val newProductReceiverId: String
)