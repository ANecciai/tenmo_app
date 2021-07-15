package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

public class AccountDaoTests extends TenmoTests{
    private AccountDao accountDao;
    private JdbcTemplate jdbcTemplate;
    private static final Account ACCOUNT_1 = new Account(2001L, 1001L, 1000.00);
    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.accountDao = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void get_balance_returns_correct_balance(){
        Double expectedBalance = 1000.00;
        Double actualBalance = ACCOUNT_1.getBalance();
        Assert.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    public void get_account_by_account_id_returns_correct_account(){
       Account actualAccountReturned = accountDao.getAccountByAccountId(2001L);
        assertAccountsMatch(ACCOUNT_1, actualAccountReturned);
    }

    @Test
    public void get_account_by_user_id(){
        Account actualAccountReturned = accountDao.getAccountByUserId(1001L);
        assertAccountsMatch(ACCOUNT_1, actualAccountReturned);
    }

    @Test
    public void account_balance_updates_correctly(){
        Double actualAccountReturned = accountDao.getBalance(1001L);
        Assert.assertEquals(1000.00, actualAccountReturned, 0);
    }

    private void assertAccountsMatch(Account expected, Account actual){
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
    }
}
