package com.naveen.payment_service.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.naveen.payment_service.dto.OrderReadyForPaymentEvent;
import com.naveen.payment_service.dto.PaymentProcessedEvent;
import com.naveen.payment_service.entity.Payment;
import com.naveen.payment_service.repository.PaymentRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "order-ready-for-payment", groupId = "payment-group")
    public void handleOrderReady(OrderReadyForPaymentEvent event) {
        log.info("Processing payment for Order ID: {} for amount: {}", event.getOrderId(), event.getTotalAmount());

        // 1. Mock Payment Logic (In real life, call payment app here)
        boolean paymentSuccessful = true; 
        String txnId = UUID.randomUUID().toString();

        // 2. Save Payment record to DB
        Payment payment = new Payment();
        payment.setOrderId(event.getOrderId());
        payment.setAmount(event.getTotalAmount());
        payment.setTransactionId(txnId);
        payment.setStatus("SUCCESS");
        paymentRepository.save(payment);

        // 3. Publish the result back to Kafka
        
        PaymentProcessedEvent resultEvent = new PaymentProcessedEvent(
            event.getOrderId(),
            txnId,
            payment.getStatus(),
            "SUCCESS"
        );
        log.info("Publish the result back to payment-result-topic: {}", event.getOrderId());
        try {
        	log.info("Payment process event = " + "Order Id: "+resultEvent.getOrderId() + " Transaction id: "+txnId+ " Status: "+resultEvent.getStatus()
        	+ " Reason :"+resultEvent.getReason());
        	kafkaTemplate.send("payment-result-topic", resultEvent);
        }catch(Exception e) {
        	log.info("Excetion: " + e);
        }
        
        
        log.info("Payment result sent to Kafka for Order ID: {}", event.getOrderId());
    }
}
