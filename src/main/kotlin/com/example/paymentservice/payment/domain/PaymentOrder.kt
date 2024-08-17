package com.example.paymentservice.payment.domain

import java.math.BigDecimal

/**
 * 장부에 기록했는지, 정산처리했는지에 대한 플래그 변수를 외부에서 변경하지 못하도록 private 로 설정
 */
data class PaymentOrder (
    val id: Long? = null,
    val paymentEventId: Long? = null,
    val sellerId: Long,
    val buyerId: Long,
    val productId: Long,
    val orderId: String,
    val amount: BigDecimal,
    val paymentStatus: PaymentStatus,
    private var isLedgerUpdated: Boolean = false,
    private val isWalletUpdated: Boolean = false

){

    fun isLedgerUpdated(): Boolean = isLedgerUpdated
    fun isWalletUpdated(): Boolean = isWalletUpdated
}