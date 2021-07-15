package com.techelevator.tenmo.services;

public class InsufficientFunds extends Exception {
    public InsufficientFunds(String message) {
        super(message);
    }
}
