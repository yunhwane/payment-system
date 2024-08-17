package com.example.paymentservice.payment.application.port.`in`

/**
 * idempotencyKey : 멱등성 키
 */
data class CheckoutCommand (
    val cartId: Long,
    val productIds: List<Long>,
    val buyerId: Long,
    val idempotencyKey: String
)