package com.dev.unitests.service;

import com.dev.unitests.exception.BusinessException;
import com.dev.unitests.model.entity.Book;
import com.dev.unitests.repository.BookRepository;
import com.dev.unitests.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService bookService;

    @MockBean
    BookRepository bookRepository;

    @BeforeEach
    void setup() {
        this.bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest() {
        //cenario
        Book book = getBook();

        //execucao
        when(bookRepository.save(any(Book.class)))
                .thenReturn(savedBook());

        Book savedBook = bookService.save(book);

        //verificacao
        assertNotNull(savedBook.getId());
        assertEquals("123", savedBook.getIsbn());
        assertEquals("As aventuras", savedBook.getTitle());
        assertEquals("Artur", savedBook.getAuthor());
    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com isbn duplicado")
    public void shouldNotSaveABookWithDuplicatedISBN() {
        //cenario
        Book book = getBook();

        //execucao
        when(bookService.save(book)).thenThrow(new BusinessException("Isbn já cadastrado"));
        when(bookRepository.existsByIsbn(anyString())).thenReturn(true);

        assertThrows(BusinessException.class, () -> bookService.save(book));
        verify(bookRepository, never()).save(book);
    }

    private Book getBook() {
        return Book.builder().author("Artur").title("As aventuras").isbn("123").build();
    }

    private Book savedBook() {
        return Book.builder().id(1L).author("Artur").title("As aventuras").isbn("123").build();
    }
}
