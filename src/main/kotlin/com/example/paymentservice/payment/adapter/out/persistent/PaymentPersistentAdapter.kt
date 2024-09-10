package com.example.paymentservice.payment.adapter.out.persistent

import com.example.paymentservice.common.PersistentAdapter
import com.example.paymentservice.payment.adapter.out.persistent.repository.PaymentRepository
import com.example.paymentservice.payment.adapter.out.persistent.repository.PaymentUpdateStatusRepository
import com.example.paymentservice.payment.adapter.out.persistent.repository.PaymentValidationRepository
import com.example.paymentservice.payment.application.port.out.*
import com.example.paymentservice.payment.domain.PaymentEvent
import com.example.paymentservice.payment.domain.PendingPaymentEvent
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@PersistentAdapter
class PaymentPersistentAdapter(
    private val paymentRepository: PaymentRepository,
    private val paymentUpdateStatusRepository: PaymentUpdateStatusRepository,
    private val paymentValidationRepository: PaymentValidationRepository
) : SavePaymentPort, PaymentStatusUpdatePort, PaymentValidationPort, LoadPendingPaymentPort{


    override fun save(paymentEvent: PaymentEvent): Mono<Void> {
        return paymentRepository.save(paymentEvent)
    }

    override fun updatePaymentStatusToExecuting(orderId: String, paymentKey: String): Mono<Boolean> {
        return paymentUpdateStatusRepository.updatePaymentStatusToExecuting(orderId, paymentKey)
    }

    override fun updatePaymentStatus(command: PaymentStatusUpdateCommand): Mono<Boolean> {
        return paymentUpdateStatusRepository.updatePaymentStatus(command)
    }

    override fun isValid(orderId: String, amount: Long): Mono<Boolean> {
        return paymentValidationRepository.isValid(orderId, amount)
    }

    override fun getPendingPayments(): Flux<PendingPaymentEvent> {
        return paymentRepository.getPendingPayments()
    }

}