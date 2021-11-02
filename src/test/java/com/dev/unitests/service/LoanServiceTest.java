package com.dev.unitests.service;

import com.dev.unitests.api.dto.LoanFilterDTO;
import com.dev.unitests.model.entity.Book;
import com.dev.unitests.model.entity.Loan;
import com.dev.unitests.repository.LoanRepository;
import com.dev.unitests.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    @MockBean
    LoanRepository repository;

    LoanService loanService;

    @BeforeEach
    public void setup() {
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

        when(repository.existsByBookAndNotReturned(book)).thenReturn(false);
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

        Loan savingLoan = Loan.builder()
                .book(book)
                .customer("gabi")
                .loanDate(LocalDate.now())
                .build();

        when(repository.existsByBookAndNotReturned(book)).thenReturn(true); //se existe emprestimo para esse livro

        Throwable exception = catchThrowable(() -> loanService.save(savingLoan));

        assertEquals("Book already loaned", exception.getMessage());
        verify(repository, never()).save(savingLoan);
    }

    @Test
    @DisplayName(" Deve obter as informações de um empréstimo pelo ID")
    public void getLoanDetaisTest() {
        //cenário
        Long id = 1L;
        Loan loan = createLoan();
        loan.setId(id); //simula o que foi salvo no banco

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(loan));

        //execucao
        Optional<Loan> result = loanService.getById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertEquals(loan.getCustomer(), result.get().getCustomer());
        assertEquals(loan.getBook(), result.get().getBook());
    }

    @Test
    @DisplayName("Deve atualizar um empréstimo.")
    public void updateLoanTest() {
        Loan loan = createLoan();
        loan.setId(1L);
        loan.setReturned(true);

        when(repository.save(loan)).thenReturn(loan);

        Loan updatedLoan = loanService.update(loan);

        assertTrue(updatedLoan.getReturned());
        verify(repository).save(loan);
    }

    @Test
    @DisplayName("Deve filtrar empréstimos pelas propriedades")
    public void findLoanTest() {
        //cenario
        LoanFilterDTO loanFilterDTO = LoanFilterDTO.builder().customer("Fulano").isbn("321").build();

        Loan loan = createLoan();
        loan.setId(1L);
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Loan> lista = Arrays.asList(loan);

        Page<Loan> page = new PageImpl<>(lista, pageRequest, lista.size());
        when(repository.findByBookIsbnOrCustomer(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(PageRequest.class)))
                .thenReturn(page);

        //execucao
        Page<Loan> result = loanService.find(loanFilterDTO, pageRequest);

        //verificacoes
        assertEquals(1, result.getTotalElements());
        assertEquals(lista, result.getContent());
        assertEquals(0, result.getPageable().getPageNumber());
        assertEquals(10, result.getPageable().getPageSize());
    }

    public static Loan createLoan() {
        Book book = Book.builder().id(1L).isbn("123").build();
        String customer = "Fulano";

        return Loan.builder()
                .book(book)
                .customer(customer)
                .loanDate(LocalDate.now())
                .build();
    }
}
