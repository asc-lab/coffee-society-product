package pl.altkom.coffee.product.query

import org.axonframework.eventhandling.EventHandler
import org.axonframework.messaging.responsetypes.InstanceResponseType
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Component
import pl.altkom.coffee.product.api.ProductPreparationRegisteredEvent
import pl.altkom.coffee.product.api.ProductReceiverChangedEvent
import pl.altkom.coffee.productcatalog.api.query.ProductNameQuery


@Component
//@ProcessingGroup("queryModel")
class ProductEntryProjection(private val repository: ProductEntryRepository, private val queryGateway: QueryGateway) {


    @EventHandler
    fun on(event: ProductPreparationRegisteredEvent) {
        val name = queryGateway.query(
                ProductNameQuery(event.productDefId), InstanceResponseType(String::class.java)).get()

        repository.save(ProductEntry(name, event.productReceiverId))
    }

    @EventHandler
    fun on(event: ProductReceiverChangedEvent) {
        repository.findById(event.id).ifPresent { productEntry ->
            productEntry.memberName = event.productReceiverNewId
            repository.save(productEntry)
        }
    }
}
