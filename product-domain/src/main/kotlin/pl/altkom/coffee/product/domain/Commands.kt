package pl.altkom.coffee.product.domain

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class RegisterProductPreparationCommand(
        @TargetAggregateIdentifier val id: String,
        val productDefId: String,
        val productReceiverId: String,
        val productExecutorId: String
)

data class CancelProductPreparationCommand(
        @TargetAggregateIdentifier val id: String)

data class ChangeProductReceiverCommand(
        @TargetAggregateIdentifier val id: String,
        val newProductReceiverId: String,
        val executor: String
)