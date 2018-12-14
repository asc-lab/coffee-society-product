package pl.altkom.coffee.product.query


import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "product", type = "product")
data class ProductEntry(var productName: String? = null, var memberName: String? = null) {
    @Id
    private val productId: String? = null

}
