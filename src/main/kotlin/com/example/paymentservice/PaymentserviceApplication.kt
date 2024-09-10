package com.example.paymentservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class PaymentserviceApplication

fun main(args: Array<String>) {
    runApplication<PaymentserviceApplication>(*args)
}
