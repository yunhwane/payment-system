package com.example.paymentservice.payment.application.port.out

import reactor.core.publisher.Mono


/**
 * 결제 상태를 변경하기 위한, 즉, 복구를 위한 결제 상태 execute로 변경하는 포트
 */
interface PaymentStatusUpdatePort {

    fun updatePaymentStatusToExecuting(orderId: String, paymentKey: String): Mono<Boolean>

    fun updatePaymentStatus(command: PaymentStatusUpdateCommand): Mono<Boolean>
}