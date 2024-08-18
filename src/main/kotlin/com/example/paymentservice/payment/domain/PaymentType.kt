package com.example.paymentservice.payment.domain

enum class PaymentType (val description: String) {
    NORMAL("일반 결제");


    companion object {
        fun get(type: String): PaymentType {
            return entries.find { it.name == type } ?: error("결제 타입 (type: $type) 는 올바르지 않은 타입입니다.")
        }
    }
}