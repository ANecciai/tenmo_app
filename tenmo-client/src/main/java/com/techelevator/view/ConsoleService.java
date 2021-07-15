package com.techelevator.view;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;

	public ConsoleService(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}

	public String getUserInput(String prompt) {
		out.print(prompt+": ");
		out.flush();
		return in.nextLine();
	}

	public Integer getUserInputInteger(String prompt) {
		Integer result = null;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}

	public void displayTransferDetails(Transfer transfer, User sender, User recipient) {
		System.out.println("-----------------------");
		System.out.println("Transfer Details");
		System.out.println("-----------------------");
		System.out.println("Id: " + transfer.getTransferId());
		System.out.println("From: " + sender.getUsername()); //need to figure out how to get the username to display but using user_id as a place holder
		System.out.println("To: " + recipient.getUsername());
		if (transfer.getTransferTypeId().equals(1)){
			System.out.println("Type: Request");
		} else if (transfer.getTransferId().equals(2)){
		System.out.println("Type: Send");}
		if (transfer.getTransferStatus().equals(1)){
			System.out.println("Status: Pending");
		} else if (transfer.getTransferStatus().equals(2)){
			System.out.println("Status: Approved");
		} else if (transfer.getTransferStatus().equals(3)){
			System.out.println("Status: Rejected");
		}
		System.out.println("Amount: " + transfer.getAmount());
	}

	public void displayAllTransfers(Transfer transfer, User currentUser, User sender, User recipient) {
		System.out.println("---------------------------------");
		System.out.println("Transfers");
		System.out.println("ID         From/To       Amount");
		System.out.println("---------------------------------");
		if (sender.getId().equals(currentUser.getId())){
			System.out.println(transfer.getTransferId() + "        " + "To: " + recipient.getUsername() +  "         $" + transfer.getAmount());
		} else {
			System.out.println(transfer.getTransferId() + "        " + "From: " + sender.getUsername() +  "         $" + transfer.getAmount());}

	}

	public void requestTEBucks(User[] options) {
		System.out.println("-------------------------");
		System.out.println("Users");
		System.out.println("ID           Name");
		System.out.println("-------------------------");
		for (User user : options) {
			System.out.println(user.getId() + "           " + user.getUsername());
		}
	}

	public void sendInterface(User[] options){
		System.out.println("-------------------------");
		System.out.println("Users");
		System.out.println("ID           Name");
		System.out.println("-------------------------");
		for (User user : options){
			System.out.println(user.getId() + "           " + user.getUsername());
		}

	}
}
