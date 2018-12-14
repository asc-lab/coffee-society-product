package pl.altkom.coffee.product.query

import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
import pl.altkom.coffee.product.api.ProductPreparationStartedEvent
import pl.altkom.coffee.product.api.ProductReceiverChangedEvent


@Component
//@ProcessingGroup("queryModel")
class ProductEntryProjection(private val repository: ProductEntryRepository) {


    @EventHandler
    fun on(event: ProductPreparationStartedEvent) {
        repository.save(ProductEntry(event.productName, event.productReceiverName))
    }

    @EventHandler
    fun on(event: ProductReceiverChangedEvent) {
        repository.findById(event.id).ifPresent { productEntry ->
            productEntry.memberName = event.newProductReceiverName
            repository.save(productEntry)
        }
    }
}
