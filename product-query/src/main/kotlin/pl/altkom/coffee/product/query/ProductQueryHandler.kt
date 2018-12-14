package pl.altkom.coffee.product.query

import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class ProductQueryHandler(private val repository: ProductEntryRepository) {

    @QueryHandler
    fun getAllProducts(query: AllProductsQuery): List<ProductEntry> {
        return repository.findAll().toList()
    }
}