package com.dev.unitests.repository;

import com.dev.unitests.model.entity.Book;
import com.dev.unitests.model.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    boolean existsByBookAndNotReturned(Book book);
}
