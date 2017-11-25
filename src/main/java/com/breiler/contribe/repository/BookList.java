package com.breiler.contribe.repository;

import com.breiler.contribe.model.Book;
import org.springframework.stereotype.Service;

@Service
public interface BookList {

    Book[] list(String searchString);

    boolean add(Book book, int quantity);

    int[] buy(Book... books);

    StatusEnum getStatus(String bookId);
}