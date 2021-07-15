package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exceptions.InsufficientFunds;
import com.techelevator.tenmo.model.Account;


public interface AccountDao {

   Double getBalance(Long userId);
   Account getAccountByAccountId(Long accountId);
   Account getAccountByUserId(Long userId);
   Double updateAccountBalance(Double newBalance, Account accountToUpdate);
}
