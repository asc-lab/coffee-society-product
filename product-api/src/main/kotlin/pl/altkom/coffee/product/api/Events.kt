package pl.altkom.coffee.product.api

data class ProductPreparationStartedEvent(
        val id: String, val productDefId: String, val productReceiverName: String, val productName: String)

data class ProductPreparationEndedEvent(val id: String)

data class ProductPreparationCancelled(val id: String)

data class ProductReceiverChangedEvent(val id: String, val newProductReceiverName: String)