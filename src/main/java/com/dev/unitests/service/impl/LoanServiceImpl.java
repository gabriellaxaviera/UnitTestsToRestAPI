package com.dev.unitests.service.impl;

import com.dev.unitests.exception.BusinessException;
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
        if (repository.existsByBookAndNotReturned(loan.getBook())) {
            throw new BusinessException("Book already loaned");
        }
        return repository.save(loan);
    }
}
