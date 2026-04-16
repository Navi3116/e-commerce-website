//package com.naveen.payment_service.listener;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//import com.naveen.payment_service.dto.OrderReadyForPaymentEvent;
//import com.naveen.payment_service.dto.PaymentProcessedEvent;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class PaymentListener {
//	
//	@Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;
//
//    @KafkaListener(topics = "order-ready-for-payment", groupId = "payment-group")
//    public void processPayment(OrderReadyForPaymentEvent event) {
//        log.info("Processing payment for Order: {}", event.getOrderId());
//
//        // Mock payment logic
//        boolean isSuccess = true; // In real life, call Stripe/PayPal API here
//
//        PaymentProcessedEvent result = new PaymentProcessedEvent();
//        result.setOrderId(event.getOrderId());
//        result.setStatus(isSuccess ? "SUCCESS" : "FAILED");
//        
//        // Publish result back to Kafka
//        kafkaTemplate.send("payment-result-topic", result);
//    }
//}
