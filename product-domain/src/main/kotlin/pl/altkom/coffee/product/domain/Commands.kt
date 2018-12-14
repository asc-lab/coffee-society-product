package pl.altkom.coffee.product.domain

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class BeginProductPreparationCommand(
        @TargetAggregateIdentifier val id: String,
        val productDefId: String, val productReceiverName: String,
        val productName: String)

data class EndProductPreparationCommand(
        val id: String)

data class CancelProductPreparationCommand(
        val id: String)

data class ChangeProductReceiverCommand(
        val id: String,
        val newProductReceiverName: String)