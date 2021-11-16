package com.dev.unitests.service.impl;

import com.dev.unitests.model.entity.Loan;
import com.dev.unitests.service.EmailService;
import com.dev.unitests.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl {

    private static final String CRON_LATE_LOANS = "0 0 0 1/1 * ?";

    private final LoanService loanService;
    private final EmailService emailService;

    @Scheduled(cron = CRON_LATE_LOANS)
    public void sendMailToLateLoans(){
        List<Loan> allLateLoans = loanService.getAllLateLoans();
        List<String> emailsList = allLateLoans.stream().map(loan -> loan.getCustomer()).collect(Collectors.toList());

        String emailMessage = "Atenção, você tem um empréstimo atrasado!";

        emailService.sendMails(emailMessage, emailsList);
    }
}
