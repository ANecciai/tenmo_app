package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TEnmoServices {

    private String baseURL;
    private RestTemplate restTemplate = new RestTemplate();

    public TEnmoServices(String url) {
        this.baseURL = url;
    }

    public Transfer getTransferDetails(Integer userId, Integer transferId, String token) throws TransferNotFound {
        Transfer transferDetails = null;
        try {
            transferDetails = restTemplate.exchange(baseURL + "user/" + userId + "/transfer/" + transferId + "?transIdParam=" + transferId, HttpMethod.GET, makeAuthEntity(token), Transfer.class).getBody();
        } catch (RestClientResponseException e) {
            System.out.println(e.getRawStatusCode() + " " + e.getStatusText());
        }
        if (transferDetails.getTransferId() == null) {
            throw new TransferNotFound("No transfer found for this ID.");
        } else {
            return transferDetails;
        }
    }


    public Transfer[] getAllTransfers(Integer userId, String token) {
        Transfer[] transfers = null;
        try {
            transfers = restTemplate.exchange(baseURL + "user/" + userId + "/transferhistory?transfersForUser=" + userId, HttpMethod.GET, makeAuthEntity(token), Transfer[].class).getBody();
        } catch (RestClientResponseException e) {
            System.out.println(e.getRawStatusCode() + " " + e.getStatusText());
        }
        return transfers;
    }

    public User[] getAllUsers(String token) {
        User[] users = null;
        try {
            users = restTemplate.exchange(baseURL + "user", HttpMethod.GET, makeAuthEntity(token), User[].class).getBody();
        } catch (RestClientResponseException e) {
            System.out.println(e.getRawStatusCode() + " " + e.getStatusText());
        }
        return users;
    }

    public Double viewCurrentBalance(Integer userId, String token) {
        Double balance = null;
        try {
            balance = restTemplate.exchange(baseURL + "user/" + userId + "/balance", HttpMethod.GET, makeAuthEntity(token), Double.class).getBody();
        } catch (RestClientResponseException e) {
            System.out.println(e.getRawStatusCode() + " " + e.getStatusText());
        }
        return balance;
    }

    public User getUserByAccountId(Long accountId, String token) {
        User user = new User();
        try {
            user = restTemplate.exchange(baseURL + "account/" + accountId + "/user", HttpMethod.GET, makeAuthEntity(token), User.class).getBody();
        } catch (RestClientResponseException e) {
            System.out.println(e.getRawStatusCode() + " " + e.getStatusText());
        }
        return user;
    }

    public Long sendMoney(Integer userId, Integer recipientID, Double amount, String token) throws InsufficientFunds {
        Transfer transfer = new Transfer();
        Double balance = viewCurrentBalance(userId, token);
        if (amount <= 0) {
            throw new InsufficientFunds("Please enter a number greater than zero.");
        }
        if (balance >= amount) {
            try {
                transfer = restTemplate.exchange(baseURL + "user/" + userId + "/transfer?recipientId=" + recipientID + "&amount=" + amount, HttpMethod.POST, makeAuthEntity(token), Transfer.class).getBody();
            } catch (RestClientResponseException e) {
                System.out.println(e.getRawStatusCode() + " " + e.getStatusText());
            }
            return transfer.getTransferStatus();
        } else {
            throw new InsufficientFunds("Transfer amount exceeds account balance");
        }
    }


    public Long requestMoney() {
        Transfer transfer = new Transfer();
        return transfer.getTransferStatus();
    }

    private HttpEntity makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
}
