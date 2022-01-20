package com.drh.flummox.utilities.mail;

import org.junit.jupiter.api.Test;

class EmailSenderTest {

    @Test
    public void test(){
        new EmailSender().send("Subject", "Message");
    }

}