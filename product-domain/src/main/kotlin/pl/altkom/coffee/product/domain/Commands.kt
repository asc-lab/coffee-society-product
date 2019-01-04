package pl.altkom.coffee.product.domain

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class RegisterProductPreparationCommand(
        @TargetAggregateIdentifier val id: String,
        val productDefId: String,
        val productReceiverName: String,
        val executor: String
)

data class CancelProductPreparationCommand(
        @TargetAggregateIdentifier val id: String)

data class ChangeProductReceiverCommand(
        @TargetAggregateIdentifier val id: String,
        val newProductReceiverName: String,
        val executor: String
)