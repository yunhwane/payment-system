package com.example.paymentservice.payment.adapter.`in`.web.view

import com.example.paymentservice.common.IdempotencyCreator
import com.example.paymentservice.common.WebAdapter
import com.example.paymentservice.payment.adapter.`in`.web.request.CheckoutRequest
import com.example.paymentservice.payment.application.port.`in`.CheckoutCommand
import com.example.paymentservice.payment.application.port.`in`.CheckoutUseCase
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import reactor.core.publisher.Mono


@Controller
@WebAdapter
class CheckoutController (
    private val checkoutUseCase: CheckoutUseCase
) {

    @GetMapping
    fun checkoutPage(checkoutRequest: CheckoutRequest, model: Model): Mono<String> {
        val command = CheckoutCommand(
            cartId = checkoutRequest.cartId,
            productIds = checkoutRequest.productIds,
            buyerId = checkoutRequest.buyerId,
            idempotencyKey = IdempotencyCreator.create(checkoutRequest.seed)
        )

        return checkoutUseCase.checkout(command)
            .map {
                model.addAttribute("orderId", it.orderId)
                model.addAttribute("orderName", it.orderName)
                model.addAttribute("amount", it.amount)
                "checkout"
            }
    }

}