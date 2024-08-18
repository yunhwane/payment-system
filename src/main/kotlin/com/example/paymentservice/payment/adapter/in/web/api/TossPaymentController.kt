package com.example.paymentservice.payment.adapter.`in`.web.api

import com.example.paymentservice.common.WebAdapter
import com.example.paymentservice.payment.adapter.`in`.web.request.TossPaymentConfirmRequest
import com.example.paymentservice.payment.adapter.`in`.web.response.ApiResponse
import com.example.paymentservice.payment.adapter.out.web.toss.executer.TossPaymentExecutor
import com.example.paymentservice.payment.application.port.`in`.PaymentConfirmCommand
import com.example.paymentservice.payment.application.port.`in`.PaymentConfirmUseCase
import com.example.paymentservice.payment.domain.PaymentConfirmationResult
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
class TossPaymentController (private val paymentConfirmUseCase: PaymentConfirmUseCase) {


    @PostMapping("/confirm")
    fun confirmPayment(@RequestBody request: TossPaymentConfirmRequest): Mono<ResponseEntity<ApiResponse<PaymentConfirmationResult>>> {
        val command = PaymentConfirmCommand(
            orderId = request.orderId,
            paymentKey = request.paymentKey,
            amount = request.amount
        )

        return paymentConfirmUseCase.confirm(command)
            .map { ResponseEntity.ok().body(ApiResponse.with(httpStatus = HttpStatus.OK, "", it)) }
    }
}