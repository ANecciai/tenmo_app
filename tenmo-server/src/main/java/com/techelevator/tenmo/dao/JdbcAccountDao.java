package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exceptions.InsufficientFunds;
import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{
private JdbcTemplate jdbcTemplate;
public JdbcAccountDao(JdbcTemplate jdbcTemplate){this.jdbcTemplate = jdbcTemplate;}


    private Account mapRowToUser(SqlRowSet rs){
        Account account = new Account();
        account.setId(rs.getLong("account_id"));
        account.setUserId(rs.getLong("user_id"));
        account.setBalance(rs.getDouble("balance"));
        return account;
    }

    @Override
    public Double getBalance(Long userId) {
        String sql = "SELECT balance FROM accounts WHERE user_id = ?;";
        Double balance = jdbcTemplate.queryForObject(sql, Double.class, userId);
        if (balance != null) {
            return balance;
        }
        return null;
    }

    @Override
    public Account getAccountByAccountId(Long accountId){
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE account_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId);
        if (rowSet.next()){
            return mapRowToUser(rowSet);
        } return null;
    }

    @Override
    public Account getAccountByUserId(Long userId){
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        if (rowSet.next()){
            return mapRowToUser(rowSet);
        } return null;
    }

    @Override
    public Double updateAccountBalance(Double newBalance, Account accountToUpdate){
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, newBalance, accountToUpdate.getId());
        return accountToUpdate.getBalance();
    }
}
