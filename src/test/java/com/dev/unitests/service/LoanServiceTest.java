package com.dev.unitests.service;

import com.dev.unitests.exception.BusinessException;
import com.dev.unitests.model.entity.Book;
import com.dev.unitests.model.entity.Loan;
import com.dev.unitests.repository.LoanRepository;
import com.dev.unitests.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    @MockBean
    LoanRepository repository;

    LoanService loanService;

    @BeforeEach
    public void setup(){
        this.loanService = new LoanServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um emprestimo")
    public void shouldSaveLoanTest() {
        Book book = Book.builder().id(1L).build();

        Loan savindLoan = Loan.builder()
                .book(book)
                .customer("gabi")
                .loanDate(LocalDate.now())
                .build();

        Loan savedLoad = Loan.builder()
                .id(1L)
                .book(book)
                .customer("gabi")
                .loanDate(LocalDate.now())
                .build();

        Mockito.when(repository.save(savindLoan)).thenReturn(savedLoad);

        Loan loan = loanService.save(savindLoan);

        assertEquals(1L, loan.getId());
        assertEquals(1L, loan.getBook().getId());
        assertEquals("gabi", loan.getCustomer());
    }

    @Test
    @DisplayName("Deve lançar errod de negocio ao salvar um livro ja emprestado")
    public void shouldNotSaveLoanTest() {
        Book book = Book.builder().id(1L).build();

        Loan savindLoan = Loan.builder()
                .book(book)
                .customer("gabi")
                .loanDate(LocalDate.now())
                .build();

        when(repository.existsByBookAndNotReturned(book)).thenReturn(true); //se existe emprestimo para esse livro

        Throwable exception = catchThrowable(() -> loanService.save(savindLoan));

        assertEquals("Book already loaned",exception.getMessage());
        verify(repository, never()).save(savindLoan);
    }
}
