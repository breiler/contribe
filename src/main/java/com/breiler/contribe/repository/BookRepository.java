package com.breiler.contribe.repository;

import com.breiler.contribe.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findByTitleContainingOrAuthorContaining(String title, String author);
}