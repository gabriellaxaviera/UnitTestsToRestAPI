package com.dev.unitests.service.impl;

import com.dev.unitests.model.entity.Loan;
import com.dev.unitests.repository.LoanRepository;
import com.dev.unitests.service.LoanService;

public class LoanServiceImpl implements LoanService {

    private LoanRepository repository;

    public LoanServiceImpl(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        return repository.save(loan);
    }
}
