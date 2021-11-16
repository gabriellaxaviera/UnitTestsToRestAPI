package com.dev.unitests.repository;

import com.dev.unitests.model.entity.Book;
import com.dev.unitests.model.entity.Loan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static com.dev.unitests.repository.BookRepositoryTest.createNewBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    private LoanRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("deve verificar se existe empréstimo não devolvido para o livro.")
    public void existsByBookAndNotReturnedTest() {
        Loan loan = createAndPersistLoan(LocalDate.now());
        Book book = loan.getBook();

        //execucao
        boolean exists = repository.existsByBookAndNotReturned(book);

        assertTrue(exists);
    }

    @Test
    @DisplayName("Deve buscar empréstimo pelo isbn do livro ou customer")
    public void findByBookIsbnOrCustomerTest(){
        Loan loan = createAndPersistLoan(LocalDate.now());

        Page<Loan> result = repository.findByBookIsbnOrCustomer(
                "123", "Fulano", PageRequest.of(0, 10));

        assertEquals(1, result.getContent().size());
        assertEquals(loan, result.getContent().get(0));
        assertEquals(10, result.getPageable().getPageSize());
        assertEquals(0, result.getPageable().getPageNumber());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Deve obter emprestimos cuja data de emprestimo for menor a 3 dias e nao retornados")
    public void findByLoanDateLessThanAndNotReturnedTest(){
        Loan loan = createAndPersistLoan(LocalDate.now().minusDays(5));

        List<Loan> loanDateLessThanAndNotReturned = repository.findByLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));

        assertEquals(1, loanDateLessThanAndNotReturned.size());
    }

    @Test
    @DisplayName("Deve retornar vazio quando não houver emprestimos atrasados")
    public void notFindByLoanDateLessThanAndNotReturnedTest(){
        Loan loan = createAndPersistLoan(LocalDate.now());

        List<Loan> loanDateLessThanAndNotReturned = repository.findByLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));

        assertEquals(0, loanDateLessThanAndNotReturned.size());
    }

    private Loan createAndPersistLoan(LocalDate localDate) {
        Book book = createNewBook("123");
        entityManager.persist(book);

        Loan loan = Loan.builder().book(book).customer("Fulano").loanDate(localDate).build();
        entityManager.persist(loan);

        return loan;
    }

}
