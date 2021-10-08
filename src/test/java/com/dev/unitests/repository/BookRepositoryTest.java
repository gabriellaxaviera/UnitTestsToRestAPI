package com.dev.unitests.repository;

import com.dev.unitests.model.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest //indica que vou fazer testes com JPA, cria instancia de banco em memoria para testes e ao final apaga tudo
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository bookRepository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado")
    public void returnTrueWhenIsbnExists() {
        //cenario
        String isbn = "123";
        Book book = createNewBook(isbn);
        entityManager.persist(book);

        //execucao
        boolean exists = bookRepository.existsByIsbn(isbn);

        //verificacao
        assertTrue(exists);
    }

    @Test
    @DisplayName("Deve retornar falso quando existir um livro na base com o isbn informado")
    public void returnFalseWhenIsbnExists() {
        //cenario
        String isbn = "123";

        //execucao
        boolean exists = bookRepository.existsByIsbn(isbn);

        //verificacao
        assertFalse(exists);
    }

    @Test
    @DisplayName("Deve retornar um livro por id")
    public void returnBookById() {
        //cenario
        Book book = createNewBook("123");
        entityManager.persist(book);

        //execucao
        Optional<Book> byId = bookRepository.findById(book.getId());

        //verificacoes
        assertTrue(byId.isPresent());
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        Book book = createNewBook("123");

        Book saved = bookRepository.save(book);

        assertNotNull(saved.getId());
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest(){
        Book book = createNewBook("123");
        entityManager.persist(book);

        Book foundBook = entityManager.find(Book.class, book.getId());
        bookRepository.delete(foundBook);

        Book deletedBook = entityManager.find(Book.class, book.getId());

        assertNull(deletedBook);
    }

    public static Book createNewBook(String isbn) {
        return Book.builder().title("As Aventuras").author("Fulano").isbn(isbn).build();
    }

}

