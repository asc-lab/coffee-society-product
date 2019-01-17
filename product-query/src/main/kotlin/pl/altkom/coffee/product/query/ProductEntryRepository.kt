package pl.altkom.coffee.product.query

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository

interface ProductEntryRepository : ElasticsearchCrudRepository<ProductEntry, String> {
    fun findByActive(active: Boolean): List<ProductEntry>
}

