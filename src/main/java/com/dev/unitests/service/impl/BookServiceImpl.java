package com.dev.unitests.service.impl;

import com.dev.unitests.exception.BusinessException;
import com.dev.unitests.model.entity.Book;
import com.dev.unitests.repository.BookRepository;
import com.dev.unitests.service.BookService;
import lombok.SneakyThrows;

import java.util.Optional;

public class BookServiceImpl implements BookService {

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if (repository.existsByIsbn(book.getIsbn())) {
            throw new BusinessException("Isbn já cadastrado");
        }
        return repository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
        return this.repository.findById(id);
    }

    @SneakyThrows
    @Override
    public void delete(Book book) {
        if (book.getId() == null || book == null) {
            throw new IllegalAccessException("Book id cannot be null");
        }
        this.repository.delete(book);
    }

    @SneakyThrows
    @Override
    public Book update(Book book) {
        if (book.getId() == null || book == null) {
            throw new IllegalAccessException("Book id cannot be null");
        }
        return this.repository.save(book);
    }
}
