
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.chatapp;

import java.util.Scanner;

/**
 *
 * @author Student
 */
    public class ChatApp {

    public static void main(String[] args) {

    // Create a Scanner to read keyboard input.
    Scanner input = new Scanner(System.in);

    // Display the registration heading.
    System.out.println("==============================");
    System.out.println("    QUICKCHAT REGISTRATION");
    System.out.println("==============================");

    // Ask the user for their name.
    System.out.print("Enter name: ");
    String name = input.nextLine();

    // Ask the user for their surname.
    System.out.print("Enter surname: ");
    String surname = input.nextLine();

    // Ask the user to create a username.
    System.out.print("Enter username: ");
    String userName = input.nextLine();

    // Ask the user to create a password.
    System.out.print("Enter password: ");
    String password = input.nextLine();

    // Ask the user for their cellphone number.
    System.out.print("Enter cellphone number (+27718693002): ");
    String cellphone = input.nextLine();

    // Create a Login object using the registration details.
    Login user = new Login(userName, password, cellphone);

    // Display the results of the registration checks.
    System.out.println("\n========== REGISTRATION RESULTS ==========");
    System.out.println(user.getUserNameMessage());
    System.out.println(user.getPasswordMessage());
    System.out.println(user.getCellPhoneMessage());

    // Stop the program if any registration check fails.
    if (!user.registrationSuccessful()) {

    System.out.println("\nRegistration failed.");
    System.out.println("Please correct your details and try again.");

    input.close();
    return;
    }

    // Tell the user that registration was successful.
    System.out.println("\nRegistration successful!");

    // Display the login heading.
    System.out.println("\n========== QUICKCHAT LOGIN ==========");

    // Ask the user to enter their username.
    System.out.print("Enter username: ");
    String enteredUsername = input.nextLine();

    // Ask the user to enter their password.
    System.out.print("Enter password: ");
    String enteredPassword = input.nextLine();

    // Check whether the login details are correct.
    boolean loginSuccessful = user.loginUser(
    enteredUsername, enteredPassword);

    // Display the login result.
    System.out.println(user.returnLoginStatus(loginSuccessful));

    // Do not allow access to QuickChat if login fails.
    if (!loginSuccessful) {
    input.close();
    return;
    }

    // Welcome the successfully logged-in user.
    System.out.println("\nWelcome to QuickChat.");

    // Ask how many message entries the user wants to process.
    int messageLimit;

    // Repeat until the user enters zero or a positive whole number.
    do {

    System.out.print(
    "How many messages would you like to enter? ");

    // Check whether the input is a whole number.
    while (!input.hasNextInt()) {

    // Remove the invalid input.
    System.out.println("Please enter a whole number.");
    input.nextLine();

    System.out.print("Enter the number of messages: ");
    }

    // Read the number entered by the user.
    messageLimit = input.nextInt();

    // Clear the remaining newline from the Scanner.
    input.nextLine();

    // Do not accept a negative message count.
    if (messageLimit < 0) {
    System.out.println(
        "Please enter zero or a positive number.");
    }

    } while (messageLimit < 0);

    // Count message entries that have been processed.
    int messagesProcessed = 0;

    // Control whether the main menu continues running.
    boolean running = true;

    // Repeat the menu until the user chooses Quit.
    do {

    // Display the main menu.
    System.out.println("\n========== QUICKCHAT MENU ==========");
    System.out.println("1. Send Messages");
    System.out.println("2. Show recently sent messages");
    System.out.println("3. Quit");
    System.out.print("Choose an option: ");

    // Read the menu selection.
    String menuChoice = input.nextLine();

    // Perform an action based on the selected option.
    switch (menuChoice) {

    case "1":

    // Do not allow more entries than the selected limit.
    if (messagesProcessed >= messageLimit) {
        System.out.println(
                "You have reached your message limit.");
        break;
    }

    // Keep asking for messages until the limit is reached.
    do {

        // Display the current message number.
        System.out.println("\nMessage "
                + (messagesProcessed + 1)
                + " of " + messageLimit);

        // Ask for the recipient's number.
        System.out.print(
                "Enter recipient number (+27...): ");
        String recipient = input.nextLine();

        // Ask for the message text.
        System.out.print("Enter message: ");
        String messageText = input.nextLine();

        // Create a Message object with the information.
        // The message number starts at zero.
        Message message = new Message(
                messagesProcessed, recipient, messageText);

        // Check the recipient number before continuing.
        if (!message.checkRecipientCell().equals(
                "Cell phone number successfully captured.")) {

            System.out.println(
                    message.checkRecipientCell());
            System.out.println(
                    "Please enter this message again.");

            // Try again without increasing the count.
            continue;
        }

        // Check the message length.
        if (!message.checkMessageLength().equals(
                "Message ready to send.")) {

            System.out.println(
                    message.checkMessageLength());
            System.out.println(
                    "Please enter this message again.");

            // Try again without increasing the count.
            continue;
        }

        // Display the generated ID and hash.
        System.out.println("\nMessage ID: "
                + message.getMessageID());
        System.out.println("Message Hash: "
                + message.getMessageHash());

        // Ask the user what to do with the message.
        String action;

        // Repeat until the user enters a valid action.
        do {

            System.out.println("\n1. Send Message");
            System.out.println("2. Disregard Message");
            System.out.println("3. Store Message");
            System.out.print("Choose an option: ");

            action = input.nextLine();

            // Explain the valid options if the choice is wrong.
            if (!action.equals("1")
                    && !action.equals("2")
                    && !action.equals("3")) {

                System.out.println(
                        "Invalid choice. Please try again.");
            }

        } while (!action.equals("1")
                && !action.equals("2")
                && !action.equals("3"));

        // Call the correct Message method.
        String result;

        if (action.equals("1")) {
            result = message.SentMessage("send");

        } else if (action.equals("2")) {
            result = message.SentMessage("disregard");

        } else {
            result = message.SentMessage("store");
        }

        // Display the result of the selected action.
        System.out.println(result);

        // Display full details if the message was sent.
        if (action.equals("1")
                && result.equals(
                        "Message successfully sent.")) {

            System.out.println("\nMessage details:");

            System.out.println("Message ID: "
                    + message.getMessageID());

            System.out.println("Message Hash: "
                    + message.getMessageHash());

            System.out.println("Recipient: "
                    + message.getRecipient());

            System.out.println("Message: "
                    + message.getMessageText());
        }

        // Count sent, stored, and disregarded entries.
        // Invalid messages do not increase this count.
        if (result.equals("Message successfully sent.")
                || result.equals(
                        "Press 0 to delete the message.")
                || result.equals(
                        "Message successfully stored in memory.")) {

            messagesProcessed++;
        }

    } while (messagesProcessed < messageLimit);

    // Display the number of entries processed.
    System.out.println("\nMessages processed: "
            + messagesProcessed + " of " + messageLimit);

    break;

    case "2":

    // This menu option is required to show this exact text.
    System.out.println("Coming Soon.");
    break;

    case "3":

    // Create an object so we can access the total count.
    Message summary = new Message(
            0, "+27718693002", "Summary");

    // Display the number of messages actually sent.
    System.out.println("\nTotal messages sent: "
            + summary.returnTotalMessages());

    // End the application.
    System.out.println("Thank you for using QuickChat.");
    running = false;
    break;

    default:

    // Display an error if the menu choice is not valid.
    System.out.println(
            "Invalid option. Choose 1, 2, or 3.");
    }

    } while (running);

    // Close the Scanner when the program finishes.
    input.close();
    }
 }
