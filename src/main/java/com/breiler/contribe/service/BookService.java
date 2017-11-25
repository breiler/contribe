package com.breiler.contribe.service;


import com.breiler.contribe.model.Book;
import com.breiler.contribe.repository.BookList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BookService {

    private final BookList bookList;

    @Autowired
    public BookService(BookList bookList) {
        this.bookList = bookList;
    }

    public List<Book> findBooks(String query) {
        return Arrays.asList(bookList.list(query));
    }
}
