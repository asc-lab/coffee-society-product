package pl.altkom.coffee.product.query

import mu.KLogging
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.axonframework.messaging.responsetypes.InstanceResponseType
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Component
import pl.altkom.coffee.product.api.ProductPreparationCancelledEvent
import pl.altkom.coffee.product.api.ProductPreparationRegisteredEvent
import pl.altkom.coffee.product.api.ProductReceiverChangedEvent
import pl.altkom.coffee.productcatalog.api.query.ProductNameQuery


@Component
@ProcessingGroup("ProductEntryProjection")
class ProductEntryProjection(private val repository: ProductEntryRepository, private val queryGateway: QueryGateway) {


    @EventHandler
    fun on(event: ProductPreparationRegisteredEvent) {
        val name = queryGateway.query(
                ProductNameQuery(event.productDefId), InstanceResponseType(String::class.java)).get()

        repository.save(ProductEntry(event.id, name, event.productReceiverId))
    }

    @EventHandler
    fun on(event: ProductReceiverChangedEvent) {
        repository.findById(event.id).ifPresent { productEntry ->
            productEntry.productReceiverId = event.productReceiverNewId
            repository.save(productEntry)
        }
    }

    @EventHandler
    fun on(event: ProductPreparationCancelledEvent) {
        logger.info("updating projectEntry after cancelation")
        repository.findById(event.id).ifPresent { productEntry ->
            productEntry.active = false
            repository.save(productEntry)
        }
    }

    @ResetHandler
    fun onReset() {
        repository.deleteAll()
    }

    companion object : KLogging()
}
