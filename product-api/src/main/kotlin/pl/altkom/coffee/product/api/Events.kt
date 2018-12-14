package pl.altkom.coffee.product.api

data class ProductPreparationStartedEvent(
        val id: String, val productDefId: String, val productReceiverName: String, val productExecutorName: String)

data class ProductPreparationEndedEvent(val id: String)

data class ProductPreparationCancelledEvent(val id: String)

data class ProductReceiverChangedEvent(val id: String, val newProductReceiverName: String)