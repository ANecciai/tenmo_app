package com.techelevator.tenmo.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (code = HttpStatus.NOT_FOUND, reason = "No transfer found with this ID.")
public class TransferNotFound extends Exception {
    public TransferNotFound(String message) {
        super(message);
    }
}
