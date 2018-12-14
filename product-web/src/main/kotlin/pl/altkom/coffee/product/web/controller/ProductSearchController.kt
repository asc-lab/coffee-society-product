package pl.altkom.coffee.product.web.controller

import org.axonframework.messaging.responsetypes.MultipleInstancesResponseType
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.altkom.coffee.product.query.AllProductsQuery
import pl.altkom.coffee.product.query.ProductEntry
import java.util.concurrent.Future

@RestController
@RequestMapping("/api/search")
class ProductSearchController(private val queryGateway: QueryGateway) {

    @GetMapping("/products")
    fun findAll(): Future<List<ProductEntry>> =
            queryGateway.query(AllProductsQuery(), MultipleInstancesResponseType(ProductEntry::class.java))
}
