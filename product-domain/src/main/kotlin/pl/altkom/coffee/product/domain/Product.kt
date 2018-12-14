package pl.altkom.coffee.product.domain

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import pl.altkom.coffee.product.api.ProductPreparationCancelled
import pl.altkom.coffee.product.api.ProductPreparationEndedEvent
import pl.altkom.coffee.product.api.ProductPreparationStartedEvent
import pl.altkom.coffee.product.api.ProductReceiverChangedEvent

@Aggregate
class Product {
    @AggregateIdentifier
    lateinit var id: String
    lateinit var productDefId: String
    lateinit var receiverName: String
    private var inPreparation: Boolean = false

    constructor()

    @CommandHandler
    constructor(command: BeginProductPreparationCommand) {
        AggregateLifecycle.apply(
                ProductPreparationStartedEvent(command.id, command.productDefId, command.productReceiverName, command.productName))
    }

    @EventSourcingHandler
    fun on(event: ProductPreparationStartedEvent) {
        this.id = event.id
        this.productDefId = event.productDefId
        this.receiverName = event.productReceiverName
        this.inPreparation = true
    }

    @CommandHandler
    fun handle(command: EndProductPreparationCommand) {
        if (!inPreparation) {
            throw IllegalStateException("product preparation already ended!")
        }

        AggregateLifecycle.apply(ProductPreparationEndedEvent(id))
    }

    @EventSourcingHandler
    fun on(event: ProductPreparationEndedEvent) {
        this.inPreparation = false
    }

    @EventSourcingHandler
    fun on(event: ProductPreparationCancelled) {
        this.id = event.id
        this.inPreparation = false
    }

    @CommandHandler
    fun handle(command: CancelProductPreparationCommand) {
        if (!inPreparation) {
            throw IllegalStateException("product preparation already ended!")
        }

        AggregateLifecycle.apply(ProductPreparationEndedEvent(id))
    }

    @EventSourcingHandler
    fun on(event: ProductReceiverChangedEvent) {
        this.receiverName = event.newProductReceiverName
    }

    @CommandHandler
    fun handle(command: ChangeProductReceiverCommand) {
        AggregateLifecycle.apply(ProductReceiverChangedEvent(id, command.newProductReceiverName))
    }

}
