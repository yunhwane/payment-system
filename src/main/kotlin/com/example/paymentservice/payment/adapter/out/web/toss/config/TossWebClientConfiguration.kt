package com.example.paymentservice.payment.adapter.out.web.toss.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.util.*


@Configuration
class TossWebClientConfiguration (
    @Value("\${PSP.toss.url}")
    private val tossApiUrl: String,
    @Value("\${PSP.toss.secretKey}")
    private val tossApiKey: String
) {

    @Bean
    fun tossPaymentWebClient(): WebClient {
        val encodedApiKey = Base64.getEncoder().encodeToString(("$tossApiKey:").toByteArray(Charsets.UTF_8))

        return WebClient.builder()
            .baseUrl(tossApiUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic $encodedApiKey")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .clientConnector(reactorClientHttpConnector())
            .codecs { it.defaultCodecs() }
            .build()
    }

    private fun reactorClientHttpConnector(): ReactorClientHttpConnector {
        val provider = ConnectionProvider.builder("toss-payment")
            .build()

        return ReactorClientHttpConnector(HttpClient.create(provider))
    }
}