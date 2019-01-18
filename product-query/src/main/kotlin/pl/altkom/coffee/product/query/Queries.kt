package pl.altkom.coffee.product.query

class AllProductsQuery

data class ActiveProductsQuery(
        val pageSize: Int,
        val pageNumber: Int
)