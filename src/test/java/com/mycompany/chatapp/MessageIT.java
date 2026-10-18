/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.chatapp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
public class MessageIT {
    
    public MessageIT() {
    }
    
  // A valid message ID contains ten digits
    @Test
    public void testMessageIDIsValid() {
        Message message = new Message(
                0,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
    }

    // A correctly formatted recipient number is accepted
    @Test
    public void testRecipientNumberIsCorrect() {
        Message message = new Message(
                0,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertEquals(
                "Cell phone number successfully captured.",
                message.checkRecipientCell()
        );
    }

    // A local-format recipient number is rejected
    @Test
    public void testRecipientNumberIsIncorrect() {
        Message message = new Message(
                1,
                "08575975889",
                "Hi Keegan, did you receive the payment?"
        );

        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                message.checkRecipientCell()
        );
    }

    // A message of 250 characters or less is accepted
    @Test
    public void testMessageLengthIsValid() {
        Message message = new Message(
                0,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertEquals("Message ready to send.", message.checkMessageLength());
    }

    // A message exceeding 250 characters is rejected
    @Test
    public void testMessageLengthIsInvalid() {
        String longMessage = "A".repeat(251);

        Message message = new Message(
                0,
                "+27718693002",
                longMessage
        );

        assertEquals(
                "Message exceeds 250 characters by 1; please reduce the size.",
                message.checkMessageLength()
        );
    }

    // Message hash uses the first and last words
    @Test
    public void testMessageHash() {
        Message message = new Message(
                0,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        String hash = message.createMessageHash();

        assertTrue(hash.matches("\\d{2}:0:HITONIGHT"));
    }

    // Sending a valid message should succeed
    @Test
    public void testMessageSent() {
        Message message = new Message(
                0,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertEquals(
                "Message successfully sent.",
                message.SentMessage("send")
        );
    }

    // Disregarding a message should return the required message
    @Test
    public void testMessageDisregarded() {
        Message message = new Message(
                1,
                "08575975889",
                "Hi Keegan, did you receive the payment?"
        );

        assertEquals(
                "Press 0 to delete the message.",
                message.SentMessage("disregard")
        );
    }

    // Storing a message should return the required message
    @Test
    public void testMessageStored() {
        Message message = new Message(
                0,
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertEquals(
                "Message successfully stored.",
                message.SentMessage("store")
        );
    }
}