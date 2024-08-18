package com.example.paymentservice.payment.application.service

import com.example.paymentservice.payment.application.port.`in`.CheckoutCommand
import com.example.paymentservice.payment.application.port.`in`.CheckoutUseCase
import com.example.paymentservice.payment.application.port.`in`.PaymentConfirmCommand
import com.example.paymentservice.payment.application.port.out.PaymentExecutorPort
import com.example.paymentservice.payment.application.port.out.PaymentStatusUpdatePort
import com.example.paymentservice.payment.application.port.out.PaymentValidationPort
import com.example.paymentservice.payment.domain.*
import com.example.paymentservice.payment.test.PaymentDatabaseHelper
import com.example.paymentservice.payment.test.PaymentTestConfiguration
import io.mockk.MockK
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import reactor.core.publisher.Hooks
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*


@SpringBootTest
@Import(PaymentTestConfiguration::class)
class PaymentConfirmServiceTest(
    @Autowired private val checkoutUseCase: CheckoutUseCase,
    @Autowired private val paymentStatusUpdatePort: PaymentStatusUpdatePort,
    @Autowired private val paymentValidationPort: PaymentValidationPort,
    @Autowired private val paymentDatabaseHelper: PaymentDatabaseHelper,
) {
    private val mockPaymentExecutorPort = mockk<PaymentExecutorPort>()

    @BeforeEach
    fun setUp() {
        paymentDatabaseHelper.clean().block()
    }

    @Test
    fun `should be marked as SUCCESS if Payment Confirmation success in PSP`() {
        Hooks.onOperatorDebug()

        val orderId = UUID.randomUUID().toString()

        val checkoutCommand = CheckoutCommand(
            cartId = 1,
            buyerId = 1,
            productIds = listOf(1, 2, 3),
            idempotencyKey = orderId
        )

        val checkoutResult = checkoutUseCase.checkout(checkoutCommand).block()!!

        val paymentConfirmCommand = PaymentConfirmCommand(
            paymentKey = UUID.randomUUID().toString(),
            orderId = orderId,
            amount = checkoutResult.amount
        )

        val paymentConfirmService = PaymentConfirmService(
            paymentStatusUpdatePort = paymentStatusUpdatePort,
            paymentValidationPort = paymentValidationPort,
            paymentExecutorPort = mockPaymentExecutorPort,
        )

        val paymentExecutionResult = PaymentExecutionResult(
            paymentKey = paymentConfirmCommand.paymentKey,
            orderId = paymentConfirmCommand.orderId,
            extraDetails = PaymentExtraDetails(
                type = PaymentType.NORMAL,
                method = PaymentMethod.EASY_PAY,
                totalAmount = paymentConfirmCommand.amount,
                orderName = "test_order_name",
                pspConfirmationStatus = PSPConfirmationStatus.DONE,
                approvedAt = LocalDateTime.now(),
                pspRawData = "{}"
            ),
            isSuccess = true,
            isRetryable = false,
            isUnknown = false,
            isFailure = false
        )

        every { mockPaymentExecutorPort.execute(paymentConfirmCommand) } returns Mono.just(paymentExecutionResult)

        val paymentConfirmationResult = paymentConfirmService.confirm(paymentConfirmCommand).block()!!

        val paymentEvent = paymentDatabaseHelper.getPayments(orderId)!!

        assertThat(paymentConfirmationResult.status).isEqualTo(PaymentStatus.SUCCESS)
        assertTrue(paymentEvent.isSuccess())
        assertThat(paymentEvent.paymentType).isEqualTo(paymentExecutionResult.extraDetails!!.type)
        assertThat(paymentEvent.paymentMethod).isEqualTo(paymentExecutionResult.extraDetails!!.method)
        assertThat(paymentEvent.orderName).isEqualTo(paymentExecutionResult.extraDetails!!.orderName)
        assertThat(paymentEvent.approvedAt?.truncatedTo(ChronoUnit.MINUTES)).isEqualTo(paymentExecutionResult.extraDetails!!.approvedAt.truncatedTo(ChronoUnit.MINUTES))
    }

    @Test
    fun `should be marked as FAILURE if Payment Confirmation fails on PSP`() {
        Hooks.onOperatorDebug()

        val orderId = UUID.randomUUID().toString()

        val checkoutCommand = CheckoutCommand(
            cartId = 1,
            buyerId = 1,
            productIds = listOf(1, 2, 3),
            idempotencyKey = orderId
        )

        val checkoutResult = checkoutUseCase.checkout(checkoutCommand).block()!!

        val paymentConfirmCommand = PaymentConfirmCommand(
            paymentKey = UUID.randomUUID().toString(),
            orderId = orderId,
            amount = checkoutResult.amount
        )

        val paymentConfirmService = PaymentConfirmService(
            paymentStatusUpdatePort = paymentStatusUpdatePort,
            paymentValidationPort = paymentValidationPort,
            paymentExecutorPort = mockPaymentExecutorPort,
        )

        val paymentExecutionResult = PaymentExecutionResult(
            paymentKey = paymentConfirmCommand.paymentKey,
            orderId = paymentConfirmCommand.orderId,
            extraDetails = PaymentExtraDetails(
                type = PaymentType.NORMAL,
                method = PaymentMethod.EASY_PAY,
                totalAmount = paymentConfirmCommand.amount,
                orderName = "test_order_name",
                pspConfirmationStatus = PSPConfirmationStatus.DONE,
                approvedAt = LocalDateTime.now(),
                pspRawData = "{}"
            ),
            failure = PaymentExecutionFailure("ERROR", "Test Error"),
            isSuccess = false,
            isRetryable = false,
            isUnknown = false,
            isFailure = true
        )

        every { mockPaymentExecutorPort.execute(paymentConfirmCommand) } returns Mono.just(paymentExecutionResult)

        val paymentConfirmationResult = paymentConfirmService.confirm(paymentConfirmCommand).block()!!

        val paymentEvent = paymentDatabaseHelper.getPayments(orderId)!!

        assertThat(paymentConfirmationResult.status).isEqualTo(PaymentStatus.FAILURE)
        assertTrue(paymentEvent.isFailure())
    }
}