package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.Exceptions.InsufficientFunds;
import com.techelevator.tenmo.Exceptions.TransferNotFound;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class TenmoController {
    private final UserDao userDao;
    private final AccountDao accountDao;
    private final TransferDao transferDao;

    public TenmoController(UserDao userDao, AccountDao accountDao, TransferDao transferDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable Long id){
        return userDao.findById(id);
    }

    @RequestMapping(value = "/account/{id}/user", method = RequestMethod.GET )
    public User getUserByAccount(@PathVariable Long id){
        return userDao.findByAccountId(id);
    }

    @RequestMapping(value = "/user/{id}/balance", method = RequestMethod.GET)
    public Double getBalance(@PathVariable Long id){
        return accountDao.getBalance(id);
    }

    @RequestMapping(value = "/user/{id}/transfer", method = RequestMethod.POST)
    public Transfer sendMoney(@PathVariable Long id, @RequestParam Long recipientId, Double amount) throws InsufficientFunds, TransferNotFound {

        Account recipient = accountDao.getAccountByUserId(recipientId);
        Account sender = accountDao.getAccountByUserId(id);

        Transfer transferToReturn = transferDao.sendMoney(sender, recipient, amount);

        accountDao.updateAccountBalance((sender.getBalance() - amount), sender);
        accountDao.updateAccountBalance((recipient.getBalance() + amount), recipient);

        return transferToReturn;
    }

    @RequestMapping(value = "/user/{id}/transferhistory", method = RequestMethod.GET)
    public List<Transfer> getTransferHistory(@PathVariable Long id, @RequestParam Long transfersForUser){
        Account userAccount = accountDao.getAccountByUserId(transfersForUser);
        Long accountId = userAccount.getId();
        return transferDao.findAllByAccountID(accountId);
    }

    @RequestMapping(value ="/user/{id}/transfer/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable Long id, Long transferId, @RequestParam Long transIdParam) throws TransferNotFound {
        return transferDao.getTransfer(transIdParam);
    }


}
