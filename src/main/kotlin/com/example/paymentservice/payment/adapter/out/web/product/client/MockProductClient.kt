package com.example.paymentservice.payment.adapter.out.web.product.client

import com.example.paymentservice.common.WebAdapter
import com.example.paymentservice.payment.domain.Product
import reactor.core.publisher.Flux
import java.math.BigDecimal


@WebAdapter
class MockProductClient : ProductClient {
    override fun getProducts(cartId: Long, productIds: List<Long>): Flux<Product> {
        return Flux.fromIterable(
            productIds.map {
                Product(
                    id = it,
                    amount = BigDecimal(it*10000),
                    quantity = 2,
                    name = "test product_$it",
                    sellerId = 1
                )
            }
        )
    }

}