package com.system.payments.controller;

import com.system.payments.entity.Payment;
import com.system.payments.model.PaymentsRequest;
import com.system.payments.service.PaymentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class PaymentsController {

    @Autowired
    private PaymentsService paymentsService;

    @Operation( summary="Create Payment",
            description="Creates a new payment and ensures idempotent processing.")
    @ApiResponses({
            @ApiResponse(responseCode="200", description="Payment Created Successfully"),
            @ApiResponse(responseCode="400", description="Invalid Request"),
            @ApiResponse(responseCode="409", description="Duplicate idempotency key")
    })
    @RequestMapping(value= "/newPayment", method=RequestMethod.POST)
    public ResponseEntity<String> createANewPayment(@Valid @RequestBody PaymentsRequest payment){
        String res="";
        try{
            log.info("Payment initiated for {}", payment.getSourceAccount());
            res = paymentsService.recordPayment(payment);
            if(res.equals("Duplicate Payment")){
                log.warn("Payment was already initiated for idempotencyKey:{}",payment.getIdempotencyKey());
                return new ResponseEntity<>(res,HttpStatusCode.valueOf(409));
            }
                log.info("Payment initiated successfully");
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        }catch(Exception e){
            log.error("Error while initiating payment for {}", payment.getSourceAccount());
            return new ResponseEntity<>(res,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation( summary="Get Payment details",
                description="Get the payment details of a particular payment Id")
    @ApiResponses({
            @ApiResponse(responseCode="200", description="Payment Created Successfully"),
            @ApiResponse(responseCode="400", description="Invalid Request"),
            @ApiResponse(responseCode="409", description="Duplicate idempotency key")
    })
    @RequestMapping(value= "/payment/{id}", method=RequestMethod.GET)
    public ResponseEntity<Payment> getPayment(@PathVariable Long id){
        try{
            log.info("Trying to get payment info with id {}",id);
            return new ResponseEntity<>(paymentsService.getPayment(id),HttpStatus.OK);
        }catch(Exception e){
            log.error("Error while trying to get payment info with id {}", id);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation( summary="Reconciliation mismatches",
                description="Returns payments whose ledger entries are missing or incomplete")
    @ApiResponses({
            @ApiResponse(responseCode="200", description="Payment Created Successfully"),
            @ApiResponse(responseCode="400", description="Invalid Request"),
            @ApiResponse(responseCode="409", description="Duplicate idempotency key")
    })
    @RequestMapping(value= "/reconcilliation/mismatch", method=RequestMethod.GET)
    public ResponseEntity<List<Payment>> paymentMismatch(){
        try{
            log.info("Trying to get all the reconciliation mismatches");
            return ResponseEntity.ok(paymentsService.mismatches());
        }catch(Exception e){
            log.error("Error while trying to get the reconciliation mismatches");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}