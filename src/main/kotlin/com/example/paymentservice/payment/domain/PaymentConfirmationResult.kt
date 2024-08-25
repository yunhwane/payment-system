package com.example.paymentservice.payment.domain


data class PaymentConfirmationResult(
    val status: PaymentStatus,
    val failure: PaymentFailure? = null
)
{
    init {
        if(status == PaymentStatus.FAILURE) {
            requireNotNull(failure) { "PaymentStatus 값이 FAILURE 라면 PaymentExecutionFailure 는 null 이 되면 안됩니다. " }
        }
    }

    val message = when(status) {
        PaymentStatus.SUCCESS -> "결제 성공"
        PaymentStatus.FAILURE -> "결제 실패"
        PaymentStatus.UNKNOWN -> "결제 상태 알 수 없음"
        else -> error("현재 결제 상태 (status: $status) 는 지원하지 않습니다.")
    }
}
