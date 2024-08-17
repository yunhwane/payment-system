package com.example.paymentservice.payment.adapter.`in`.web.api

import com.example.paymentservice.common.WebAdapter
import com.example.paymentservice.payment.adapter.`in`.web.request.TossPaymentConfirmRequest
import com.example.paymentservice.payment.adapter.`in`.web.response.ApiResponse
import com.example.paymentservice.payment.adapter.out.web.toss.executer.TossPaymentExecutor
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import reactor.core.publisher.Mono


@WebAdapter
@RequestMapping("/v1/toss")
@RestController
class TossPaymentController (private val tossPaymentExecutor: TossPaymentExecutor) {


    @PostMapping("/confirm")
    fun confirmPayment(@RequestBody request: TossPaymentConfirmRequest): Mono<ResponseEntity<ApiResponse<String>>> {
        return tossPaymentExecutor.execute(
            paymentKey = request.paymentKey,
            orderId = request.orderId,
            amount = request.amount
        ).map {
            ResponseEntity.ok().body(
                ApiResponse.with(httpStatus = HttpStatus.OK, message = "Payment confirmed", data = it)
            )
        }
    }
}