package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import com.techelevator.view.ConsoleService;

import java.text.ParseException;
import java.util.InputMismatchException;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
    private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
    private static final String[] LOGIN_MENU_OPTIONS = {LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT};
    private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
    private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
    private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
    private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
    private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
    private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
    private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT};

    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private TEnmoServices tenmoServices;

    public static void main(String[] args) {
        App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new TEnmoServices(API_BASE_URL));
        app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, TEnmoServices tenmoServices) {
        this.console = console;
        this.authenticationService = authenticationService;
        this.tenmoServices = tenmoServices;
    }

    public void run() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");

        registerAndLogin();
        mainMenu();
    }

    private void mainMenu() {
        while (true) {
            String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
            if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
                viewCurrentBalance(currentUser.getUser().getId(), currentUser.getToken());
            } else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
                viewTransferHistory();
            } else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
                viewPendingRequests();
            } else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
                sendBucks();
            } else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
                requestBucks();
            } else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else {
                // the only other option on the main menu is to exit
                exitProgram();
            }
        }
    }

    private void viewCurrentBalance(Integer userId, String token) {
        System.out.println("Your current balance is:" + tenmoServices.viewCurrentBalance(userId, token));


    }

    private void viewTransferHistory() {
        Transfer[] allTransfers;
        User sender = null;
        User recipient = null;
        allTransfers = tenmoServices.getAllTransfers(currentUser.getUser().getId(), currentUser.getToken());
        for (Transfer transfer : allTransfers) {
            sender = tenmoServices.getUserByAccountId(transfer.getSender(), currentUser.getToken());
            //finds the user who is sending by the account id which we get from transfer.getSender()
            recipient = tenmoServices.getUserByAccountId(transfer.getRecipient(), currentUser.getToken());
            //finds the user who is receiving
            console.displayAllTransfers(transfer, currentUser.getUser(), sender, recipient);
            //pass the transfer, as well as the users, into getTransfers so we can get the transfer info as well as the usernames
        }
        String userInputForTransDetails = console.getUserInput("Please enter transfer ID to view details (0 to cancel)");
        if (isNumeric(userInputForTransDetails) == true){
            Integer transferIdForDetails = Integer.parseInt(userInputForTransDetails);
            Transfer transferToDisplay = new Transfer();
            if (transferIdForDetails != 0) {
                try {
                    transferToDisplay = tenmoServices.getTransferDetails(currentUser.getUser().getId(), transferIdForDetails, currentUser.getToken());
                    console.displayTransferDetails(transferToDisplay, sender, recipient);
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                } catch (TransferNotFound e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            System.out.println("Please enter all input as numbers");
        }
    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub

    }

    private void sendBucks() {
        Integer recipientIdAsInt = null;
        Double amountAsDouble = null;
        User[] allUsers;
        allUsers = tenmoServices.getAllUsers(currentUser.getToken());
        console.sendInterface(allUsers);
        String recipientId = console.getUserInput("Enter the ID of the user you would like to send Bucks to");
        String amountToSend = console.getUserInput("Enter amount to send");
        if (isNumeric(recipientId) == true && isNumeric(amountToSend) == true) {
            recipientIdAsInt = Integer.parseInt(recipientId);
            amountAsDouble = Double.parseDouble(amountToSend);
            try {
                tenmoServices.sendMoney(currentUser.getUser().getId(), recipientIdAsInt, amountAsDouble, currentUser.getToken());
            } catch (InsufficientFunds e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Please enter all responses as numbers.");
        }

    }

    private void requestBucks() {

    }

    private void exitProgram() {
        System.exit(0);
    }

    private void registerAndLogin() {
        while (!isAuthenticated()) {
            String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
            if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
                register();
            } else {
                // the only other option on the login menu is to exit
                exitProgram();
            }
        }
    }

    private boolean isAuthenticated() {
        return currentUser != null;
    }

    private void register() {
        System.out.println("Please register a new user account");
        boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
                authenticationService.register(credentials);
                isRegistered = true;
                System.out.println("Registration successful. You can now login.");
            } catch (AuthenticationServiceException e) {
                System.out.println("REGISTRATION ERROR: " + e.getMessage());
                System.out.println("Please attempt to register again.");
            }
        }
    }

    private void login() {
        System.out.println("Please log in");
        currentUser = null;
        while (currentUser == null) //will keep looping until user is logged in
        {
            UserCredentials credentials = collectUserCredentials();
            try {
                currentUser = authenticationService.login(credentials);
            } catch (AuthenticationServiceException e) {
                System.out.println("LOGIN ERROR: " + e.getMessage());
                System.out.println("Please attempt to login again.");
            }
        }
    }

    private UserCredentials collectUserCredentials() {
        String username = console.getUserInput("Username");
        String password = console.getUserInput("Password");
        return new UserCredentials(username, password);
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
