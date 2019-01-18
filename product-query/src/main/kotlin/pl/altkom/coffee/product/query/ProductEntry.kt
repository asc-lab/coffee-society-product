package pl.altkom.coffee.product.query


import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "product", type = "product")
data class ProductEntry(
        @Id val productId: String? = null,
        var productName: String? = null,
        var productReceiverId: String? = null,
        var active: Boolean = true)
