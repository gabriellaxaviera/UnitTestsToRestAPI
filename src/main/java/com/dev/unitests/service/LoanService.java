package com.dev.unitests.service;

import com.dev.unitests.model.entity.Loan;

import java.util.Map;
import java.util.Optional;

public interface LoanService {
    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    Loan update(Loan loan);
}
