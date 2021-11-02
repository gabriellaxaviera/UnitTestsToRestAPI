package com.dev.unitests.service;

import com.dev.unitests.exception.BusinessException;
import com.dev.unitests.model.entity.Book;
import com.dev.unitests.repository.BookRepository;
import com.dev.unitests.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    @DisplayName("Deve obter um livro por Id")
    public void getBookById() {
        //cernario
        Long id = 1L;

        when(bookRepository.findById(id)).thenReturn(Optional.of(savedBook()));
        //execucao
        Optional<Book> byId = bookService.getById(id);

        //verificacoes
        assertNotNull(byId.get());
        assertEquals(id, byId.get().getId());
        assertEquals("Artur", byId.get().getAuthor());
        assertEquals("123", byId.get().getIsbn());
        assertEquals("As aventuras", byId.get().getTitle());
    }

    @Test
    @DisplayName("Deve retornar vazio quando o id do livro nao existe na base")
    public void getBookEmptyById() {
        //cernario
        Long id = 1L;

        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        //execucao
        Optional<Book> byId = bookService.getById(id);

        //verificacoes
        assertNotNull(byId);
    }

    @Test
    @DisplayName("Deve obter um livro por Id")
    public void deletBookById() {
        Book book = savedBook();

        assertDoesNotThrow(() -> bookService.delete(book));

        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    @DisplayName("Deve obter um livro por Id")
    public void deletBookWithErrorNotFoundById() {
        Book book = new Book();

        assertThrows(IllegalAccessException.class, () -> bookService.delete(book));

        verify(bookRepository, never()).delete(book);
    }

    @Test
    @DisplayName("Deve atualizar um livro.")
    public void updateBookTest(){
        //cenário
        long id = 3L;

        //livro a atualizar
        Book updatingBook = Book.builder().id(id).build();

        //simulacao
        Book updatedBook = savedBook();
        updatedBook.setId(id);

        when(bookRepository.save(updatingBook)).thenReturn(updatedBook);

        //exeucao
        Book book = bookService.update(updatingBook);

        //verificacoes
        assertEquals(book.getId(), updatedBook.getId());
        assertEquals(book.getTitle(), updatedBook.getTitle());
        assertEquals(book.getIsbn(), updatedBook.getIsbn());
        assertEquals(book.getAuthor(), updatedBook.getAuthor());

    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar atualizar um livro inexistente.")
    public void updateInvalidBookTest() {
        Book book = new Book();

        assertThrows(IllegalAccessException.class, () -> bookService.update(book));

        verify(bookRepository, never()).save(book);
    }

    @Test
    @DisplayName("Deve filtrar livros pelas propriedades")
    public void findBookTest(){
        //cenario
        Book book = getBook();

        PageRequest pageable = PageRequest.of(0, 10);

        Page<Book> page = new PageImpl<>(Arrays.asList(book), pageable, 1);

        when(bookRepository.findAll(any(Example.class), any(PageRequest.class)))
                .thenReturn(page);

        //execucao
        Page<Book> bookPage = bookService.find(book, pageable);

        //verificacoes
        assertEquals(1, bookPage.getTotalElements());
        assertEquals(Arrays.asList(book),bookPage.getContent());
        assertEquals(0, bookPage.getPageable().getPageNumber());
        assertEquals(10, bookPage.getPageable().getPageSize());
    }

    @Test
    @DisplayName("Deve obterum livro pelo isbn")
    public void getBookByIsbnTest(){
        String isbn = "1230";
        when(bookRepository.findByIsbn(isbn))
                .thenReturn(Optional.of(Book.builder().id(1L).isbn(isbn).build()));

        Optional<Book> bookByIsbn = bookService.getBookByIsbn(isbn);

        assertTrue(bookByIsbn.isPresent());
        assertEquals(1L, bookByIsbn.get().getId());
        assertEquals("1230", bookByIsbn.get().getIsbn());
    }


    private Book getBook() {
        return Book.builder().author("Artur").title("As aventuras").isbn("123").build();
    }

    private Book savedBook() {
        return Book.builder().id(1L).author("Artur").title("As aventuras").isbn("123").build();
    }
}
