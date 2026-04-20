package com.system.payments.controller;

import com.system.payments.entity.Payment;
import com.system.payments.model.PaymentsRequest;
import com.system.payments.service.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PaymentsController {

    @Autowired
    private PaymentsService paymentsService;

    @RequestMapping(value= "/newPayment", method=RequestMethod.POST)
    public String createANewPayment(@RequestBody PaymentsRequest payment){
        try{
            Payment saved = paymentsService.recordPayment(payment);
            return "Payment created successfully";
        }catch(Exception e){
            return "Error while recording payment";
        }
    }

    @RequestMapping(value= "/payment/{id}", method=RequestMethod.GET)
    public Payment getPayment(@PathVariable Long id){
        try{
            return paymentsService.getPayment(id);
        }catch(Exception e){
            return null;
        }
    }

}