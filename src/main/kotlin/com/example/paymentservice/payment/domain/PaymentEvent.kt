package com.example.paymentservice.payment.domain

import java.time.LocalDateTime

data class PaymentEvent(
    val id: Long? = null,
    val buyerId: Long,
    val orderId: String,
    val orderName: String,
    val paymentKey: String? = null,
    val paymentType: PaymentType? = null,
    val paymentMethod: PaymentMethod? = null,
    val approvedAt: LocalDateTime? = null,
    val paymentOrders: List<PaymentOrder> = emptyList(),
    private var isPaymentDone: Boolean = false
) {
    fun totalAmount(): Long {
        return paymentOrders.sumOf { it.amount }.toLong()
    }

    fun isPaymentDone(): Boolean = isPaymentDone
}
