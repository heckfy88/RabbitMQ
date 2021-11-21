package com.example.retailer.adapter

import com.example.retailer.api.distributor.OrderInfo
import com.example.retailer.service.OrderService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired

class RetailerConsumerImpl: RetailerConsumer {
    @Autowired
    private lateinit var orderService: OrderService

    @RabbitListener(queues = ["retailer"])
    override fun receive(message: String) {
        val orderInfo = ObjectMapper().readValue<OrderInfo>(message)
        orderService.updateOrderInfo(orderInfo)
    }
}