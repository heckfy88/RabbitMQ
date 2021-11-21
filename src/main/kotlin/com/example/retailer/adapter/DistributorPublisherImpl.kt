package com.example.retailer.adapter

import com.example.retailer.api.distributor.Order
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired

class DistributorPublisherImpl: DistributorPublisher {
    @Autowired
    private lateinit var template: RabbitTemplate
    @Autowired
    private lateinit var topic: TopicExchange

    override fun placeOrder(order: Order): Boolean {
        val orderMessage = ObjectMapper().writeValueAsString(order)
        return if (order.id != null) {
            template.convertAndSend(topic.name, "distributor.placeOrder.heckfy88.${order.id}", orderMessage) {
                msg ->
                msg.messageProperties.headers["Notify-Exchange"] = "distributor_exchange"
                msg.messageProperties.headers["Notify-RoutingKey"] = "retailer.heckfy88"
                msg
            }
            true
        } else {
            println("order id == null !")
            false
        }
    }
}