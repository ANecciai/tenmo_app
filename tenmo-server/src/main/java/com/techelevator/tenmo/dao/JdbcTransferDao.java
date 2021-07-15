package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exceptions.InsufficientFunds;
import com.techelevator.tenmo.Exceptions.TransferNotFound;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {


    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> findAllByAccountID(Long accountId){
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers " +
                "WHERE account_from = ? OR account_to = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while (results.next()){
            Transfer transfer = new Transfer();
            transfer = mapToRowSet(results);
            transferList.add(transfer);
        } return transferList;
    }

    @Override
    public Transfer getTransfer(Long transferId) throws TransferNotFound {
        Transfer transfer = new Transfer();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfers WHERE transfer_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, transferId);
        if (rowSet.next()){
            transfer = mapToRowSet(rowSet);
        } if (transferId == null){
            throw new TransferNotFound("No transfer found with this ID.");
        } else {return transfer;}
    }

    @Override
    public Transfer sendMoney(Account sender, Account recipient, Double amountToSend) throws InsufficientFunds, TransferNotFound {
        Transfer transfer = new Transfer();
        Long transferId = null;
        Long senderId = sender.getId();
        Long recipientId = recipient.getId();
        if (sender.getBalance() >= amountToSend) {
            String sql = "INSERT INTO transfers VALUES (default, 2, 2, ?, ?, ?) RETURNING transfer_id;";
            transferId = jdbcTemplate.queryForObject(sql, Long.class, senderId, recipientId, amountToSend);
            return getTransfer(transferId);
        } else {throw new InsufficientFunds("Transfer amount exceeds account balance");}
    }

    private Transfer mapToRowSet(SqlRowSet rs){
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getLong("transfer_id"));
        transfer.setTransferTypeId(rs.getLong("transfer_type_id"));
        transfer.setTransferStatus(rs.getLong("transfer_status_id"));
        transfer.setSender(rs.getLong("account_from"));
        transfer.setRecipient(rs.getLong("account_to"));
        transfer.setAmount(rs.getDouble("amount"));

        return transfer;
    }
}
