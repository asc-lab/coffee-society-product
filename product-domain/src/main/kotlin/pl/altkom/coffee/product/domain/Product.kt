package pl.altkom.coffee.product.domain

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import pl.altkom.coffee.product.api.ProductPreparationCancelledEvent
import pl.altkom.coffee.product.api.ProductPreparationEndedEvent
import pl.altkom.coffee.product.api.ProductPreparationStartedEvent
import pl.altkom.coffee.product.api.ProductReceiverChangedEvent
import pl.altkom.coffee.product.api.enums.ProductState

@Aggregate
class Product {
    @AggregateIdentifier
    lateinit var id: String
    lateinit var productDefId: String
    lateinit var executorName: String
    lateinit var receiverName: String
    lateinit var state: ProductState

    constructor()

    @CommandHandler
    constructor(command: BeginProductPreparationCommand) {
        val executingUser = SecurityContextHolder.getContext().authentication.principal as User

        AggregateLifecycle.apply(
                ProductPreparationStartedEvent(command.id, command.productDefId,
                        command.productReceiverName, executingUser.username))
    }

    @EventSourcingHandler
    fun on(event: ProductPreparationStartedEvent) {
        this.id = event.id
        this.productDefId = event.productDefId
        this.receiverName = event.productReceiverName
        this.executorName = event.productExecutorName
        this.state = ProductState.IN_PREPARATION
    }

    @CommandHandler
    fun handle(command: EndProductPreparationCommand) {
        if (!isInPreparation()) {
            throw IllegalStateException(PREPARED_ERROR_MESSAGE)
        }

        if (isCancelled()) {
            throw IllegalStateException(CANCELLED_ERROR_MESSAGE)
        }

        AggregateLifecycle.apply(ProductPreparationEndedEvent(id))
    }

    @EventSourcingHandler
    fun on(event: ProductPreparationEndedEvent) {
        this.state = ProductState.PREPARED
    }

    @CommandHandler
    fun handle(command: CancelProductPreparationCommand) {
        if (!isInPreparation()) {
            throw IllegalStateException(PREPARED_ERROR_MESSAGE)
        }

        if (isCancelled()) {
            throw IllegalStateException(CANCELLED_ERROR_MESSAGE)
        }

        AggregateLifecycle.apply(ProductPreparationCancelledEvent(id))
    }

    @EventSourcingHandler
    fun on(event: ProductPreparationCancelledEvent) {
        this.id = event.id
        this.state = ProductState.CANCELLED
    }

    @CommandHandler
    fun handle(command: ChangeProductReceiverCommand) {
        val executingUser = SecurityContextHolder.getContext().authentication.principal as User

        if (isCancelled()) {
            throw IllegalStateException(CANCELLED_ERROR_MESSAGE)
        }
        if (!canChangeReceiver(executingUser.username)) {
            throw IllegalStateException(WRONG_USER_ERROR_MESSAGE)
        }

        AggregateLifecycle.apply(ProductReceiverChangedEvent(id, command.newProductReceiverName))
    }

    @EventSourcingHandler
    fun on(event: ProductReceiverChangedEvent) {
        this.receiverName = event.newProductReceiverName
    }

    private fun isInPreparation(): Boolean {
        return this.state == ProductState.IN_PREPARATION
    }

    private fun isCancelled(): Boolean {
        return this.state == ProductState.CANCELLED
    }

    private fun canChangeReceiver(username: String): Boolean {
        return this.receiverName == username || this.executorName == username

    }

    companion object {
        private const val PREPARED_ERROR_MESSAGE = "Product preparation already ended!"
        private const val CANCELLED_ERROR_MESSAGE = "Product preparation has been cancelled!"
        private const val WRONG_USER_ERROR_MESSAGE =
                "Product receiver can be changed only by executing barista or receiving user!"
    }

}
