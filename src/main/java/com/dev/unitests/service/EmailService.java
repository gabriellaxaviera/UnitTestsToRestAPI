package com.dev.unitests.service;

import java.util.List;

public interface EmailService {

    void sendMails(String emailMessage, List<String> emailsList);
}
