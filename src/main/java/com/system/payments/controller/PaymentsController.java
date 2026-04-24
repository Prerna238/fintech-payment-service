package com.system.payments.controller;

import com.system.payments.entity.Payment;
import com.system.payments.model.PaymentsRequest;
import com.system.payments.service.PaymentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class PaymentsController {

    @Autowired
    private PaymentsService paymentsService;

    @RequestMapping(value= "/newPayment", method=RequestMethod.POST)
    public ResponseEntity<String> createANewPayment(@RequestBody PaymentsRequest payment){
        String res="";
        try{
            log.info("Payment initiated for {}", payment.getSourceAccount());
            res = paymentsService.recordPayment(payment);
            if(res.equals("Duplicate Payment")){
                log.info("Payment was already initiated for {}",payment.getSourceAccount());
                return new ResponseEntity<>(res,HttpStatusCode.valueOf(409));
            }
                log.info("Payment initiated successfully");
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        }catch(Exception e){
            log.error("Error while initiating payment for {}", payment.getSourceAccount());
            return new ResponseEntity<>(res,HttpStatus.INTERNAL_SERVER_ERROR);
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