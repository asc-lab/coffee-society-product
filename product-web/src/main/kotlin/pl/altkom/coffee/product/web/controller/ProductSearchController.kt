package pl.altkom.coffee.product.web.controller

import org.axonframework.messaging.responsetypes.MultipleInstancesResponseType
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.altkom.coffee.product.query.ActiveProductsQuery
import pl.altkom.coffee.product.query.AllProductsQuery
import pl.altkom.coffee.product.query.ProductEntry
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/search")
class ProductSearchController(private val queryGateway: QueryGateway) {

    @GetMapping("/products")
    fun findActive(
            @RequestParam(name = "pageSize", defaultValue = "50") pageSize: Int,
            @RequestParam(name = "pageNumber", defaultValue = "0") pageNumber: Int
    ): Mono<List<ProductEntry>> =
            Mono.fromFuture(queryGateway.query(ActiveProductsQuery(pageSize, pageNumber), MultipleInstancesResponseType(ProductEntry::class.java)))

    @GetMapping("/all-products")
    fun findAll(): Mono<List<ProductEntry>> =
            Mono.fromFuture(queryGateway.query(AllProductsQuery(), MultipleInstancesResponseType(ProductEntry::class.java)))
}
