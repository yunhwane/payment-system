package com.example.paymentservice.payment.domain

enum class PaymentMethod(val description: String) {
    EASY_PAY("간편 결제");

    companion object {
        fun get(method: String): PaymentMethod {
            return entries.find { it.name == method } ?: error("결제 수단 (method: $method) 는 올바르지 않은 수단입니다.")
        }
    }
}