package com.example.paymentservice.payment.test

import com.example.paymentservice.payment.domain.PaymentEvent
import reactor.core.publisher.Mono

interface PaymentDatabaseHelper {

    fun getPayment(orderId: String): PaymentEvent?
    fun clean(): Mono<Void>
}