

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

/**
 *
 * @author Student
 */
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Message {

// These variables store the information for one message.
private String messageID;
private int messageNumber;
private String recipient;
private String messageText;
private String messageHash;
private String messageStatus;

// This array stores messages that have been successfully sent.
// The array can hold up to 1000 messages.
private static Message[] sentMessages = new Message[1000];

// This variable counts the total number of messages sent.
private static int totalMessages = 0;

// This is the file where stored messages will be saved.
private static final Path JSON_FILE = Path.of("messages.json");

// This list keeps messages that have been stored during this run.
private static List<Message> storedMessages = new ArrayList<>();

// The constructor runs when a new Message object is created.
public Message(int messageNumber, String recipient, String messageText) {

// Save the information received from ChatApp.
this.messageNumber = messageNumber;
this.recipient = recipient;
this.messageText = messageText;

// Generate a random ten-digit message ID.
long number = 1000000000L
        + (long) (Math.random() * 9000000000L);

// Convert the generated number into a String.
messageID = String.valueOf(number);

// Create a unique hash using the message information.
messageHash = createMessageHash();

// A new message starts with the Pending status.
messageStatus = "Pending";
}

// Check whether the message ID contains exactly ten digits.
public boolean checkMessageID() {
return messageID != null && messageID.matches("\\d{10}");
}

// Check whether the recipient's cellphone number is valid.
public String checkRecipientCell() {

// The number must start with +27 and have nine more digits.
if (recipient != null && recipient.matches("^\\+27\\d{9}$")) {
    return "Cell phone number successfully captured.";
}

return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
}

// Check whether the message contains 250 characters or fewer.
public String checkMessageLength() {

if (messageText != null && messageText.length() <= 250) {
    return "Message ready to send.";
}

int length = messageText == null ? 0 : messageText.length();
int extra = length - 250;

return "Message exceeds 250 characters by "
        + extra + "; please reduce the size.";
}

// Create a hash from the message ID, message number,
// first word and last word.
public String createMessageHash() {

String firstTwoDigits = messageID.substring(0, 2);

String text = messageText == null ? "" : messageText.trim();

// Handle an empty message without causing an error.
if (text.isEmpty()) {
    return (firstTwoDigits + ":" + messageNumber + ":")
            .toUpperCase();
}

// Split the message into separate words.
String[] words = text.split("\\s+");

// Get the first and last words and remove punctuation.
String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
String lastWord = words[words.length - 1]
        .replaceAll("[^a-zA-Z0-9]", "");

// Example format: 58:0:HITONIGHT
return (firstTwoDigits + ":" + messageNumber + ":"
        + firstWord + lastWord).toUpperCase();
}

// Send, disregard or store the message.
public String SentMessage(String choice) {

if (choice == null) {
    return "Invalid message option.";
}

if (choice.equalsIgnoreCase("send")) {

    // Check the message details before sending.
    if (!checkMessageID()
            || !checkRecipientCell().equals(
                    "Cell phone number successfully captured.")
            || !checkMessageLength().equals(
                    "Message ready to send.")) {

        return "Message could not be sent. Check the message details.";
    }

    // Check that there is space in the array.
    if (totalMessages < sentMessages.length) {

        // Add the message to the sent-message array.
        sentMessages[totalMessages] = this;
        totalMessages++;

        // Update the message status.
        messageStatus = "Sent";

        return "Message successfully sent.";
    }

    return "Message limit reached.";

} else if (choice.equalsIgnoreCase("disregard")
        || choice.equalsIgnoreCase("discard")) {

    // Mark the message as disregarded.
    messageStatus = "Disregarded";

    return "Press 0 to delete the message.";

} else if (choice.equalsIgnoreCase("store")) {

    // Store the message in the JSON file.
    if (storeMessage()) {
        messageStatus = "Stored";
        storedMessages.add(this);

        return "Message successfully stored in JSON.";
    }

    return "Message could not be stored.";

} else {

    return "Invalid message option.";
}
}

// Store this message in the messages.json file.
public boolean storeMessage() {

try {
    // Build a JSON object containing this message's details.
    String jsonObject = "{"
            + "\"messageID\":\"" + escapeJSON(messageID) + "\","
            + "\"messageNumber\":" + messageNumber + ","
            + "\"recipient\":\"" + escapeJSON(recipient) + "\","
            + "\"messageText\":\"" + escapeJSON(messageText) + "\","
            + "\"messageHash\":\"" + escapeJSON(messageHash) + "\","
            + "\"messageStatus\":\"Stored\""
            + "}";

    String existingJSON = "[]";

    // Read the existing JSON file if it already exists.
    if (Files.exists(JSON_FILE)) {
        existingJSON = Files.readString(JSON_FILE).trim();
    }

    // If the file is empty or does not contain a JSON array,
    // stop rather than accidentally corrupting the file.
    if (!existingJSON.startsWith("[")
            || !existingJSON.endsWith("]")) {
        return false;
    }

    // Remove the opening and closing square brackets.
    String contents = existingJSON.substring(
            1, existingJSON.length() - 1
    ).trim();

    // Add a comma if the array already contains messages.
    if (!contents.isEmpty()) {
        contents += ",";
    }

    // Add the new message and rebuild the JSON array.
    String updatedJSON = "[\n"
            + contents + "\n"
            + jsonObject + "\n"
            + "]";

    // Save the updated JSON file.
    Files.writeString(
            JSON_FILE,
            updatedJSON,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING,
            StandardOpenOption.WRITE
    );

    return true;

} catch (IOException e) {
    System.out.println("Error saving the message to JSON: "
            + e.getMessage());
    return false;
}
}

// Escape special characters so the message remains valid JSON.
private String escapeJSON(String text) {

if (text == null) {
    return "";
}

return text.replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\r", "\\r")
        .replace("\t", "\\t");
}

// Read the saved JSON file and return its contents.
public String readStoredMessages() {

try {
    if (Files.exists(JSON_FILE)) {
        return Files.readString(JSON_FILE);
    }

    return "No stored messages were found.";

} catch (IOException e) {
    return "Error reading stored messages.";
}
}

// Display all messages that have been sent during this run.
public String printMessages() {

if (totalMessages == 0) {
    return "No messages have been sent.";
}

String result = "";

// Loop through every message in the sent-message array.
for (int i = 0; i < totalMessages; i++) {

    result += "Message ID: "
            + sentMessages[i].messageID + "\n";

    result += "Message Hash: "
            + sentMessages[i].messageHash + "\n";

    result += "Recipient: "
            + sentMessages[i].recipient + "\n";

    result += "Message: "
            + sentMessages[i].messageText + "\n\n";
}

return result;
}

// Return the total number of successfully sent messages.
public int returnTotalMessages() {
return totalMessages;
}

// Return the message ID.
public String getMessageID() {
return messageID;
}

// Return the message number.
public int getMessageNumber() {
return messageNumber;
}

// Return the recipient's cellphone number.
public String getRecipient() {
return recipient;
}

// Return the message text.
public String getMessageText() {
return messageText;
}

// Return the message hash.
public String getMessageHash() {
return messageHash;
}

// Return the current message status.
public String getMessageStatus() {
return messageStatus;
}
}
