package com.example.paymentservice.payment.adapter.`in`.web.request

import java.time.LocalDateTime

/**
 * 요청을 구별하기 위해 seed
 */
data class CheckoutRequest (
    val cartId: Long = 1,
    val productIds: List<Long> = listOf(1, 2, 3),
    val buyerId: Long = 1,
    val seed: String = LocalDateTime.now().toString()
)