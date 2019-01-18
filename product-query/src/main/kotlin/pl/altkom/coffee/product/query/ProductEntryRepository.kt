package pl.altkom.coffee.product.query

import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository

interface ProductEntryRepository : ElasticsearchCrudRepository<ProductEntry, String> {
    fun findByActive(active: Boolean, pageable: Pageable): List<ProductEntry>
}

