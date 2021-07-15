package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exceptions.InsufficientFunds;
import com.techelevator.tenmo.Exceptions.TransferNotFound;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.annotation.ServletSecurity;
import java.util.ArrayList;
import java.util.List;

public class TransferDaoTests extends TenmoTests {
    private TransferDao transferDao;
    private JdbcTemplate jdbcTemplate;
    private static final Account ACCOUNT_1 = new Account(2001L, 1001L, 1000.00);
    private static final Account ACCOUNT_2 = new Account(2002L, 1002L, 1000.00);
    private static final Transfer TRANSFER_1 = new Transfer(3001L, 2L, 2L, 2001L, 2002L, 100.00);
    private static final Transfer TRANSFER_2 = new Transfer(3002L, 2L, 2L, 2003L, 2004L, 100.00);
    private static final Transfer TRANSFER_3 = new Transfer(3003L, 2L, 2L, 2001L, 2003L, 100.00);
    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.transferDao = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void get_transaction_returns_correct_transaction() throws TransferNotFound {
        Transfer actualTransferReturned = transferDao.getTransfer(3001L);
        assertTransfersMatch(TRANSFER_1, actualTransferReturned);

    }

    @Test
    public void get_all_transactions_by_account(){
        List<Transfer> ALL_TRANSFERS = new ArrayList<>();
        ALL_TRANSFERS.add(TRANSFER_1);
        ALL_TRANSFERS.add(TRANSFER_3);

        List<Transfer> actualList = transferDao.findAllByAccountID(2001L);
        Assert.assertEquals(ALL_TRANSFERS.size(), actualList.size());
        assertTransfersMatch(ALL_TRANSFERS.get(0), actualList.get(0));
        assertTransfersMatch(ALL_TRANSFERS.get(1), actualList.get(1));
    }

    @Test
    public void send_money_inserts_transfer() throws InsufficientFunds, TransferNotFound {
        int numberOfTransferBeforeSend = transferDao.findAllByAccountID(2001L).size();
        transferDao.sendMoney(ACCOUNT_1, ACCOUNT_2, 100.00);
        int numberOfTransfersAfterSend = transferDao.findAllByAccountID(2001L).size();
        Assert.assertEquals(numberOfTransferBeforeSend + 1, numberOfTransfersAfterSend);
    }

    private void assertTransfersMatch (Transfer expected, Transfer actual){
        Assert.assertEquals(expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(expected.getTransferTypeId(), actual.getTransferTypeId());
        Assert.assertEquals(expected.getTransferStatus(), actual.getTransferStatus());
        Assert.assertEquals(expected.getSender(), actual.getSender());
        Assert.assertEquals(expected.getRecipient(), actual.getRecipient());
        Assert.assertEquals(expected.getAmount(), actual.getAmount());
    }
}