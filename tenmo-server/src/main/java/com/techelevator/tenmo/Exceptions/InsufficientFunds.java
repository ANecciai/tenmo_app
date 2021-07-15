package com.techelevator.tenmo.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_MODIFIED, reason = "Amount to send exceeds balance for this account.")
public class InsufficientFunds extends Exception{
    private static final long serialVersionUID = 1L;

    public InsufficientFunds(String message) {
        super(message);
    }
}
