package com.system.payments.exceptions;

public class DuplicatePaymentException extends RuntimeException{
    public DuplicatePaymentException(String message){
        super(message);
    }
}
