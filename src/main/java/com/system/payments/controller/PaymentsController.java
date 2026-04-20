package com.system.payments.controller;

import com.system.payments.entity.Payment;
import com.system.payments.model.PaymentsRequest;
import com.system.payments.service.PaymentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class PaymentsController {

    @Autowired
    private PaymentsService paymentsService;

    @RequestMapping(value= "/newPayment", method=RequestMethod.POST)
    public String createANewPayment(@RequestBody PaymentsRequest payment){
        try{
            log.info("Payment initiated for {}", payment.getSourceAccount());
            Payment saved = paymentsService.recordPayment(payment);
            log.info("Payment initiated successfully");
            return "Payment created successfully";
        }catch(Exception e){
            log.error("Error while initiating payment for {}", payment.getSourceAccount());
            return "Error while recording payment";
        }
    }

    @RequestMapping(value= "/payment/{id}", method=RequestMethod.GET)
    public Payment getPayment(@PathVariable Long id){
        try{
            log.info("Trying to get payment info with id {}",id);
            return paymentsService.getPayment(id);
        }catch(Exception e){
            log.error("Error while trying to get payment info with id {}", id);
            return null;
        }
    }

}