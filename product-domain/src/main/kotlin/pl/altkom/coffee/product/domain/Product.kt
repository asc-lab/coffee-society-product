package pl.altkom.coffee.product.domain

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import pl.altkom.coffee.product.api.ProductPreparationCancelledEvent
import pl.altkom.coffee.product.api.ProductPreparationRegisteredEvent
import pl.altkom.coffee.product.api.ProductReceiverChangedEvent
import pl.altkom.coffee.product.api.enums.ProductState

@Aggregate
class Product {
    @AggregateIdentifier
    lateinit var productId: String
    lateinit var productDefId: String
    lateinit var executorId: String
    lateinit var receiverId: String
    lateinit var state: ProductState

    constructor()

    @CommandHandler
    constructor(command: RegisterProductPreparationCommand) {
        with(command) {
            AggregateLifecycle.apply(
                    ProductPreparationRegisteredEvent(id, selectedProductId, productDefId,
                            productReceiverId, productExecutorId))
        }
    }

    @EventSourcingHandler
    fun on(event: ProductPreparationRegisteredEvent) {
        this.productId = event.id
        this.productDefId = event.productDefId
        this.receiverId = event.productReceiverId
        this.executorId = event.productExecutorId
        this.state = ProductState.PREPARED
    }

    @CommandHandler
    fun handle(command: CancelProductPreparationCommand) {
        if (isCancelled()) {
            throw IllegalStateException(CANCELLED_ERROR_MESSAGE)
        }

        with(command) {
            AggregateLifecycle.apply(ProductPreparationCancelledEvent(id, productDefId))
        }
    }

    @EventSourcingHandler
    fun on(event: ProductPreparationCancelledEvent) {
        this.state = ProductState.CANCELLED
    }

    @CommandHandler
    fun handle(command: ChangeProductReceiverCommand) {
        if (isCancelled()) {
            throw IllegalStateException(CANCELLED_ERROR_MESSAGE)
        }
        if (!canChangeReceiver(command.executorId)) {
            throw IllegalStateException(WRONG_USER_ERROR_MESSAGE)
        }

        AggregateLifecycle.apply(ProductReceiverChangedEvent(productId, command.productDefId, command.productReceiverNewId))
    }

    @EventSourcingHandler
    fun on(event: ProductReceiverChangedEvent) {
        this.receiverId = event.productReceiverNewId
    }

    private fun isCancelled(): Boolean {
        return this.state == ProductState.CANCELLED
    }

    private fun canChangeReceiver(memberId: String): Boolean {
        return this.receiverId == memberId || this.executorId == memberId

    }

    companion object {
        private const val CANCELLED_ERROR_MESSAGE = "Product preparation has been cancelled!"
        private const val WRONG_USER_ERROR_MESSAGE =
                "Product receiver can be changed only by executing barista or receiving user!"
    }

}
