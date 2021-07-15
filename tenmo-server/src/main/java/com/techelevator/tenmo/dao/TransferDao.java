package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exceptions.InsufficientFunds;
import com.techelevator.tenmo.Exceptions.TransferNotFound;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> findAllByAccountID(Long accountId);
    Transfer getTransfer(Long transferId) throws TransferNotFound;
    Transfer sendMoney(Account sender, Account recipient, Double amountToSend) throws InsufficientFunds, TransferNotFound;

}
