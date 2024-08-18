package com.example.paymentservice.payment.application.port.`in`

import com.example.paymentservice.payment.domain.PaymentConfirmationResult
import reactor.core.publisher.Mono

interface PaymentConfirmUseCase {

    fun confirm(command: PaymentConfirmCommand): Mono<PaymentConfirmationResult>
}